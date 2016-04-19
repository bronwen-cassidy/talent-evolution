package com.zynap.common.util;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * User: amark
 * Date: 10-Aug-2006
 * Time: 10:44:15
 */

public class UploadedFileTest {

    @Test
    public void testIsInvalid() throws Exception {

        UploadedFile uploadedFile = new UploadedFile();

        // default must be false as invalid file must have file name but no file size
        assertFalse(uploadedFile.isInvalid());

        // set file size and name
        uploadedFile.setFileName("filename");
        uploadedFile.setFileSize(10L);

        assertTrue(uploadedFile.isValid());
        assertFalse(uploadedFile.isInvalid());

        uploadedFile.setFileSize(0L);
        assertTrue(uploadedFile.isInvalid());
    }

    @Test
    public void testIsFileEmpty() throws Exception {

        UploadedFile uploadedFile = new UploadedFile();

        // default must be true
        assertTrue(uploadedFile.isFileEmpty());

        uploadedFile.setFileSize(0L);
        assertTrue(uploadedFile.isFileEmpty());

        uploadedFile.setFileSize(null);
        assertTrue(uploadedFile.isFileEmpty());

        uploadedFile.setFileSize(1L);
        assertFalse(uploadedFile.isFileEmpty());
    }

    @Test
    public void testIsFileNameSet() throws Exception {

        UploadedFile uploadedFile = new UploadedFile();

        // default must be false
        assertFalse(uploadedFile.isFileNameSet());

        uploadedFile.setFileName("");
        assertFalse(uploadedFile.isFileNameSet());

        uploadedFile.setFileName("test");
        assertTrue(uploadedFile.isFileNameSet());
    }

    @Test
    public void testIsValid() throws Exception {

        UploadedFile uploadedFile = new UploadedFile();

        // default must be false
        assertFalse(uploadedFile.isValid());

        uploadedFile.setFileName("");
        uploadedFile.setFileSize(0L);
        assertFalse(uploadedFile.isValid());

        uploadedFile.setFileName(null);
        uploadedFile.setFileSize(null);
        assertFalse(uploadedFile.isValid());

        uploadedFile.setFileName("test");
        uploadedFile.setFileSize(10L);
        assertTrue(uploadedFile.isValid());        
    }
}