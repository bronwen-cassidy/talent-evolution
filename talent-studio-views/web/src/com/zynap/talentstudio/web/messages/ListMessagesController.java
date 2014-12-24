/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.messages;

import com.zynap.talentstudio.messages.IMessageService;
import com.zynap.talentstudio.messages.MessageItem;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.common.ControllerConstants;

import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 13-Sep-2007 13:55:59
 */
public class ListMessagesController extends SimpleFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long userId = ZynapWebUtils.getUserId(request);
        List<MessageItem> inboxItems = messageService.findAll(userId);
        InboxWrapper wrapper = new InboxWrapper();
        wrapper.setInboxItems(inboxItems);

        final Map displayTagParams = ZynapWebUtils.getParametersStartingWith(request, ControllerConstants.DISPLAY_TAG_PREFIX);
        wrapper.setDisplayTagParams(displayTagParams);

        if (!displayTagParams.isEmpty()) {
            Set entrySet = displayTagParams.entrySet();

            final Map.Entry pageNumber = (Map.Entry) entrySet.iterator().next();
            wrapper.setSortValues(pageNumber.getKey().toString(), pageNumber.getValue().toString());            
        }

        return wrapper;
    }

    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    private IMessageService messageService;
}
