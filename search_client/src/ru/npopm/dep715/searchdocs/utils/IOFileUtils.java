/*
 * ROSCOSMOS CORP. PROPERTY. 
 * Don't use without permission
 */
package ru.npopm.dep715.searchdocs.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 *
 * @author ayrat
 */
public class IOFileUtils {

    public static File getFileFromBytes(byte[] bytedFile, String path) throws
            IOException, FileNotFoundException {
        File file = new File(path);

        try (OutputStream fos = new FileOutputStream(file)) {
            //Создаем файловый ВЫХходной поток
            fos.write(bytedFile);

        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        }
        //Записываем в него байты
        return file;
    }

    public static byte[] getBytesFromFile(File file) throws IOException,
            FileNotFoundException {
        try (InputStream fis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            return bytes;
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        }
        //Создаем файловый ВХодной поток
        //Записываем в него байты
    }

    public static boolean stringToFile(String fileString, String text) {
        File file = new File(fileString);
        try {
            file.createNewFile();
            try (PrintWriter pr = new PrintWriter(file)) {
                    pr.println(text);
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
