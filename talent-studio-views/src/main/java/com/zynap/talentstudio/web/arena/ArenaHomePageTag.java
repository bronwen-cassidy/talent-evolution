package com.zynap.talentstudio.web.arena;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: amark
 * Date: 26-May-2005
 * Time: 18:11:44
 *
 * Tag library that loads content to display on arena home pages from disk.
 */
public class ArenaHomePageTag extends RequestContextAwareTag {

    protected int doStartTagInternal() throws JspException {

        // check if arena id has been passed in - if so use it rather than the current arena id 
        final UserSession userSession = ZynapWebUtils.getUserSession((HttpServletRequest) pageContext.getRequest());
        final String currentArenaId = StringUtils.hasText(arenaId) ? arenaId : userSession.getCurrentArenaId();

        try {
            getContent(currentArenaId, userSession);
        } catch (Exception ignored) {
        }

        return EVAL_PAGE;
    }

    protected void getContent(final String key, final UserSession userSession) throws JspException {

        final WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();

        MessageSource messageSource = (MessageSource) webApplicationContext.getBean("arenaTextMessageSource");
        String fileName = messageSource.getMessage(key, null, userSession.getLocale());

        if (StringUtils.hasText(fileName)) {
            BufferedReader reader = null;
            try {
                final Resource resource = webApplicationContext.getResource(fileName);
                reader = new BufferedReader(new FileReader(resource.getFile()));

                // write to output stream
                int c;
                while ((c = reader.read()) != -1) {
                    pageContext.getOut().write(c);
                }
            } catch (Exception e) {
                throw new JspException(e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new JspException(e);
                    }
                }
            }
        }
    }

    public void setArenaId(String arenaId) throws JspException {
        this.arenaId = arenaId;
    }

    private String arenaId;
}
