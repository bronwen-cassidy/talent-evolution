/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security.homepages;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Nov-2007 10:15:34
 */
public class HomePagesMultiController extends ZynapMultiActionController {

    public ModelAndView viewHomePages(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long groupId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.GROUP_ID);
        Group group = groupService.findById(groupId);

        final Collection<Arena> arenas = arenaManager.getArenas();

        final List<HomePage> homePages = new ArrayList<HomePage>(group.getHomePages());
        List<HomePageWrapperBean> homePageWrappers = new ArrayList<HomePageWrapperBean>();

        for(Arena arena : arenas) {
            final HomePage homePage = findHomePage(arena.getArenaId(), homePages);
            homePageWrappers.add(new HomePageWrapperBean(homePage, arena));
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(GROUP, group);
        model.put(HOME_PAGES, homePageWrappers);
        return new ModelAndView(VIEW_HOMEPAGES_URL, model);
    }

    public ModelAndView listHomePages(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // get all the groups for the home pages
        List<Group> groups = groupService.find(Group.TYPE_HOMEPAGE);        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(GROUPS, groups);
        return new ModelAndView(LIST_HOMEPAGES_URL, model);
    }

    public ModelAndView deleteHomePages(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long groupId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.GROUP_ID);
        groupService.delete(groupId);
        return new ModelAndView(new ZynapRedirectView(LIST_HOMEPAGES_REDIRECT_URL));
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    public void setArenaManager(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public static Arena getArena(String arenaId, Collection<Arena> arenas) throws TalentStudioException {
        for(Arena arena : arenas) {
            if(arena.getArenaId().equals(arenaId)) {
                return arena;
            }
        }
        return null;
    }

    public static HomePage findHomePage(String arenaId, List<HomePage> homePages) {
        for(HomePage homePage : homePages) {
            if(arenaId.equals(homePage.getArenaId())) {
                return homePage;
            }
        }
        return new HomePage(arenaId, null);
    }

    private IGroupService groupService;
    private IArenaManager arenaManager;
    private static final String GROUPS = "groups";
    private static final String GROUP = "group";
    private static final String HOME_PAGES = "homePages";
    private static final String VIEW_HOMEPAGES_URL = "viewhomepages";
    private static final String LIST_HOMEPAGES_URL = "listhomepages";
    private static final String LIST_HOMEPAGES_REDIRECT_URL = "listhomepages.htm";
}