/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.tag;

import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.util.collections.CollectionSorter;

import com.zynap.talentstudio.web.tag.properties.TagGeneralProperties;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PermissionTag extends ZynapTagSupport {

    protected int doInternalStartTag() throws Exception {

        properties = new TagGeneralProperties();

        CollectionSorter.PrimaryAttrHolder primaryAttrHolder = CollectionSorter.sort("content", "action", permissions);

        StringBuffer buf = new StringBuffer();
        buf.append(generateTableHeader(primaryAttrHolder));
        buf.append("<tbody>");
        buf.append(generateTableBody(primaryAttrHolder));
        buf.append("\n</tbody>");
        buf.append("\n</table>");

        out.print(buf.toString());

        return EVAL_BODY_INCLUDE;
    }

    protected String generateTableBody(CollectionSorter.PrimaryAttrHolder permissions) {

        StringBuffer bodyBuffer = new StringBuffer();

        Map uniquePrimaryAttributes = permissions.getEntries();
        Collection uniqueSecondaryAttributes = permissions.getUniqueSecondaryAttributes();

        int index = 0;
        for (Iterator primaryAttrIterator = uniquePrimaryAttributes.entrySet().iterator(); primaryAttrIterator.hasNext(); index++) {

            String styleClass = (index % 2 == 0) ? "even" : "odd";

            Map.Entry entry = (Map.Entry) primaryAttrIterator.next();
            CollectionSorter.SecondaryAttrHolder secondaryAttr = (CollectionSorter.SecondaryAttrHolder) entry.getValue();

            bodyBuffer.append(replaceTokens(properties.getPermissionTrStart(), new Object[]{styleClass, entry.getKey()}));

            for (Iterator secondaryAttrIterator = uniqueSecondaryAttributes.iterator(); secondaryAttrIterator.hasNext();) {
                String secondaryAttribute = (String) secondaryAttrIterator.next();
                IPermit permit = (IPermit) secondaryAttr.get(secondaryAttribute);

                // if editable display checkboxes
                if (Boolean.valueOf(editable).booleanValue()) {
                    if (permit != null) {
                        if (permit.isSelected()) {
                            bodyBuffer.append(replaceTokens(properties.getPermissionChecked(), new Object[]{bindName, permit.getId()}));
                        } else {
                            bodyBuffer.append(replaceTokens(properties.getPermissionUnchecked(), new Object[]{bindName, permit.getId()}));
                        }
                    } else {
                        bodyBuffer.append(properties.getPermissionNoEntry());
                    }
                } else {
                    if (permit != null) {
                        if (permit.isSelected()) {
                            bodyBuffer.append(properties.getPermissionActive());
                        } else {
                            bodyBuffer.append(properties.getPermissionInactive());
                        }
                    } else {
                        bodyBuffer.append(properties.getPermissionNoEntry());
                    }
                }
            }

            bodyBuffer.append("\n</tr>");
        }

        return bodyBuffer.toString();
    }

    private String generateTableHeader(CollectionSorter.PrimaryAttrHolder primaryAttrHolder) {

        StringBuffer buf = new StringBuffer();
        buf.append(replaceTokens(properties.getPermissionTableTop(), new Object[]{label}));

        Collection uniqueSecondaryAttributes = primaryAttrHolder.getUniqueSecondaryAttributes();
        String permissionTableHeader = properties.getPermissionTableHeader();
        for (Iterator iterator = uniqueSecondaryAttributes.iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            buf.append(replaceTokens(permissionTableHeader, new Object[]{string}));
        }

        buf.append(properties.getPermissionTableTopClose());

        return buf.toString();
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(permissions.toString())) {
            this.permissions = (Collection) ExpressionEvaluationUtils.evaluate(permissions.toString(), permissions.toString(), Object.class, pageContext);
        } else {
            this.permissions = (Collection) permissions;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) throws JspException {
        this.label = label;
    }

    public String getBindName() {
        return bindName;
    }

    public void setBindName(String bindName) {
        this.bindName = bindName;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    /**
     * Reset all attributes to default values.
     */
    public void release() {
        this.editable = null;
    }

    private Collection permissions;
    private String label;
    private String bindName;
    private String editable;
    private TagGeneralProperties properties;
}
