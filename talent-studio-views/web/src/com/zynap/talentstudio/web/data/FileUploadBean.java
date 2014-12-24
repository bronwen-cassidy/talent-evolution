package com.zynap.talentstudio.web.data;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 *
 */
public class FileUploadBean implements Serializable {

    private MultipartFile file;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

}
