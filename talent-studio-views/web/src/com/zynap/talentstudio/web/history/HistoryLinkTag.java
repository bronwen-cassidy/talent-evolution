package com.zynap.talentstudio.web.history;

import com.zynap.talentstudio.web.common.html.link.LinkTag;

import javax.servlet.http.HttpServletRequest;

/**
 * User: amark
 * Date: 20-Jul-2005
 * Time: 10:40:40
 *
 * Taglibrary that builds up history link.
 */
public class HistoryLinkTag extends LinkTag {

    /**
     * Build link.
     *
     * @return un-encoded URL with token appended as String.
     */
    protected String buildLink() {

        // do not encode
        final boolean uri = false;

        return HistoryHelper.addTokenToURL((HttpServletRequest) pageContext.getRequest(), buildUrl(uri), uri);
    }
}
