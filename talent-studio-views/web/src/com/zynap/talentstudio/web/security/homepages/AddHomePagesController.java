/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security.homepages;

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.beans.UploadedFilePropertyEditor;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Nov-2007 10:15:34
 */
public class AddHomePagesController extends DefaultWizardFormController {

    /**
     * Set up custom property editors.
     *
     * @throws Exception
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(UploadedFile.class, new UploadedFilePropertyEditor());
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        HomePagesFormBean homePagesFormBean = new HomePagesFormBean();
        List<HomePageWrapperBean> arenaHomePages = new ArrayList<HomePageWrapperBean>();
        // add in a new set of home pages
        Collection<Arena> arenas = arenaManager.getSortedArenas();
        for (Arena arena : arenas) {
            arenaHomePages.add(new HomePageWrapperBean(new HomePage(arena.getArenaId(), ""), arena));
        }
        homePagesFormBean.setHomePages(arenaHomePages);
        homePagesFormBean.setGroup(new Group(null, null, Group.TYPE_HOMEPAGE));
        homePagesFormBean.setInternalUrls(internalUrls);
        return homePagesFormBean;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        HomePagesFormBean homePagesFormBean = (HomePagesFormBean) command;

        if (isFinishRequest(request)) {
            getValidator().validate(command, errors);
        }

        if (getTargetPage(request, page) == HOME_PAGE_IDX && request.getParameter(DELETE_IMAGE_INDEX) != null) {
            int index = Integer.parseInt(request.getParameter(DELETE_IMAGE_INDEX));
            if (index > -1) {
                HomePageWrapperBean homePageWrapperBean = homePagesFormBean.getHomePages().get(index);
                homePageWrapperBean.deleteUpload();
            }
        }
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        HomePagesFormBean homePagesFormBean = (HomePagesFormBean) command;

        try {
            groupService.createOrUpdate(homePagesFormBean.getCreatedHomePages());
        } catch (DataAccessException e) {
            errors.rejectValue("groupLabel", "error.duplicate.group.label", "Duplicate Group Label Not allowed");
            if (!homePagesFormBean.isEditing()) {
                homePagesFormBean.clearState();
            }
            return showPage(request, errors, HOME_PAGE_IDX);
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.GROUP_ID, homePagesFormBean.getGroup().getId()));
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    public void setArenaManager(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public void setInternalUrls(List<String> internalUrls) {
        this.internalUrls = internalUrls;
    }

    protected IArenaManager arenaManager;
    protected IGroupService groupService;

    public static final String DELETE_IMAGE_INDEX = "deleteImageIndex";
    private static final int HOME_PAGE_IDX = 1;
    List<String> internalUrls;
}
