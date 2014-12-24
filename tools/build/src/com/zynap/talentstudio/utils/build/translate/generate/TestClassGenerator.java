/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.utils.build.translate.generate;

import com.zynap.talentstudio.utils.build.translate.ant.TestGenTask;
import com.zynap.talentstudio.utils.build.translate.elements.Actions;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Jun-2006 13:57:49
 */
public class TestClassGenerator {

    public TestClassGenerator() throws Exception {
        velocityEngine = new VelocityEngine();
        velocityEngine.addProperty(VelocityEngine.RESOURCE_LOADER, "class");
        velocityEngine.addProperty("class." + VelocityEngine.RESOURCE_LOADER + ".description", "Classpath loader");
        velocityEngine.addProperty("class." + VelocityEngine.RESOURCE_LOADER + ".class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            velocityEngine.init();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public String escape(String value) {
        return org.apache.commons.lang.StringEscapeUtils.escapeJava(value);
    }

    /**
     * Form submits with submit buttons have values.
     *
     * @param value
     * @return true or false
     */
    public boolean isFormSubmit(String value) {
        return value != null && isFormInputParameter(value) && hasValue(value);
    }

    /**
     * Form submits with normal buttons have no values.
     *
     * @param value
     * @return true or false
     */
    public boolean isButtonSubmit(String value) {
        return value != null && isFormInputParameter(value) && !hasValue(value);
    }

    /**
     * Check for link tag.
     *
     * @param value
     * @return true or false
     */
    public boolean isLink(String value) {
        return value != null && value.indexOf("A[") != -1;
    }

    /**
     * Is the URL going to the login or logout pages.
     *
     * @param value
     * @return true or false
     */
    public boolean isLoginOrLogout(String value) {
        return value != null && (value.indexOf("login.htm") != -1 || value.indexOf("logout.htm") != -1);
    }

    public void generate(Actions actions, String genDir, String selectedVelocityTemplate, String fileName, String packageName) throws Exception {

        fileName = removePrefix(fileName);
        final Boolean useDbUnit = new Boolean(fileName.startsWith(DBUNIT_PREFIX));

        final Template template = velocityEngine.getTemplate(selectedVelocityTemplate);

        final String dirName = StringUtils.replace(packageName, TestGenTask.PACKAGE_DELIMITER, TestGenTask.FILE_DELIMITER);
        final String destinationDirectory = genDir + TestGenTask.FILE_DELIMITER + dirName;
        new File(destinationDirectory).mkdirs();

        final String className = formatForJava(fileName);
        final Writer writer = new FileWriter(destinationDirectory + TestGenTask.FILE_DELIMITER + "Test" + className + ".java");
        try {
            VelocityContext context = new VelocityContext();
            context.put("className", className);
            context.put("fileName", fileName);
            context.put("testName", className);
            context.put("packageName", packageName);
            context.put("actions", actions.getActions());
            context.put("util", this);
            context.put("useDbUnit", useDbUnit);

            template.merge(context, writer);
        } finally {
            writer.close();
        }
    }

    private String formatForJava(String fileName) {
        return org.apache.commons.lang.StringUtils.capitalize(fileName);
    }

    private boolean hasValue(String value) {
        return value.indexOf("VALUE") != -1;
    }

    private boolean isFormInputParameter(String value) {
        return value.indexOf("FORM") != -1 && isInput(value);
    }

    private boolean isInput(String value) {
        return value.indexOf("INPUT") != -1;
    }

    private String removePrefix(String fileName) {

        if (fileName.indexOf(TestGenTask.PACKAGE_DELIMITER) != -1) {
            fileName = fileName.substring(0, fileName.indexOf(TestGenTask.PACKAGE_DELIMITER));
        }

        return fileName;
    }

    private VelocityEngine velocityEngine;

    private static final String DBUNIT_PREFIX = "dbunit";
}
