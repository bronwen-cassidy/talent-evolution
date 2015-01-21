package com.zynap.talentstudio.integration.conversion;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.integration.common.IntegrationConstants;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Cell implements Serializable {

    public Cell(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isSubjectAssociation() {
        return IntegrationConstants.SUBJECT_ASSOCIATION_FIELD.equals(key);
    }

    public boolean isOuPosition() {
        return IPopulationEngine.PARENT_ATTR.equals(key) || "organisationUnit".equals(key);
    }

    public boolean isSourceAssociations () {
        return IntegrationConstants.SOURCE_ASSOCIATION_FIELD.equals(key);
    }

    private String key;
    private String value;
}
