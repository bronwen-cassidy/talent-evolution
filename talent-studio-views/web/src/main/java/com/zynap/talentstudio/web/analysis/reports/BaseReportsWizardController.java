/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.analysis.picker.PopulationCriteriaBuilder;
import com.zynap.talentstudio.web.arena.MenuItemHelper;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.SelectionNodeHelper;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BaseReportsWizardController extends DefaultWizardFormController {

    protected abstract void setSuitablePopulations(ReportWrapperBean bean, Long userId) throws TalentStudioException;

    protected abstract Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception;

    protected abstract void validateColumns(ReportValidator validator, ReportWrapperBean bean, Errors errors) throws TalentStudioException;

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        ReportWrapperBean wrapperBean = (ReportWrapperBean) command;
        final int targetPage = getTargetPage(request, page);

        wrapperBean.setTarget(targetPage);

        switch (page) {
            case CORE_DETAILS_IDX:
                clearMenuItemsIfRequired(request, wrapperBean);
                wrapperBean.setGroupIds(RequestUtils.getAllLongParameters(request, "groupIds"));
                wrapperBean.setLastLineItem(RequestUtils.getBooleanParameter(request, "lastLineItem", false));
                wrapperBean.setPersonal(RequestUtils.getBooleanParameter(request, "personal", false));
                break;

        }
    }

    /**
     * Check if current user is owner of report.
     * <br/> Default implementation does nothing for add - edit controllers override this.
     *
     * @param reportWrapperBean
     * @param userId
     */
    protected void checkReportOwner(ReportWrapperBean reportWrapperBean, Long userId) {
        boolean scopeEditable = reportWrapperBean.getReport().getUserId() == null;
        scopeEditable = scopeEditable || (User.ROOT_USER_ID.equals(userId) || userId.equals(reportWrapperBean.getReport().getUserId()));
        reportWrapperBean.setScopeEditable(scopeEditable);
    }

    /**
     * Add fields to view when request cancelled.
     * <br/> Default implementation does nothing (for add) - edit controllers must override this method if they want to go back to the view report page.
     *
     * @param view
     * @param command
     */
    protected void addStaticFields(RedirectView view, Object command) {
    }

    protected final void setColumnAttributes(ReportWrapperBean reportWrapperBean) throws TalentStudioException {
        final AnalysisAttributeCollection analysisAttributeCollection = reportWrapperBean.getAttributeCollection();
        if (analysisAttributeCollection == null) {
            reportWrapperBean.setAttributeCollection(builder.buildCollection());
        }
    }

    /**
     * Clear menu items if none selected.
     *
     * @param request
     * @param wrapperBean
     */
    protected final void clearMenuItemsIfRequired(HttpServletRequest request, ReportWrapperBean wrapperBean) {

        final String[] menuItems = request.getParameterValues(MenuItemHelper.ACTIVE_MENU_ITEMS_PARAM);
        final String[] homePageMenuItems = request.getParameterValues(MenuItemHelper.HOME_PAGE_MENU_ITEMS_PARAM);
        MenuItemHelper.clearMenuItems(wrapperBean, menuItems, homePageMenuItems);
    }

    protected void validatePage(Object command, Errors errors, int page, boolean finish) {

        final ReportWrapperBean reportWrapperBean = (ReportWrapperBean) command;

        ReportValidator validator = (ReportValidator) getValidator();
        switch (page) {
            case CORE_DETAILS_IDX:
                if (reportWrapperBean.getTarget() != CORE_DETAILS_IDX)
                    validator.validateCoreValues(command, errors);
                break;
            case ADD_DELETE_COLUMNS_IDX:
                if (finish) {
                    try {
                        validateColumns(validator, reportWrapperBean, errors);
                    } catch (TalentStudioException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    protected final ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        RedirectView view = new ZynapRedirectView(getCancelView());
        addStaticFields(view, command);
        return new ModelAndView(view);
    }

    protected final void addColumn(HttpServletRequest request, ReportWrapperBean bean, Column column) {
        int index = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);
        if (index != -1) {
            bean.addColumn(column);
        }
    }

    protected final void removeColumn(HttpServletRequest request, ReportWrapperBean bean) {
        int index = RequestUtils.getIntParameter(request, SELECTED_COLUMN_INDEX, -1);
        if (index != -1) {
            bean.removeColumn(index);
        }
    }

    protected final void addDataForFormula(Map<String, Object> refData) {
        ArrayList<String> operators = new ArrayList<String>();
        for (int i = 0; i < IPopulationEngine.operators.length; i++) {
            operators.add(new String("" + IPopulationEngine.operators[i]));
        }
        refData.put("operators", operators);

    }

    protected final void assignFormulaInformation(int selectedColumnIndex, ReportWrapperBean reportWrapperBean) {
        ColumnWrapperBean column = reportWrapperBean.getColumnWrapper(selectedColumnIndex);
        reportWrapperBean.setSelectedFunction(column.getFunctionWrapperBean());
        reportWrapperBean.setCurrentColumnIndex(new Integer(selectedColumnIndex));
    }

    protected final void saveFormula(ReportWrapperBean reportWrapperBean) {

        if (reportWrapperBean.getCurrentColumnIndex() == null) {
            Column column = new Column();
            column.setFormula(true);
            column.setCalculation(reportWrapperBean.getSelectedFunction().getModifiedCalculation());
            reportWrapperBean.addColumn(column);

        } else {
            reportWrapperBean.updateColumnFormula();
        }
        reportWrapperBean.checkColumnAttributes();
    }

    /**
     * Get populations compatible with report.
     *
     * @param userId
     * @param defaultPopulation If null will not supply type
     * @param access
     * @return Collection
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    protected final Collection<PopulationDto> getPopulations(Long userId, Population defaultPopulation, String access) throws TalentStudioException {
        String artefactType = defaultPopulation == null ? null : defaultPopulation.getArtefactType();
        return populationService.findAll(artefactType, userId, access);
    }

    protected final Collection<Group> getUserGroups() throws TalentStudioException {
        return groupService.find(Group.TYPE_HOMEPAGE);
    }

    protected Collection<SelectionNode> wrapGroups(Collection<Group> groups, Report report) {
        return SelectionNodeHelper.createDomainObjectSelections(groups, report.getGroups());
    }

    public final void setBuilder(PopulationCriteriaBuilder builder) {
        this.builder = builder;
    }

    public final void setPopulationService(IAnalysisService populationService) {
        this.populationService = populationService;
    }

    public final IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public final void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public final IReportService getReportService() {
        return reportService;
    }

    public final void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public final String getRunReportURL() {
        return runReportURL;
    }

    public final void setRunReportURL(String runReportURL) {
        this.runReportURL = runReportURL;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    private String runReportURL;
    protected IDynamicAttributeService dynamicAttributeService;
    private IAnalysisService populationService;
    protected IReportService reportService;
    private IGroupService groupService;

    protected PopulationCriteriaBuilder builder;

    protected static final int CORE_DETAILS_IDX = 0;
    protected static final int ADD_DELETE_COLUMNS_IDX = 1;
    protected static final int ASSIGN_COLOURS_IDX = 2;
    protected static final int SAVE_COLOURS_IDX = 3;
    protected static final int REMOVE_COLUMNS_IDX = 4;

    public static final String ADD_PREFIX = "_add";
    public static final String DELETE_PREFIX = "_delete";

    public static final String SELECTED_COLUMN_INDEX = "selectedColumnIndex";
    public static final String SHOW_POPUP = "showPopup";
    public static final String COL_ID_PREFIX = "prefix";
}
