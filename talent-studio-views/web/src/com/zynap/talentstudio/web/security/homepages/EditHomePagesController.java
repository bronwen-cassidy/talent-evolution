/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security.homepages;

import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.common.ParameterConstants;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Nov-2007 10:15:34
 */
public class EditHomePagesController extends AddHomePagesController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        List<Arena> arenas = new ArrayList<Arena>(arenaManager.getSortedArenas());

        Long groupId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.GROUP_ID);
        List<HomePage> homePages = groupService.findHomePages(groupId);

        HomePagesFormBean homePagesFormBean = new HomePagesFormBean();
        for(Arena arena : arenas) {
            final HomePage homePage = HomePagesMultiController.findHomePage(arena.getArenaId(), homePages);
            homePagesFormBean.add(new HomePageWrapperBean(homePage, arena));
        }
        
        // all home pages have the same group just take the first one
        homePagesFormBean.setGroup(homePages.get(0).getGroup());
        homePagesFormBean.setEditing(true);
        homePagesFormBean.setInternalUrls(internalUrls);
        return homePagesFormBean;
    }
}