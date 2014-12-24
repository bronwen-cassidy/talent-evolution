package com.zynap.talentstudio.web.history;

/**
 * User: amark
 * Date: 20-Jul-2005
 * Time: 12:38:12
 */

import com.zynap.talentstudio.web.utils.ZynapMockTagLibTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class TestHistoryLinkTag extends ZynapMockTagLibTest {

    public void testDoInternalStartTagNoHistoryToken() throws Exception {

        final String url = "url?a=b";
        final String var = "var";

        historyLinkTag.setUrl(url);
        historyLinkTag.setVar(var);

        final int output = historyLinkTag.doEndTag();
        assertEquals(Tag.EVAL_PAGE, output);

        // there was no history token but the url will have had its query string removed
        final Object attribute = mockPageContext.getAttribute(var, PageContext.REQUEST_SCOPE);
        assertEquals(ZynapWebUtils.removeQueryString(url), attribute);
    }

    public void testDoInternalStartTag() throws Exception {

        final String url = "viewposition.htm?command.node.id=1&test1=true";
        final String var = "test";

        historyLinkTag.setUrl(url);
        historyLinkTag.setVar(var);

        final String token = "token1";
        mockRequest.setAttribute(HistoryHelper.TOKEN_KEY, token);

        final int output = historyLinkTag.doEndTag();
        assertEquals(Tag.EVAL_PAGE, output);

        // since we added a history token request attribute this should be in the URL but the querystring should have been removed
        final Object attribute = mockPageContext.getAttribute(var, PageContext.REQUEST_SCOPE);
        final String expectedURL = ZynapWebUtils.buildURL(ZynapWebUtils.removeQueryString(url), HistoryHelper.TOKEN_KEY, token);
        assertEquals(expectedURL, attribute);
    }

    protected TagSupport getTabLibrary() {
        historyLinkTag = new HistoryLinkTag();

        return historyLinkTag;
    }

    HistoryLinkTag historyLinkTag;
}