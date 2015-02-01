/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.analysis.picker.PopulationCriteriaBuilder;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class ArtefactDisplaySettingsController extends DefaultWizardFormController {

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        DisplayConfigWrapper wrapper = (DisplayConfigWrapper) command;
        int targetPage = getTargetPage(request, page);
        switch (targetPage) {

            case DISPLAY_CONFIG_ITEMS_VIEW:
                Long displayConfigId = new Long(request.getParameter(DISPLAY_CONFIG_PARAM));
                wrapper.setSelectedDisplayConfig(displayConfigId);
                wrapper.setEditing(false);
                wrapper.setActiveTab(DISPLAY_ITEMS_TAB);
                break;

            case DISPLAY_CONFIG_ITEM_VIEW:
                Long displayItemId = new Long(request.getParameter(DISPLAY_CONFIG_ITEM_PARAM));
                wrapper.setDisplayConfigItemId(displayItemId);
                wrapper.setEditing(false);
                wrapper.setActiveTab(DISPLAY_ITEM_TAB);
                reportReferenceData(wrapper);
                // build the attribute path
                getBuilder(wrapper.getViewType(), wrapper.getReportType()).setAttributeLabels(wrapper.getReportType(), wrapper.getAttributeWrapperBeans(), wrapper.getViewType());
                break;

            case EDIT_CONFIG_ITEM_FORM:
                if (wrapper.isReportConfigItem()) {
                    reportReferenceData(wrapper);
                    wrapper.removeInvalidAttributes();                    
                }
                wrapper.setEditing(true);
                wrapper.setActiveTab(DISPLAY_ITEM_TAB);
                break;

            case ADD_COLUMN:
                wrapper.addColumn(new Column(), RequestUtils.getIntParameter(request, REPORT_INDEX, -1));
                break;

            case DELETE_COLUMN:
                int index = RequestUtils.getIntParameter(request, DELETE_PREFIX, -1);
                wrapper.removeColumn(index, RequestUtils.getIntParameter(request, REPORT_INDEX, -1));
                break;

            case SUBMIT_CONFIG_ITEM:
                wrapper.setEditing(errors.hasErrors());
                wrapper.setActiveTab(DISPLAY_ITEM_TAB);
                if(!wrapper.isEditing()) getBuilder(wrapper.getViewType(), wrapper.getReportType()).setAttributeLabels(wrapper.getReportType(), wrapper.getAttributeWrapperBeans(), null);
                break;

            case CANCEL_EDIT_CONFIG_ITEM:
                wrapper.setEditing(false);
                wrapper.setActiveTab(DISPLAY_ITEM_TAB);
                reportReferenceData(wrapper);
                // build the attribute path
                getBuilder(wrapper.getViewType(), wrapper.getReportType()).setAttributeLabels(wrapper.getReportType(), wrapper.getAttributeWrapperBeans(), null);
                break;
        }
        return null;
    }
    
    private PopulationCriteriaBuilder getBuilder(String viewType, String reportType) {
        if(DisplayConfigItem.EXEC_VIEW_TYPE.equals(viewType) && reportType.equals(Node.SUBJECT_UNIT_TYPE_)) {
            return execSummaryBuilder;
        }
        return builder;
    }

    private void reportReferenceData(DisplayConfigWrapper wrapper) throws TalentStudioException {

        final String reportType = wrapper.getReportType();
        final String viewType = wrapper.getViewType();

        if (!isPickerNeededView(viewType)) {
            return;
        }

        if(DisplayConfigItem.EXEC_VIEW_TYPE.equals(viewType) && reportType.equals(Node.SUBJECT_UNIT_TYPE_)) {
            // this execSummaryBuilder.buildCollection() does not use the builder.buildCollection(reportType, viewType) as it uses both
            // subject and position attributes, comes from a different file that only has executive summary attributes, we need
            // both subject attribute sets and position attribute sets. see: execartefactviewtree.xml
            wrapper.setAttributeCollection(execSummaryBuilder.buildExecSummaryCollection());

        } else  {
            wrapper.setAttributeCollection(builder.buildCollection(reportType, viewType));
        }
        wrapper.checkColumnAttributes();
    }

    private boolean isPickerNeededView(String viewType) {
        return (DisplayConfigItem.ASSOCIATION_DISPLAY_VIEW_TYPE.equals(viewType) || DisplayConfigItem.ATTRIBUTE_VIEW_TYPE.equals(viewType)
                || DisplayConfigItem.ADD_VIEW_TYPE.equals(viewType) || DisplayConfigItem.EXEC_VIEW_TYPE.equals(viewType));
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        DisplayConfigWrapper wrapper = (DisplayConfigWrapper) command;
        DisplaySettingsValidator displaySettingsValidator = (DisplaySettingsValidator) getValidator();
        switch (getTargetPage(request, page)) {

            case SUBMIT_CONFIG_ITEM:
                wrapper.setGroupIds(RequestUtils.getAllLongParameters(request, "groupIds"));
                DisplayConfigItem item = wrapper.getModifedReportConfigItem();
                if (item.isHideable()) item.setActive(RequestUtils.getBooleanParameter(request, "displayConfigItem.active", false));

                displaySettingsValidator.validate(wrapper, errors);
                if (!errors.hasErrors()) {
                    displayConfigService.update(item);
                }
                break;
            case CANCEL_EDIT_CONFIG_ITEM:
                wrapper.reset(displayConfigService.findById(wrapper.getDisplayConfig().getId()));
                break;
        }
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        List<DisplayConfig> displayConfigurations = displayConfigService.findAll();
        List<Role> roles = roleManager.getActiveAccessRoles();
        List<Group> groups = groupService.find(Group.TYPE_HOMEPAGE);
        return new DisplayConfigWrapper(displayConfigurations, roles, groups);
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public final void setBuilder(PopulationCriteriaBuilder builder) {
        this.builder = builder;
    }    

    public final void setExecSummaryBuilder(PopulationCriteriaBuilder execSummaryBuilder) {
        this.execSummaryBuilder = execSummaryBuilder;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    private IDisplayConfigService displayConfigService;
    private IRoleManager roleManager;
    private IGroupService groupService;

    private PopulationCriteriaBuilder builder;

    protected static final String DISPLAY_CONFIG_PARAM = "dc";
    protected static final String DISPLAY_CONFIG_ITEM_PARAM = "ic";

    private static final String DELETE_PREFIX = "_delete";
    private static final String REPORT_INDEX = "repIndex";

    private static final int DISPLAY_CONFIG_ITEMS_VIEW = 1;
    private static final int DISPLAY_CONFIG_ITEM_VIEW = 2;

    private static final int EDIT_CONFIG_ITEM_FORM = 3;
    private static final int ADD_COLUMN = 4;
    private static final int DELETE_COLUMN = 5;

    private static final int SUBMIT_CONFIG_ITEM = 6;
    private static final int CANCEL_EDIT_CONFIG_ITEM = 7;

    public static final String DISPLAY_ITEMS_TAB = "displayitems";
    private static final String DISPLAY_ITEM_TAB = "displayitem";

    private PopulationCriteriaBuilder execSummaryBuilder;
}
