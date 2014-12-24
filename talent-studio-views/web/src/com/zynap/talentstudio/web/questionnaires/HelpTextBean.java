/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.help.HelpTextItem;
import com.zynap.talentstudio.help.IHelpTextService;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 07-Feb-2007 12:06:00
 */
public class HelpTextBean {

    public HelpTextItem getHelpTextItem(String helpTextId) {
        
        HelpTextItem helpTextItem = null;
        try {
            helpTextItem = helpTextService.findByID(new Long(helpTextId));        
        } catch (TalentStudioException e) {
            e.printStackTrace();
        }
        return helpTextItem;        
    }

    public void setHelpText(String helpTextId, String information) {

        HelpTextItem helpTextItem = getHelpTextItem(helpTextId);
        if (StringUtils.hasText(information)) {
            if (helpTextItem == null) {
                helpTextItem = new HelpTextItem();
                helpTextItem.setId(new Long(helpTextId));
            }
            helpTextItem.setBlobValue(information.getBytes());
            try {
                helpTextService.update(helpTextItem);
            } catch (TalentStudioException e) {
                e.printStackTrace();
            }
        } else {
            // delete from db
            try {
                if(helpTextItem != null) helpTextService.delete(helpTextItem);
            } catch (TalentStudioException e) {
                e.printStackTrace();
            }
        }
    }

    public void setHelpTextService(IHelpTextService helpTextService) {
        this.helpTextService = helpTextService;
    }

    private IHelpTextService helpTextService;
}
