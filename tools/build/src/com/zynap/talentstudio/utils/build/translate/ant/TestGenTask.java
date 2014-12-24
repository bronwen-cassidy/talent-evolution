/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.utils.build.translate.ant;

import com.zynap.talentstudio.utils.build.translate.ActionsFactory;
import com.zynap.talentstudio.utils.build.translate.elements.Actions;
import com.zynap.talentstudio.utils.build.translate.generate.TestClassGenerator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 20-Jun-2006 14:19:12
 */
public class TestGenTask extends Task {

    public void execute() throws BuildException {

        final Pattern excludedFilesPattern = StringUtils.isNotBlank(fileExcludePattern) ? Pattern.compile(fileExcludePattern) : null;

        if (baseXmlDirectory == null) throw new BuildException("The base directory to where the test xml files are stored must be specified");
        if (genSourceDirectory == null) throw new BuildException("Please specify the path to place the generated test source classes");
        if (packagePrefix == null) {
            packagePrefix = "com.zynap.talentstudio.web.testgen4web";
        }
        if (testTemplate == null) {
            testTemplate = DEFAULT_VELOCITY_TEMPLATE;
        }

        try {
            final TestClassGenerator generator = new TestClassGenerator();
            final ActionsFactory actionsFactory = new ActionsFactory();

            logger.debug("Generating test classes from directory: " + baseXmlDirectory + " using template :" + testTemplate);

            final List testXmlFiles = loadFiles(new File(baseXmlDirectory), null);
            logger.debug("Found " + testXmlFiles.size() + " test definition files");

            for (int i = 0; i < testXmlFiles.size(); i++) {
                final File file = (File) testXmlFiles.get(i);
                final String fileName = file.getName();

                if (excludedFilesPattern == null || !excludedFilesPattern.matcher(fileName).matches()) {

                    String path = getFilePath(file, null);
                    // remove the / at the beginning
                    path = path.substring(1);

                    // package name will be parent dirs up to genSourceDir + the prefix
                    final String packageName = formatPackageName(path, file);
                    Actions actions = actionsFactory.parse(path);
                    generator.generate(actions, genSourceDirectory, testTemplate, fileName, packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    public void setBaseXmlDirectory(String baseXmlDirectory) {

        if (StringUtils.isNotBlank(baseXmlDirectory)) {
            if (baseXmlDirectory.startsWith(FILE_DELIMITER)) this.baseXmlDirectory = baseXmlDirectory.substring(1);
            else this.baseXmlDirectory = baseXmlDirectory;
        }
    }

    public void setGenSourceDirectory(String genSourceDirectory) {
        this.genSourceDirectory = genSourceDirectory;
    }

    public void setFileExcludePattern(String fileExcludePattern) {
        this.fileExcludePattern = fileExcludePattern;
    }

    public void setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    public void setTestTemplate(String testTemplate) {
        this.testTemplate = testTemplate;
    }

    private String formatPackageName(String path, File file) {

        final String prefix = packagePrefix + FILE_DELIMITER;

        String basePath = new File(baseXmlDirectory).getPath();
        String output;
        if (path.indexOf(basePath) != -1) {
            output = path.substring(baseXmlDirectory.length() + 1, path.indexOf(file.getName()) - 1);
        } else {
            output = path.substring(0, path.indexOf(file.getName()) - 1);
        }

        return StringUtils.replace(prefix + output, FILE_DELIMITER, PACKAGE_DELIMITER);
    }

    private String getFilePath(File file, StringBuffer path) {

        if (path == null) path = new StringBuffer();
        final String name = file.getName();
        path.insert(0, name).insert(0, FILE_DELIMITER);
        if (!name.equals(baseXmlDirectory)) {
            if (file.getParentFile() != null) {
                getFilePath(file.getParentFile(), path);
            }
        }
        return path.toString();
    }

    private List loadFiles(File file, List<File> result) throws Exception {

        if (result == null) result = new ArrayList<File>();
        if (file.isDirectory()) {
            file.mkdirs();
            final File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File subFile = files[i];
                loadFiles(subFile, result);
            }
        } else {
            result.add(file);
        }

        return result;
    }

    private String baseXmlDirectory;
    private String genSourceDirectory;
    private String packagePrefix;
    private String testTemplate;
    private String fileExcludePattern;

    private final String DEFAULT_VELOCITY_TEMPLATE = "com" + FILE_DELIMITER + "zynap" + FILE_DELIMITER + "talentstudio" + FILE_DELIMITER +
            "utils" + FILE_DELIMITER + "build" + FILE_DELIMITER + "translate" + FILE_DELIMITER + "generate" + FILE_DELIMITER + "ZynapWebTestTemplate.vm";


    /**
     * Logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    public static final String FILE_DELIMITER = File.separator;
    public static final String PACKAGE_DELIMITER = ".";
}
