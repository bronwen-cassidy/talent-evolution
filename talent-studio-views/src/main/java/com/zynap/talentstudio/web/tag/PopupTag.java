/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.tag;

import com.zynap.talentstudio.web.tag.properties.TagGeneralProperties;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.tree.PickerController;

import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;

import java.io.IOException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PopupTag extends ZynapTagSupport {

    protected int doInternalStartTag() throws Exception {

        properties = new TagGeneralProperties();
        out.write(replaceTokens(properties.getPopupDivStart(), new Object[]{id, closeFunction}));
        if (StringUtils.hasText(src)) {
            src = ZynapWebUtils.buildURL(src, PickerController.POPUP_ID_PARAM, id);
            if (StringUtils.hasText(initialLeaf))
                src = ZynapWebUtils.buildURL(src, PickerController.POPUP_INITIAL_LEAF_PARAM, initialLeaf);
            out.write(replaceTokens(properties.getIframeSrc(), new Object[]{id, src}));
        }

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {

        try {
            out.write(properties.getPopupDivEnd());
        } catch (IOException e) {
            throw new JspException("Error occurred writing end popup tag message: " + e.getMessage());
        }

        return EVAL_PAGE;
    }

    public void setSrc(String src) throws JspException {
        this.src = ExpressionEvaluationUtils.evaluateString("src", src, pageContext);
    }

    public void setInitialLeaf(String initialLeaf) throws JspException {
        this.initialLeaf = ExpressionEvaluationUtils.evaluateString("initialLeaf", initialLeaf, pageContext);
    }

    public void setCloseFunction(String closeFunction) throws JspException {
        this.closeFunction = ExpressionEvaluationUtils.evaluateString("closeFunction", closeFunction, pageContext);
    }

    public void release() {
        super.release();

        this.src = null;
        this.initialLeaf = null;
        this.closeFunction = "";
    }

    private TagGeneralProperties properties;
    private String src;
    private String initialLeaf;
    private String closeFunction = "";
}
