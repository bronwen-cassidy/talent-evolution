/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security.homepages;

import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Nov-2007 10:15:34
 */
@Controller
public class HomePagesMultiController extends ZynapMultiActionController {

	@Autowired
	public HomePagesMultiController(IGroupService groupService, IArenaManager arenaManager) {
		this.groupService = groupService;
		this.arenaManager = arenaManager;
	}

	@RequestMapping("/admin/viewhomepages.htm")
    public String viewHomePages(@RequestParam(ParameterConstants.GROUP_ID) Long groupId, Model model) throws Exception {
		
        Group group = groupService.findById(groupId);
        final Collection<Arena> arenas = arenaManager.getArenas();

        final List<HomePage> homePages = new ArrayList<>(group.getHomePages());
        List<HomePageWrapperBean> homePageWrappers = new ArrayList<>();

        for(Arena arena : arenas) {
            final HomePage homePage = findHomePage(arena.getArenaId(), homePages);
            homePageWrappers.add(new HomePageWrapperBean(homePage, arena));
        }
		
        model.addAttribute(GROUP, group);
        model.addAttribute(HOME_PAGES, homePageWrappers);
        
        return VIEW_HOMEPAGES_URL;
    }

	@RequestMapping("/admin/listhomepages.htm")
    public String listHomePages(Model model) throws Exception {
        // get all the groups for the home pages
        List<Group> groups = groupService.find(Group.TYPE_HOMEPAGE);
        model.addAttribute(GROUPS, groups);
        return LIST_HOMEPAGES_URL;
    }

	@RequestMapping("/admin/deletehomepages.htm")
    public ModelAndView deleteHomePages(@RequestParam(ParameterConstants.GROUP_ID) Long groupId) throws Exception {
        groupService.delete(groupId);
        return new ModelAndView(new ZynapRedirectView(LIST_HOMEPAGES_REDIRECT_URL));
    }

	static HomePage findHomePage(String arenaId, List<HomePage> homePages) {
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