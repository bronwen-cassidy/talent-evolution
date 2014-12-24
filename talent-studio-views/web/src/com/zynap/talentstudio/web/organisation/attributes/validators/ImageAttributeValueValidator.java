/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.util.ArrayUtils;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ImageAttributeValueValidator implements IAttributeValueSpecification {

    public ErrorMessageHandler validate(AttributeWrapperBean attributeWrapperBean, Long nodeId) {

        if(attributeWrapperBean.isDirtyFile()) return new ErrorMessageHandler(NOT_AN_IMAGE_FILE, attributeWrapperBean.getUploadedFileOriginalName());

        if(!StringUtils.hasText(attributeWrapperBean.getValue())) return null;

        if(attributeWrapperBean.getUploadedFile() == null) return null;

        return validateFileEnding(attributeWrapperBean.getUploadedFile().getFileExtension());
    }

    public static ErrorMessageHandler validateFileEnding(String fileEnding) {        
        return ArrayUtils.contains(VALID_FILE_EXTENSIONS, fileEnding) ? null : new ErrorMessageHandler(ERROR_CODE);
    }

    public void setDynamicAttributeService(IDynamicAttributeService attributeService) {}

    private static String[] VALID_FILE_EXTENSIONS = {"gif", "jpeg", "jpg", "bmp", "png", "ico"};
    private static final String ERROR_CODE = "not.an.image";
    private static final String NOT_AN_IMAGE_FILE = "image.file.invalid";
}

