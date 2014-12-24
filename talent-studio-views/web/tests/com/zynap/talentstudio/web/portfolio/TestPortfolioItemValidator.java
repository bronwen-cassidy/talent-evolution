/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

import junit.framework.TestCase;

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItemFile;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class TestPortfolioItemValidator extends TestCase {

    protected void setUp() throws Exception {
        portfolioItemValidator = new PortfolioItemValidator();
    }

    protected void tearDown() throws Exception {
        portfolioItemValidator = null;
    }

    public void testSupports() throws Exception {
        assertTrue(portfolioItemValidator.supports(PortfolioItemWrapper.class));
    }

    public void testValidateUploadNoFile() throws Exception {

        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        itemWrapper.setContentSubType(PortfolioItem.UPLOAD_SUBTYPE);
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", "yes sir");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError fieldError = ex.getFieldError("file");
            assertEquals("error.file.required", fieldError.getCode());
        }
    }

    public void testValidateUploadEmptyFile() throws Exception {

        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        itemWrapper.setContentSubType(PortfolioItem.UPLOAD_SUBTYPE);
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", "yes sir");
        binder.bind(values);

        final String fileName = "fileName.txt";
        final Long fileSize = new Long(0);
        final String fileExtension = ".txt";
        itemWrapper.setFile(new UploadedFile(fileName, fileSize, null, fileExtension));

        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError fieldError = ex.getFieldError("file");
            assertEquals("error.file.required", fieldError.getCode());
        }
    }

    public void testEditUploadNoLabel() throws Exception {

        // label is required when editing a portfolio item
        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        itemWrapper.setContentSubType(PortfolioItem.UPLOAD_SUBTYPE);
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", "");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError fieldError = ex.getFieldError("label");
            assertEquals("error.document.title.mandatory", fieldError.getCode());
        }
    }

    public void testEditUploadNoFile() throws Exception {

        final PortfolioItem fileItem = new PortfolioItem();
        fileItem.setOrigFileName("mycv.txt");
        PortfolioItemWrapper itemWrapper = new PortfolioItemWrapper(fileItem);

        // uploading a file is not required when editing a portfolio item
        itemWrapper.setContentSubType(PortfolioItem.UPLOAD_SUBTYPE);
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", "yes sir");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateContentValues() throws Exception {

        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        itemWrapper.setContentSubType(PortfolioItem.URL_SUBTYPE);
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", " ");
        values.addPropertyValue("url", "not to be tested information");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError fieldError = ex.getFieldError("label");
            assertEquals("error.document.title.mandatory", fieldError.getCode());
        }
    }

    public void testValidateContentValuesInvalidUrl() throws Exception {

        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        itemWrapper.setContentSubType(PortfolioItem.URL_SUBTYPE);
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", "yes sir");
        values.addPropertyValue("url", "to be tested information");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError fieldError = ex.getFieldError("url");
            assertNotNull(fieldError);
            assertEquals("invalid.url", fieldError.getCode());
        }
    }

    public void testValidateContentValuesPartInvalidUrl() throws Exception {

        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        itemWrapper.setContentSubType(PortfolioItem.URL_SUBTYPE);
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", "yes sir");
        values.addPropertyValue("url", "www.xyz.com");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError fieldError = ex.getFieldError("url");
            assertNotNull(fieldError);
            assertEquals("invalid.url", fieldError.getCode());
        }
    }

    public void testValidateContentValuesFTPUrl() throws Exception {

        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        itemWrapper.setContentSubType(PortfolioItem.URL_SUBTYPE);
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", "yes sir");
        values.addPropertyValue("url", "ftp://www.xyz.com");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError fieldError = ex.getFieldError("url");
            assertNotNull(fieldError);
            assertEquals("invalid.url", fieldError.getCode());
        }
    }

    public void testValidateContentValuesTextValue() throws Exception {

        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        itemWrapper.setContentSubType(PortfolioItem.TEXT_SUBTYPE);
        itemWrapper.setUploadedText("");
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("label", "test Label");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateContentValues(itemWrapper, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError fieldError = ex.getFieldError("uploadedText");
            assertNotNull(fieldError);
            assertEquals("error.content.required", fieldError.getCode());
        }
    }

    public void testValidateRequiredValues() throws Exception {

        PortfolioItemWrapper itemWrapper = getPortfolioItemWrapper();

        //x.setContentType(new ContentType());
        DataBinder binder = new DataBinder(itemWrapper, "item");
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("contentTypeId", "");
        binder.bind(values);
        Errors errors = binder.getBindingResult();
        portfolioItemValidator.validateRequiredFileItemValues(errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException e) {
            FieldError fieldError = e.getFieldError("contentTypeId");
            assertNotNull(fieldError);
            assertEquals("error.contenttype.required", fieldError.getCode());
        }
    }

    private PortfolioItemWrapper getPortfolioItemWrapper() {
        return new PortfolioItemWrapper(new PortfolioItem(), new PortfolioItemFile());
    }

    private PortfolioItemValidator portfolioItemValidator;
}
