/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SubjectPositionAssociationValidator implements Validator {

    public boolean supports(Class clazz) {
        return DocumentSearchQuery.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
    }

    public void validateSearchParams(Object obj, Errors errors) {
        DocumentSearchQuery collator = (DocumentSearchQuery) obj;
        int maxResults = collator.getMaxResults();
        if(maxResults > 500 || maxResults < 1) {
            errors.rejectValue("maxResults", "maximum.search.results", "The maximum number of search results returned for any search should be between 1 and 500");
        }
        int threashold = collator.getThreashold();
        if(threashold > 100 || threashold < 0) {
            errors.rejectValue("threashold", "maximum.threshold", "The threshold must lie between the values of 0 and 100 inclusive");
        }
        if (collator.getQueryText().length() > 8000) {
            errors.rejectValue("queryText", "maximum.search.length", "The free text query must not contain more than 8000 characters");
        }
    }
}
