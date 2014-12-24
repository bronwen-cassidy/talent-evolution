package com.zynap.talentstudio.web.portfolio;


import org.apache.commons.lang.ArrayUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validator used when adding / editing portfolio items.
 * User: aandersson
 * Date: 04-May-2004
 * Time: 15:52:46
 */
public class PortfolioItemValidator implements Validator {

    public boolean supports(Class aClass) {
        return PortfolioItemWrapper.class.isAssignableFrom(aClass);
    }

    public void validate(Object o, Errors errors) {
        validateCommon(errors);
    }

    private void validateCommon(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.document.title.mandatory", "The Title for the document is a mandatory field");
    }

    public void validateRequiredFileItemValues(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contentTypeId", "error.contenttype.required", "Please select content type before you hit continue");
    }

    public void validateContentValues(PortfolioItemWrapper item, Errors errors) {
        validateCommon(errors);

        if (item.isText() && !StringUtils.hasText(item.getUploadedText())) {

            errors.rejectValue("uploadedText", "error.content.required", "This is a mandatory field. Please enter a value.");

        } else if (item.isUpload()) {

            if (!item.hasFile() || item.isDirtyFile()) {
                errors.rejectValue("file", "error.file.required", "Please select a file to upload.");
            }

        } else if (item.isURL()) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "error.content.required", "This is a mandatory field. Please enter a value.");
            final String url = item.getUrl();

            if (StringUtils.hasText(url)) {
                final int pos = url.indexOf("://");
                final String prefix = pos != -1 ? url.substring(0, pos) : null;
                if (!ArrayUtils.contains(ALLOWED_URL_PREFIXES, prefix)) {
                    errors.rejectValue("url", "invalid.url", "The url given must use the http protocol of http://");
                }
            }

        }
    }

    public void validateSubContentValues(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contentSubType", "error.select.content.subtype", "You must select a content sub type to continue");
    }

    private String[] ALLOWED_URL_PREFIXES = new String[] {"http", "https"};
}
