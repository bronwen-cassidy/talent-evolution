package com.zynap.talentstudio.web.common.html;

/**
 * User: amark
 * Date: 20-Jul-2005
 * Time: 13:50:22
 */

import com.zynap.talentstudio.web.utils.ZynapMockTagLibTest;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class TestBrowseArtefactLinkTag extends ZynapMockTagLibTest {

    public void testDoInternalStartTag() throws Exception {

        final String url = "url";
        final String var = "var";
        final String tabName = "activeTab";
        final String activeTab = "details";

        // add history token
        final String token = "token1";
        mockRequest.setAttribute(HistoryHelper.TOKEN_KEY, token);

        browseArtefactLinkTag.setUrl(url);
        browseArtefactLinkTag.setVar(var);
        browseArtefactLinkTag.setTabName(tabName);
        browseArtefactLinkTag.setActiveTab(activeTab);

        assertEquals(Tag.EVAL_PAGE, browseArtefactLinkTag.doEndTag());

        // check expected parameters are all in the request
        final String newURL = (String) mockPageContext.getAttribute(var, PageContext.REQUEST_SCOPE);
        assertTextPresent(newURL, ParameterConstants.DISABLE_COMMAND_DELETION);
        assertTextPresent(newURL, ParameterConstants.SAVE_COMMAND);
        assertTextPresent(newURL, ParameterConstants.PREFIX_COMMAND_PARAMETER + tabName);
        assertTextPresent(newURL, activeTab);
        assertTextPresent(newURL, HistoryHelper.TOKEN_KEY);
        assertTextPresent(newURL, token);
    }

    public void testRelease() throws Exception {

        browseArtefactLinkTag.addParameter("key", "value");

        browseArtefactLinkTag.setUrl("url");
        browseArtefactLinkTag.setVar("var");
        browseArtefactLinkTag.setTabName("tabName");
        browseArtefactLinkTag.setActiveTab("activeTab");

        browseArtefactLinkTag.release();

        assertNull(browseArtefactLinkTag.getUrl());
        assertNull(browseArtefactLinkTag.getVar());
        assertNull(browseArtefactLinkTag.getActiveTab());
        assertNull(browseArtefactLinkTag.getTabName());

        assertTrue(browseArtefactLinkTag.getParameters().isEmpty());
    }

    protected TagSupport getTabLibrary() {
        browseArtefactLinkTag = new BrowseArtefactLinkTag();

        return browseArtefactLinkTag;
    }

    BrowseArtefactLinkTag browseArtefactLinkTag;
}