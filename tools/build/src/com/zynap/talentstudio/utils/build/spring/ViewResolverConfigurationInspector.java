/* Copyright: Copyright (c) 2004
 * Company:
 */
package com.zynap.talentstudio.utils.build.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.PropertyResourceBundle;


/**
 * .
 *
 * @author Angus Mark
 */
public class ViewResolverConfigurationInspector {

    /**
     * Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(ViewResolverConfigurationInspector.class);

    private static final String CSV_SUFFIX = ".attributesCSV";

    private static final String CLASS_SUFFIX = ".class";

    private static final String WEB_INF_DIR = "WEB-INF/jsp/common";

    private static final String URL_SUFFIX = ".url";

    private static final String CONTENT_PROPERTY_KEY = "content";

    private static final String JSP_EXTENSION = ".jsp";

    private static final String SPRING_ACTION_EXTENSION = ".htm";

    private PropertyResourceBundle propertyResourceBundle;

    private Collection<String> invalidConfigEntries;

    private final String baseDir;

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            throw new IllegalArgumentException("Two arguments expected - JSP base dir and filename to inspect");
        }

        String baseDir = args[0];
        String fileName = args[1];

        logger.debug("JSP directory: " + baseDir);
        logger.debug("Configuration file: " + fileName);

        ViewResolverConfigurationInspector inspector = new ViewResolverConfigurationInspector(baseDir, fileName);
        Collection invalidConfigEntries = inspector.inspect();

        final int numberOfInvalidConfigs = invalidConfigEntries.size();
        logger.info("Found " + numberOfInvalidConfigs + " invalid config entries in ResourceBundleViewResolver file " + fileName);
        for (Iterator iterator = invalidConfigEntries.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            logger.error("Invalid entry: " + s);
        }

        if (numberOfInvalidConfigs > 0) {
            throw new RuntimeException("Configuration file " + fileName + " is invalid");
        }
    }

    public ViewResolverConfigurationInspector(String baseDir, String fileName) throws IOException {
        this.baseDir = baseDir;

        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        this.propertyResourceBundle = new PropertyResourceBundle(inputStream);
    }

    public Collection inspect() {
        invalidConfigEntries = new ArrayList<String>();

        Enumeration entries = propertyResourceBundle.getKeys();
        while (entries.hasMoreElements()) {
            String key = (String) entries.nextElement();

            if (key.indexOf(CLASS_SUFFIX) > 0) {
                recordStatus(key, processClassEntry(key));
            } else if (key.indexOf(URL_SUFFIX) > 0) {
                recordStatus(key, processURLEntry(key));
            } else if (key.indexOf(CSV_SUFFIX) > 0) {
                recordStatus(key, processCSVEntry(key));
            }
        }

        return invalidConfigEntries;
    }

    private void recordStatus(String key, boolean valid) {
        if (!valid) invalidConfigEntries.add(key);
    }

    /**
     * Checks that for every ".attributesCSV" entry there is a "content=" part that points to a JSP in the filesystem.
     *
     * @param key
     * @return true or false
     */
    private boolean processCSVEntry(String key) {

        String value = getProperty(key);

        key = removeSuffix(key, CSV_SUFFIX);
        return processCSVEntry(key, value);
    }

    private String getProperty(String key) {

        String value = null;
        try {
            value = propertyResourceBundle.getString(key);
        } catch (Exception e) {}

        return value;
    }

    private boolean processCSVEntry(String key, String value) {
        String content = null;

        String[] values = StringUtils.tokenizeToStringArray(value, ",");
        for (int i = 0; i < values.length; i++) {
            String s = values[i];
            if (s.indexOf(CONTENT_PROPERTY_KEY) >= 0) {
                content = StringUtils.tokenizeToStringArray(s, "=")[1];
            }
        }

        return processURL(key, content);
    }

    /**
     * Checks that for every ".url" entry there is an equivalent ".class" entry.
     *
     * @param key
     * @return true or false
     */
    private boolean processURLEntry(String key) {

        key = removeSuffix(key, URL_SUFFIX);

        String propertyName = appendSuffix(key, CLASS_SUFFIX);
        String value = getProperty(propertyName);
        return StringUtils.hasText(value);
    }

    /**
     * Checks that for every ".class" entry there is an equivalent and valid ".url" entry.
     *
     * @param key
     * @return true or false
     */
    private boolean processClassEntry(String key) {

        key = removeSuffix(key, CLASS_SUFFIX);

        String propertyName = appendSuffix(key, URL_SUFFIX);
        String value = getProperty(propertyName);
        return processURL(key, value);
    }

    private String removeSuffix(String text, String suffix) {
        return text.substring(0, text.indexOf(suffix));
    }

    private String appendSuffix(String text, String suffix) {
        return text.concat(suffix);
    }

    private boolean processURL(String key, String url) {
        logger.debug("Checking config entry: " + key + ", URL: " + url);

        // if url is not blank
        if (StringUtils.hasText(url)) {

            // check that url ends in .jsp or in .htm
            if (url.indexOf(JSP_EXTENSION) > 0) {
                return fileExists(url);
            } else if (url.indexOf(SPRING_ACTION_EXTENSION) > 0) {

                // this must be a redirect to another view defined in the same config file so take the url and check for a csv entry matching the URL
                String newURL = removeSuffix(url, SPRING_ACTION_EXTENSION);
                String redirectURL = getProperty(appendSuffix(newURL, CSV_SUFFIX));
                if (StringUtils.hasText(redirectURL)) {
                    return processCSVEntry(key, redirectURL);
                }
            }
        }


        return false;
    }

    private boolean fileExists(String filePath) {
        String pathname;
        if (!filePath.startsWith("/WEB-INF/jsp")) {
            filePath = StringUtils.replace(filePath, "[", "");
            filePath = StringUtils.replace(filePath, "]", "");
            pathname = baseDir + "/" + WEB_INF_DIR + "/" + filePath;
        } else {
            pathname = baseDir + "/" + filePath;
        }

        logger.debug("Pathname is: " + pathname);
        File file = new File(pathname);
        return file.exists();
    }
}
