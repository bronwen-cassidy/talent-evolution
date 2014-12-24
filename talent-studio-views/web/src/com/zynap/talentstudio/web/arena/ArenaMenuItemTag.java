package com.zynap.talentstudio.web.arena;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * User: amark
 * Date: 27-May-2005
 * Time: 15:43:23
 *
 * Tag library that loads content to display for arena menu items from disk.
 */
public class ArenaMenuItemTag extends ArenaHomePageTag {

    private MenuItem menuItem;

    public void setMenuItem(Object menuItem) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(menuItem.toString())) {
            this.menuItem = (MenuItem) ExpressionEvaluationUtils.evaluate(menuItem.toString(), menuItem.toString(), Object.class, pageContext);
        } else {
            this.menuItem = (MenuItem) menuItem;
        }
    }

    public Object getMenuItem() {
        return menuItem;
    }

    protected int doStartTagInternal() {

        final UserSession userSession = ZynapWebUtils.getUserSession((HttpServletRequest) pageContext.getRequest());
        String key = menuItem.getKey();

        try {
            getContent(key, userSession);
        } catch (Exception ignored) {}

        return EVAL_PAGE;
    }

    public void release() {
        super.release();

        menuItem = null;
    }
}
