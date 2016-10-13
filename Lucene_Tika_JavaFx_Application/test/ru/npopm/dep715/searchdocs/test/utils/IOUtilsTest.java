/*
 * ROSCOSMOS CORP. PROPERTY. 
 * Don't use without permission
 */
package ru.npopm.dep715.searchdocs.test.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.npopm.dep715.searchdocs.utils.IOFileUtils;

/**
 *
 * @author ayrat
 */
public class IOUtilsTest {
    private static final IOFileUtils utils= new IOFileUtils();
    private static final File file=new File("test_res/ioutils/test.txt");
    
    @BeforeClass
    public static void setUpClass() {
       
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void bytes_from_source_and_target_must_be_equals() {
        try {
            if(!file.exists())file.createNewFile();
            byte[] sourceBytes=utils.getBytesFromFile(file);
            File targetFile=new File("test_res/ioutils/newfile.txt");
//            targetFile.createNewFile(); - файл итак будет создан во время 
//               работы след.функции
            utils.getFileFromBytes(sourceBytes, "test_res/ioutils/newfile.txt");
            byte[] targetBytes=utils.getBytesFromFile(targetFile);
            assertTrue(Arrays.equals(sourceBytes, targetBytes));
        } catch (IOException ex) {
            fail("IOException raised");
        }
     }
     
//     @Test
//     public void content_from_source_and_target_must_be_equals() {}
}
