/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Jan-2008 14:18:18
 */
public class AttributeSet implements Serializable {

    AttributeSet(String type, String viewType, boolean includeQuestionnaires, boolean includeCalculatedFields, boolean includeDynamicAttributes) {
        this.type = type;
        this.viewType = viewType;
        this.includeQuestionnaires = includeQuestionnaires;
        this.includeCalculatedFields = includeCalculatedFields;
        this.includeDynamicAttributes = includeDynamicAttributes;
    }

    void addAttribute(AttributeWrapperBean attributeWrapperBean) {
        if (attributeWrapperBean != null) {
            attributeWrapperBean.setIsOrgunitBranch(Node.ORG_UNIT_TYPE_.equals(type));
            attributeWrapperBeans.add(attributeWrapperBean);
        }
    }

    String getType() {
        return type;
    }

    String getViewType() {
        return viewType;
    }

    List<FormAttribute> getAttributeWrapperBeans() {
        return attributeWrapperBeans;
    }

    boolean isIncludeQuestionnaires() {
        return includeQuestionnaires;
    }

    public boolean isIncludeCalculatedFields() {
        return includeCalculatedFields;
    }

    public boolean isIncludeDynamicAttributes() {
        return includeDynamicAttributes;
    }

    private final List<FormAttribute> attributeWrapperBeans = new ArrayList<FormAttribute>();
    private final String type;
    private final String viewType;
    private final boolean includeQuestionnaires;
    private final boolean includeCalculatedFields;
    private boolean includeDynamicAttributes;
}

