package com.zynap.talentstudio.web.portfolio;

import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

/**
 * User: amark
 * Date: 19-May-2005
 * Time: 14:24:29
 */
public class DocumentSearchValidator extends SubjectPositionAssociationValidator {

    public boolean supports(Class clazz) {
        return DocumentSearchWrapper.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        DocumentSearchWrapper wrapper = (DocumentSearchWrapper) obj;
        final DocumentSearchQuery searchQuery = wrapper.getSearchQuery();

        validateSearchParams(searchQuery, errors);
        final String queryText = searchQuery.getQueryText();
        if (wrapper.countPortfolioItemsSelected() <= 0) {
            if (!hasText(queryText)) {
                errors.rejectValue("queryText", "documentsearch.querytext.required", "Please enter some text.");
            }
        } else {
            if (!hasText(searchQuery.getExampleDocumentIds()) && !hasText(queryText)) {
                errors.rejectValue("selectedItems", "documentsearch.text.required", "Please enter some text and/or select portfolio items");                                
            }
        }
        if(wrapper.getSelectedContentTypes().isEmpty()) {
            errors.reject("error.content.type.required", "At least one content type must be selected");
        }
    }

    private boolean hasText(final String queryText) {
        return StringUtils.hasText(queryText);
    }
}
