/*
 * 
 */
package searchdocs.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ayrat
 */
public class NIO2Utils {

//    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(_IndexFiles_.class);

    public static void deleteDirectory(String dirString) throws IOException {
        Path directory = Paths.get(dirString);
//        try {
        if (Files.exists(directory)) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        }

    }

    public static void deleteDirectoryContent(String dirString) throws IOException {
        Path directory = Paths.get(dirString);
        if (Files.exists(directory)) {

            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });

        }
    }

}
