package com.zynap.talentstudio.web.common.html;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.html.link.LinkTag;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 20-Jul-2005
 * Time: 12:59:09
 *
 * Taglibrary that builds links used by browse / search node pages.
 * <br> Adds the parameters required to stop deletion of the controller form backing object.
 */
public class BrowseArtefactLinkTag extends LinkTag {

    private String tabName;

    private String activeTab;

    private String commandAction;

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) throws JspException {
        this.activeTab = ExpressionEvaluationUtils.evaluateString("activeTab", activeTab, pageContext);
    }

    public void setCommandAction(String commandAction) throws JspException {
        this.commandAction = ExpressionEvaluationUtils.evaluateString("commandAction", commandAction, pageContext);
    }

    /**
     * Build link.
     * 
     * @return un-encoded url with paarmeters required to tell HistoryInterceptor to save form backing object (String.)
     */
    protected String buildLink() {

        // not using URI encoding
        final boolean uri = false;

        // append history token
        String newURL = HistoryHelper.addTokenToURL((HttpServletRequest) pageContext.getRequest(), buildUrl(uri), uri);

        // determine command action - defaults to ParameterConstants.SAVE_COMMAND
        final String action = StringUtils.hasText(commandAction) ? commandAction : ParameterConstants.SAVE_COMMAND;

        // build map with parameters
        Map<String, Object> parameters = new HashMap<String, Object>(getParameters());
        parameters.put(ParameterConstants.DISABLE_COMMAND_DELETION, action);
        parameters.put(ParameterConstants.PREFIX_COMMAND_PARAMETER + tabName, activeTab);

        // build url with these parameters
        return ZynapWebUtils.buildURL(newURL, parameters, uri);
    }

    public void release() {
        super.release();

        activeTab = null;
        tabName = null;
        commandAction = null;
    }
}
