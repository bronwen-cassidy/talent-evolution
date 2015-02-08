package com.zynap.talentstudio.web.common.html;

import org.springframework.beans.PropertyAccessor;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Class is used to bind spring form elements when using ajax into the form backing object
 */
public class FormFragmentTag extends AbstractHtmlElementTag {
    private static final String MODEL_ATTRIBUTE = "modelAttribute";
    private String modelAttribute;
    private String previousNestedPath;

    public void setModelAttribute(String modelAttribute) {
        this.modelAttribute = modelAttribute;
    }

    protected String getModelAttribute() {
        return this.modelAttribute;
    }

    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        this.previousNestedPath = (String) this.pageContext.getAttribute(NESTED_PATH_VARIABLE_NAME, PageContext.REQUEST_SCOPE);
        this.pageContext.setAttribute(NESTED_PATH_VARIABLE_NAME, modelAttribute + PropertyAccessor.NESTED_PROPERTY_SEPARATOR, PageContext.REQUEST_SCOPE);
        return EVAL_BODY_INCLUDE;
    }

    protected String resolveModelAttribute() throws JspException {
        Object resolvedModelAttribute = evaluate(MODEL_ATTRIBUTE, getModelAttribute());
        if (resolvedModelAttribute == null) {
            throw new IllegalArgumentException(MODEL_ATTRIBUTE + " must not be null");
        }
        return (String) resolvedModelAttribute;
    }

    @Override
    public void doFinally() {
        super.doFinally();
        if (this.previousNestedPath != null) {
            this.pageContext.setAttribute(NESTED_PATH_VARIABLE_NAME, this.previousNestedPath, PageContext.REQUEST_SCOPE);
        } else {
            this.pageContext.removeAttribute(NESTED_PATH_VARIABLE_NAME, PageContext.REQUEST_SCOPE);
        }
        this.previousNestedPath = null;
    }
}