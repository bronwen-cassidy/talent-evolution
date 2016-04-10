/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security.homepages;

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.security.homepages.HomePage;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * User: amark
 * Date: 16-Mar-2005
 * Time: 16:09:27
 * Todo: save the filename as well
 */
public class HomePageWrapperBean implements Serializable {

    public HomePageWrapperBean(HomePage homePage, Arena arena) {
        
        this.homePage = homePage;
        this.arena = arena;
        if (homePage.getData() != null && homePage.getData().length > 0) {
            this.fileExtension = homePage.getFileExtension() != null ? homePage.getFileExtension() : "html";
            this.data = new UploadedFile("*." + homePage.getFileExtension(), (long) homePage.getData().length, homePage.getData(), homePage.getFileExtension());
        } else {
            this.data = new UploadedFile();
        }
        this.url = homePage.getUrl();
    }

    public HomePage getModifiedHomePage() {
        this.homePage.setData(data.isFileEmpty() ? null : data.getBlobValue());
        this.homePage.setUrl(StringUtils.hasText(url) ? url : null);
        this.homePage.setFileExtension(fileExtension);
        return homePage;
    }

    public void setUploadHomePage(UploadedFile file) {
        if (file != null && !file.isFileEmpty()) {
            this.data = file;
            this.fileExtension = file.getFileExtension();
        }
    }

    public UploadedFile getUploadHomePage() {
        return data;
    }

    public boolean isHasUpload() {
        return !data.isFileEmpty();
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public String getUrl() {
        return url;
    }

    public String getArenaLabel() {
        return arena.getLabel();
    }

    public void deleteUpload() {
        data.setBlobValue(new byte[0]);
        data.setFileSize(new Long(0));
    }

    public String getArenaId() {
        return arena.getArenaId();
    }

    private UploadedFile data;
    private HomePage homePage;
    private Arena arena;
    private String url;

    private String fileExtension;
}
