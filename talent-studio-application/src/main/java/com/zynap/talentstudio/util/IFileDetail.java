/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.util;

/**
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IFileDetail {

    String getOrigFileName();

    void setOrigFileName(String filename);

    Long getFileSize();

    void setFileSize(Long fileSize);

    String getFileExtension();

    void setFileExtension(String extension);
}
