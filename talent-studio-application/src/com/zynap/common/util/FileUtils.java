/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.common.util;

import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * File upload utility class
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Extracts a file extension including the dot for given filename
     *
     * @param fileName
     * @return String representing the extension including the dot (for exampe .doc)
     */
    public static String getExtension(String fileName) {
        String extension = "";
        // Find the last dot
        if (fileName != null) {
            int locOfLastDot = fileName.lastIndexOf(".");
            extension = fileName.substring(locOfLastDot + 1);
        }
        return extension;
    }

    public static Properties loadPropertiesFile(String fileName) throws TalentStudioRuntimeException {

        Properties properties = new Properties();

        InputStream resourceAsStream = null;

        try {

            resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new TalentStudioRuntimeException("Unable to load properties file : " + fileName, e);
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException ignored) {
                }
            }
        }

        return properties;
    }
}
