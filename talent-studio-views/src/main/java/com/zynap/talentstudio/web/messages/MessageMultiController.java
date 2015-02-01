/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.messages;

import com.zynap.talentstudio.messages.IMessageService;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.common.ControllerConstants;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Sep-2007 13:04:05
 */
public class MessageMultiController extends ZynapMultiActionController {

    public ModelAndView deleteMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long messageItemId = RequestUtils.getLongParameter(request, "m_id", null);
        if (messageItemId != null) messageService.delete(messageItemId);
        final Map displayTagParams = ZynapWebUtils.getParametersStartingWith(request, ControllerConstants.DISPLAY_TAG_PREFIX);        
        String returnUrl = ZynapWebUtils.buildURL(MESSAGE_LIST_VIEW, displayTagParams);
        return new ModelAndView(new ZynapRedirectView(returnUrl));
    }

    public ModelAndView deleteMessages(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] messageItems = RequestUtils.getStringParameters(request, "m_ids");
        if(messageItems != null && messageItems.length > 0) {
            messageService.delete(messageItems);
        }
        final Map displayTagParams = ZynapWebUtils.getParametersStartingWith(request, ControllerConstants.DISPLAY_TAG_PREFIX);
        String returnUrl = ZynapWebUtils.buildURL(MESSAGE_LIST_VIEW, displayTagParams);
        return new ModelAndView(new ZynapRedirectView(returnUrl));
    }

    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    private IMessageService messageService;

    private static final String MESSAGE_LIST_VIEW = "worklistmessages.htm";
}
