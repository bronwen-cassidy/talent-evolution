/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.web.tag;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.tree.PickerController;
import com.zynap.util.resource.PropertiesManager;

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
public class ResizableWindowTag extends ZynapTagSupport {

   protected int doInternalStartTag() throws Exception {

        String popupStartHtml = propertiesManager.getString("popup.tag.start");
        Object[] tokens = new Object[]{elementId, closeFunction};
        out.write(replaceTokens(popupStartHtml, tokens));

        if (StringUtils.hasText(src)) {
            src = ZynapWebUtils.buildURL(src, PickerController.POPUP_ID_PARAM, elementId);

            if (StringUtils.hasText(initialLeaf)) {
                src = ZynapWebUtils.buildURL(src, PickerController.POPUP_INITIAL_LEAF_PARAM, initialLeaf);
            }

            out.write(replaceTokens(propertiesManager.getString("popup.iframe.html"), new Object[]{elementId, src}));
        }

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {

        try {
            out.write(replaceTokens(propertiesManager.getString("popup.tag.end"), new Object[] {elementId}));
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

    public void setElementId(String elementId) throws JspException {
        this.elementId = ExpressionEvaluationUtils.evaluateString("elementId", elementId, pageContext);
    }

    public void release() {
        super.release();
        this.src = null;
        this.initialLeaf = null;
        this.closeFunction = "";
    }

    private static final PropertiesManager propertiesManager = PropertiesManager.getInstance(ResizableWindowTag.class);
    private String src;
    private String initialLeaf;
    private String closeFunction = "";
    private String elementId;
}
