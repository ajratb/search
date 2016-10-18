/*
 * 
 */
package searchdocs.utils;

import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.tika.detect.EncodingDetector;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.txt.Icu4jEncodingDetector;
import org.apache.tika.sax.ToXMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import searchdocs.utils.IOFileUtils;

/**
 * Класс, позволяющий получать структурированный файл(xhtml) из переданного(pdf,
 * doc, docx)
 *
 * @author ayrat
 */
public class TikaUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TikaUtils.class);

    //переделать название?
    public static String start(File file) throws IOException, SAXException, TikaException {
        LOG.info("Start doc to xml transformation\nDoc file:{}",file);

        byte[] b = IOFileUtils.getBytesFromFile(file);
        return start(b);
    }

    public static String start(byte[] bytes) throws IOException, SAXException, TikaException {

//        byte[] reEncodedBytes = getUtf8EncodedBytes(bytes);
//
//        return parseFileBytesToXHTML(reEncodedBytes);

            return parseFileBytesToXHTML(bytes);
    }

    private static byte[] getUtf8EncodedBytes(byte[] source) {

        String charset = getCharSet(source);
        String utf8String = "";
        try {
            utf8String = new String(source, charset);
        } catch (UnsupportedEncodingException ex) {
            LOG.error("", ex);
        }

        LOG.info(utf8String);
        return utf8String.getBytes();

    }

    private static String getCharSet(byte[] source) {
        String resultString = "";
        EncodingDetector r = new Icu4jEncodingDetector();
        try (TikaInputStream stream = TikaInputStream.get(source)) {
            Charset chs = r.detect(stream, new Metadata());
            resultString = chs.name();
        } catch (IOException ex) {
            LOG.error("", ex);
        }
        LOG.info("The charset of source document is {}",resultString);
        return resultString;
    }

    private static String parseFileBytesToXHTML(byte[] fileBytes) throws IOException,
            SAXException, TikaException {
        ContentHandler handler = new ToXMLContentHandler() {
        };

        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();

        //Here was ContentHandlerExample class
        try (InputStream stream = new ByteArrayInputStream(fileBytes)) {
            parser.parse(stream, handler, metadata);

            return handler.toString();
        }
    }

    public static void main(String[] args) {
       
        Path directory = Paths.get("./test_res/docs");
        if (Files.exists(directory)) {
            try {
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        String fileName=file.getFileName().toString();
                        fileName="./test_res/xhtml/"+fileName+".xml";
                        try {
                            IOFileUtils.stringToFile(fileName, start(file.toFile()));
                        } catch (SAXException ex) {
                            ex.printStackTrace();
                        } catch (TikaException ex) {
                            ex.printStackTrace();
                        }

//                        byte[] b = IOFileUtils.getBytesFromFile(file.toFile());
//                        System.out.println(file + " is encoded in " + getCharSet(b));
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        

    }

}
