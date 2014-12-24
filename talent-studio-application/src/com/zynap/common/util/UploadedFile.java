package com.zynap.common.util;

import java.io.Serializable;

/**
 * Created: jsueiras
 * Date: 16-Jun-2005
 * Time: 15:42:04
 */
public class UploadedFile implements Serializable {

    public UploadedFile() {
    }

    public UploadedFile(String origFileName, Long fileSize, byte[] blobValue, String fileExtension) {
        this.fileName = origFileName;
        this.fileSize = fileSize;
        this.blobValue = blobValue;
        this.fileExtension = fileExtension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getBlobValue() {
        return blobValue;
    }

    public void setBlobValue(byte[] blobValue) {
        this.blobValue = blobValue;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * Returns true if file name has been set and file size has not or is zero.
     * <br/> Therefore is this returns true the upload had no data.
     * @return true or false
     */
    public boolean isInvalid() {
        return (isFileNameSet() && isFileEmpty());
    }

    public boolean isFileEmpty() {
        return (fileSize == null || fileSize.longValue() == 0);
    }

    public boolean isFileNameSet() {
        return (fileName != null && fileName.length() > 0);
    }

    public boolean isValid() {
        return (isFileNameSet() && !isFileEmpty());
    }

    private String fileName;

    private Long fileSize;

    private byte[] blobValue;

    private String fileExtension;

}
