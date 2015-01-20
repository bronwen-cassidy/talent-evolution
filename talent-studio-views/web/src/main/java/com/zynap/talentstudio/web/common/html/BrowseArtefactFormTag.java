package com.zynap.talentstudio.web.common.html;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.util.format.MessageTemplateFormatter;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import java.util.HashMap;
import java.util.Map;

/**
* Class or Interface description.
*
* @author jsuiras
* @since 19-May-2005 11:42:06
* @version 0.1
*/
public class BrowseArtefactFormTag extends FormTag {

    private String tabName;

    private String buttonMessage;
    private String buttonId = null;

    private String artefactId;

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public void setButtonMessage(String buttonMessage) throws JspException {
        this.buttonMessage = ExpressionEvaluationUtils.evaluateString("buttonMessage", buttonMessage, pageContext);
    }

    public void setButtonId(String buttonId) throws JspException {
        this.buttonId = ExpressionEvaluationUtils.evaluateString("buttonId", buttonId, pageContext);
    }

    public void setArtefactId(String artefactId) throws JspException {
        this.artefactId = ExpressionEvaluationUtils.evaluateString("artefactId", artefactId, pageContext);
    }

    /**
     * Writes out start of HTML form using the parameters supplied.
     *
     * @return EVAL_BODY_INCLUDE
     * @throws javax.servlet.jsp.JspException
     */
    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        try {
            startForm(request, out);

            MessageTemplateFormatter formatter = new MessageTemplateFormatter(propertiesManager.getString("artefact.inputs"));
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("name", name);
            parameters.put("artefactIdParam", ParameterConstants.NODE_ID_PARAM);
            parameters.put("artefactId", artefactId);
            parameters.put("disableDeleteParam", ParameterConstants.DISABLE_COMMAND_DELETION);
            parameters.put("disableDeleteSaveValue", ParameterConstants.SAVE_COMMAND);
            parameters.put("tabName", tabName);
            parameters.put("paramPrefix", ParameterConstants.PREFIX_COMMAND_PARAMETER);
            parameters.put("buttonMessage", buttonMessage);

            StringBuffer buttonAttributes = new StringBuffer();
            addAttribute(buttonAttributes, "id", buttonId);
            parameters.put("buttonAttributes", buttonAttributes.toString());

            out.println(formatter.format(parameters));

        } catch (Throwable e) {
            throw new JspException(e);
        }


        return EVAL_BODY_INCLUDE;
    }

    /**
     * Reset all attributes to default values.
     */
    public void release() {
        super.release();

        this.tabName = null;
        this.buttonMessage = null;
        this.buttonId = null;
        this.artefactId = null;
    }
}
