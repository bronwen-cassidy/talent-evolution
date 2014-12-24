/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web;


import com.meterware.httpunit.Button;
import com.meterware.httpunit.WebForm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 20-Jun-2006 09:07:07
 */
public abstract class ZynapWebGenTestCase extends ZynapWebTestCase {

    protected final void submitForm(String xpath, String value) throws Exception {

        XPathExpression xpathExpression = new XPathExpression(xpath);

        final WebForm form = getForm(xpathExpression);
        final String inputType = getElementType(xpathExpression);

        if (inputType == null) {
            String buttonName = getElementIdOrName(INPUT_ELEM_KEY, xpathExpression);

            Button button = form.getSubmitButton(buttonName);
            if (button == null) {
                button = getButtonByIdOrName(form, buttonName);
            }

            button.click();

        } else if (inputType.equalsIgnoreCase(SUBMIT_BUTTON_TYPE)) {
            form.submit();
        } else {
            form.submitNoButton();
        }
    }

    protected final void clickButton(String xpath, String value) {

        XPathExpression xpathExpression = new XPathExpression(xpath);

        final String buttonId = xpathExpression.getValue(INPUT_ELEM_KEY, ID_ATTR_KEY);
        clickButton(buttonId);
    }

    protected final void setCheckBox(String xpath, String value) {

        XPathExpression xpathExpression = new XPathExpression(xpath);

        final WebForm form = getForm(xpathExpression);

        final String inputElementId = getElementIdOrName(INPUT_ELEM_KEY, xpathExpression);
        final boolean active = Boolean.valueOf(value).booleanValue();
        form.setCheckbox(inputElementId, active);
    }

    protected final void clickLink(String xpath, String value) {

        XPathExpression xpathExpression = new XPathExpression(xpath);

        final String linkId = xpathExpression.getValue(LINK_ELEM_KEY, ID_ATTR_KEY);
        if (linkId != null) {
            clickLink(linkId);
        } else {
            final String text = xpathExpression.getValue(LINK_ELEM_KEY, CDATA_ATTR_KEY);
            clickLinkWithText(text);
        }
    }

    protected final void setInputParameter(String xpath, String value) throws Exception {

        XPathExpression xpathExpression = new XPathExpression(xpath);

        final WebForm form = getForm(xpathExpression);
        final String inputType = getElementType(xpathExpression);        

        String inputParameterName = getInputParameterName(xpathExpression);
        if (inputParameterName == null) {
            inputParameterName = getParameterName(xpathExpression);
        }

        boolean isHidden = HIDDEN_INPUT_TYPE.equalsIgnoreCase(inputType);
        if (isHidden) {
            setHiddenField(form, inputParameterName, value);
        } else {
            form.setParameter(inputParameterName, value);
        }
    }

    private String getParameterName(XPathExpression xPathExpression) throws Exception {

        final Document dom = getWebResponse().getDOM();
        final String elementId = getInputParameterId(xPathExpression);
        final Element element = dom.getElementById(elementId);

        return element.getAttribute("name");
    }

    private String getInputParameterName(XPathExpression xpathExpression) {
        String inputParameterId = null;

        for (int i = 0; i < INPUT_TYPES.length; i++) {
            String inputType = INPUT_TYPES[i];
            inputParameterId = getElementName(inputType, xpathExpression);
            if (inputParameterId != null) {
                break;
            }
        }

        return inputParameterId;
    }

    private String getInputParameterId(XPathExpression xpathExpression) {
        String inputParameterName = null;

        for (int i = 0; i < INPUT_TYPES.length; i++) {
            String inputType = INPUT_TYPES[i];
            inputParameterName = getElementId(inputType, xpathExpression);
            if (inputParameterName != null) {
                break;
            }
        }

        return inputParameterName;
    }

    private WebForm getForm(XPathExpression xpathExpression) {

        final String formId = getElementIdOrName(FORM_ELEM_KEY, xpathExpression);
        return getForm(formId);
    }

    private String getElementIdOrName(String elemKey, XPathExpression xpathExpression) {

        String inputElementId = getElementId(elemKey, xpathExpression);
        if (inputElementId == null) inputElementId = getElementName(elemKey, xpathExpression);

        return inputElementId;
    }

    private String getElementType(XPathExpression xpathExpression) {
        return xpathExpression.getValue(INPUT_ELEM_KEY, TYPE_ATTR_KEY);
    }

    private String getElementName(String elemKey, XPathExpression xpathExpression) {
        return xpathExpression.getValue(elemKey, NAME_ATTR_KEY);
    }

    private String getElementId(String elemKey, XPathExpression xpathExpression) {
        return xpathExpression.getValue(elemKey, ID_ATTR_KEY);
    }

    private class XPathExpression {

        public XPathExpression(String xpath) {

            String[] elements = xpath.split(DELIMITER);
            for (int i = 0; i < elements.length; i++) {
                String element = elements[i];

                if (element.indexOf(ATTR_DELIMITER) != -1) {
                    Map<String, String> selectedAttributes = new HashMap<String, String>();

                    final String elemKey = element.substring(0, element.indexOf("["));
                    selectedExpressions.put(elemKey, selectedAttributes);

                    final String[] attributes = element.split(ATTR_DELIMITER);
                    for (int j = 1; j < attributes.length; j++) {
                        String attribute = attributes[j];
                        final String key = attribute.substring(0, attribute.indexOf("="));
                        final String value = attribute.substring(attribute.indexOf("\"") + 1, attribute.lastIndexOf("\""));
                        selectedAttributes.put(key, value);
                    }
                }
            }
        }

        public String getValue(String elemKey, String attrKey) {

            final Map selectedAttrs = selectedExpressions.get(elemKey);
            if (selectedAttrs != null) {
                return (String) selectedAttrs.get(attrKey);
            }

            return null;
        }

        private Map<String, Map> selectedExpressions = new HashMap<String, Map>();

        private static final String DELIMITER = "/";
        private static final String ATTR_DELIMITER = "@";
    }

    private static final String FORM_ELEM_KEY = "FORM";
    private static final String INPUT_ELEM_KEY = "INPUT";
    private static final String TEXTAREA_ELEM_KEY = "TEXTAREA";
    private static final String SELECT_ELEM_KEY = "SELECT";
    private static final String LINK_ELEM_KEY = "A";

    private static final String ID_ATTR_KEY = "ID";
    private static final String NAME_ATTR_KEY = "NAME";
    private static final String CDATA_ATTR_KEY = "CDATA";

    private static final String TYPE_ATTR_KEY = "TYPE";

    private static final String[] INPUT_TYPES = new String[]{INPUT_ELEM_KEY, SELECT_ELEM_KEY, TEXTAREA_ELEM_KEY};

    private static final String SUBMIT_BUTTON_TYPE = "submit";
    private static final String HIDDEN_INPUT_TYPE = "hidden";
}
