package com.zynap.common.util;

/**
 * User: amark
 * Date: 10-Aug-2006
 * Time: 10:44:15
 */

import com.zynap.talentstudio.ZynapTestCase;

public class TestUploadedFile extends ZynapTestCase {

    public void testIsInvalid() throws Exception {

        UploadedFile uploadedFile = new UploadedFile();

        // default must be false as invalid file must have file name but no file size
        assertFalse(uploadedFile.isInvalid());

        // set file size and name
        uploadedFile.setFileName("filename");
        uploadedFile.setFileSize(new Long(10));

        assertTrue(uploadedFile.isValid());
        assertFalse(uploadedFile.isInvalid());

        uploadedFile.setFileSize(new Long(0));
        assertTrue(uploadedFile.isInvalid());
    }

    public void testIsFileEmpty() throws Exception {

        UploadedFile uploadedFile = new UploadedFile();

        // default must be true
        assertTrue(uploadedFile.isFileEmpty());

        uploadedFile.setFileSize(new Long(0));
        assertTrue(uploadedFile.isFileEmpty());

        uploadedFile.setFileSize(null);
        assertTrue(uploadedFile.isFileEmpty());

        uploadedFile.setFileSize(new Long(1));
        assertFalse(uploadedFile.isFileEmpty());
    }

    public void testIsFileNameSet() throws Exception {

        UploadedFile uploadedFile = new UploadedFile();

        // default must be false
        assertFalse(uploadedFile.isFileNameSet());

        uploadedFile.setFileName("");
        assertFalse(uploadedFile.isFileNameSet());

        uploadedFile.setFileName("test");
        assertTrue(uploadedFile.isFileNameSet());
    }

    public void testIsValid() throws Exception {

        UploadedFile uploadedFile = new UploadedFile();

        // default must be false
        assertFalse(uploadedFile.isValid());

        uploadedFile.setFileName("");
        uploadedFile.setFileSize(new Long(0));
        assertFalse(uploadedFile.isValid());

        uploadedFile.setFileName(null);
        uploadedFile.setFileSize(null);
        assertFalse(uploadedFile.isValid());

        uploadedFile.setFileName("test");
        uploadedFile.setFileSize(new Long(10));
        assertTrue(uploadedFile.isValid());        
    }
}