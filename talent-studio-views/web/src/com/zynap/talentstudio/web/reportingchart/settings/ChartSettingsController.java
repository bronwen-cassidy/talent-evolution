package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.preferences.IPreferencesManager;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.web.arena.MenuItemHelper;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.SelectionNodeHelper;
import com.zynap.talentstudio.arenas.MenuSection;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 21-Nov-2005
 * Time: 17:56:54
 */
public abstract class ChartSettingsController extends DefaultWizardFormController {

    public ChartSettingsController() {
        setAllowDirtyBack(true);
    }

    protected final Object formBackingObject(HttpServletRequest request) throws Exception {

        Preference preference = getPreference(request);

        final Collection<MenuSection> menuSections = reportService.getMenuSections();
        final Collection<MenuSection> homeMenuSections = reportService.getHomePageReportMenuSections();

        SettingsWrapperFormBean formBean = new SettingsWrapperFormBean(preference, menuSections, homeMenuSections, menuItemURL);
        Collection<SelectionNode> selections = SelectionNodeHelper.createDomainObjectSelections(groupService.find(Group.TYPE_HOMEPAGE), preference.getGroups());
        formBean.setGroups(selections);
        return formBean;
    }

    protected abstract Preference getPreference(HttpServletRequest request) throws Exception;

    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        SettingsWrapperFormBean settingsWrapperFormBean = (SettingsWrapperFormBean) command;

        Map<String, Object> refData = new HashMap<String, Object>();
        String messageKey = "chart.wizard.page.";
        refData.put(ControllerConstants.TITLE, messageKey + page);

        switch (page) {
            case 1:

                settingsWrapperFormBean.setPositionExtendedAttributes(getDynamicAttributeService().getAllActiveAttributes(Node.POSITION_UNIT_TYPE_, false));
                settingsWrapperFormBean.setSubjectExtendedAttributes(getDynamicAttributeService().getAllActiveAttributes(Node.SUBJECT_UNIT_TYPE_, false));
                settingsWrapperFormBean.setPrimarySubjectAssociationLookupType(getLookupManager().findLookupType(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC));
                settingsWrapperFormBean.setSecondarySubjectAssociationLookupType(getLookupManager().findLookupType(ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC));

                break;
        }

        return refData;
    }


    /**
     * Clear checkboxes as required.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected final void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {

        SettingsWrapperFormBean bean = (SettingsWrapperFormBean) command;

        final int currentPage = getCurrentPage(request);
        //final int targetPage = getTargetPage(request, currentPage);

        if (currentPage == PUBLISH_IDX) {

            // clear menu items
            final String[] menuItems = request.getParameterValues(MenuItemHelper.ACTIVE_MENU_ITEMS_PARAM);
            final String[] homePageMenuItems = request.getParameterValues(MenuItemHelper.HOME_PAGE_MENU_ITEMS_PARAM);

            MenuItemHelper.clearMenuItems(bean, menuItems, homePageMenuItems);

            final String[] groupItems = request.getParameterValues("groupIds");
            if (groupItems == null || groupItems.length < 1) bean.setGroupIds(new Long[0]);

            // set the secure flag
            bean.setSecure(RequestUtils.getBooleanParameter(request, "secure", false));
        }

        if (currentPage == CONFIG_VIEW_IDX) {
            // all the boolean values need to be handled
            bean.setOuShow(RequestUtils.getBooleanParameter(request, "ouShow", false));
            bean.setPositionPrimaryAssociationShow(RequestUtils.getBooleanParameter(request, "positionPrimaryAssociationShow", false));
            bean.setPositionSecondaryAssociationShow(RequestUtils.getBooleanParameter(request, "positionSecondaryAssociationShow", false));
            bean.setSubjectPrimaryAssociationsShow(RequestUtils.getBooleanParameter(request, "subjectPrimaryAssociationsShow", false));
            bean.setSubjectSecondaryAssociationsShow(RequestUtils.getBooleanParameter(request, "subjectSecondaryAssociationsShow", false));
            if (RequestUtils.getStringParameters(request, "selectedPositionAttributes").length == 0)
                bean.setSelectedPositionAttributes(new String[0]);
            if (RequestUtils.getStringParameters(request, "selectedSubjectAttributes").length == 0) bean.setSelectedSubjectAttributes(new String[0]);
        }
    }

    /**
     * Ensure that view name cannot be blank.
     * <br/>
     * Only validate if not going back
     *
     * @param request
     * @param command
     * @param errors
     * @param page
     * @throws Exception
     */
    protected final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        SettingsWrapperFormBean bean = (SettingsWrapperFormBean) command;

        switch (page) {
            case PUBLISH_IDX:
                if (!StringUtils.hasText(bean.getViewName())) {
                    errors.rejectValue("viewName", "error.chart.viewname.required", "'Name' is a required field");
                }
                break;
        }
    }

    public final IPreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public final void setPreferencesManager(IPreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    public final IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public final void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public final ILookupManager getLookupManager() {
        return lookupManager;
    }

    public final void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public IReportService getReportService() {
        return reportService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setMenuItemURL(String menuItemURL) {
        this.menuItemURL = menuItemURL;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    private IReportService reportService;
    private IPreferencesManager preferencesManager;
    private IDynamicAttributeService dynamicAttributeService;
    private ILookupManager lookupManager;
    private IGroupService groupService;
    private String menuItemURL;

    private static final int PUBLISH_IDX = 0;
    private static final int CONFIG_VIEW_IDX = 1;
}
