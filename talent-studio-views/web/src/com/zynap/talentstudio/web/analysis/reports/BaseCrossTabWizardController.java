/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.domain.ZynapDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BaseCrossTabWizardController extends BaseReportsWizardController {

    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();

        CrossTabReportWrapperBean bean = (CrossTabReportWrapperBean) command;
        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final Long userId = userSession.getId();

        // initialise metrics
        if (page != ADD_COLUMNS_INDX && bean.getType() != null) {
            initMetrics(bean, userId);
        }

        String pageTitle = "ctreports.wizard.page.";
        switch (page) {
            case CORE_DETAILS_IDX:
                setSuitablePopulations(bean, userId);
                checkReportOwner(bean, userId);
                initDrilldownReports(bean, userId);

                pageTitle += "0";
                break;
            case CHOOSE_METRIC_IDX:
                setColumnAttributes(bean);
                initDrilldownReports(bean, userId);

                pageTitle += "0";
                break;
            case ADD_COLUMNS_INDX:
                bean.checkColumnAttributes();
                pageTitle += "1";
                break;
            case CELL_LABELS_INDX:
                bean.checkColumnAttributes();
                pageTitle += "3";
                break;
        }

        refData.put(ControllerConstants.TITLE, pageTitle);
        return refData;
    }

    /**
     * Assigns the crossTabCellInfos to the wrapper. In the case of an edit the full grid will be in the database always.
     * Note the vertical and horizontal column attributes may have changed and we will need to cater for this
     *
     * @param crossTabReportWrapperBean
     */
    protected void assignCellLabels(CrossTabReportWrapperBean crossTabReportWrapperBean) throws MaxNumCellsExceededException {

        List<CrossTabCellInfo> infoList = getExistingCrossTabCellInfos(crossTabReportWrapperBean);
        List<List<CrossTabCellInfo>> cellInfoRows = new ArrayList<List<CrossTabCellInfo>>();
        List<String> hHeadings = new ArrayList<String>();
        List<String> vHeadings = new ArrayList<String>();

        final ColumnWrapperBean horizontalColumn = crossTabReportWrapperBean.getHorizontalColumn();
        final ColumnWrapperBean verticalColumn = crossTabReportWrapperBean.getVerticalColumn();

        final DynamicAttribute horizontalAttribute = horizontalColumn.getAttributeDefinition().getAttributeDefinition();
        final DynamicAttribute verticalAttribute = verticalColumn.getAttributeDefinition().getAttributeDefinition();
        // vertical for the rows
        Collection verticalValues = verticalAttribute.getActiveLookupValues();
        Collection horizontalValues = horizontalAttribute.getActiveLookupValues();

        if(horizontalAttribute.isOrganisationUnitType()) {
            horizontalValues = organisationManager.findAllSecure();
        }
        if(verticalAttribute.isOrganisationUnitType()) {
            verticalValues = organisationManager.findAllSecure();
        }

        if(verticalValues.size() * horizontalValues.size() >= maxCellsNumber) {
            throw new MaxNumCellsExceededException("Too many values in the cross tab", "crosstab.excesive.values");
        }

        for (Iterator iterator = verticalValues.iterator(); iterator.hasNext();) {
            ZynapDomainObject  vValue = (ZynapDomainObject) iterator.next();
            List<CrossTabCellInfo> cellInfos = new ArrayList<CrossTabCellInfo>();
            vHeadings.add(vValue.getLabel());

            // horizontal values for the cells <td>
            for (Iterator valueIterator = horizontalValues.iterator(); valueIterator.hasNext();) {
                ZynapDomainObject hValue = (ZynapDomainObject) valueIterator.next();
                CrossTabCellInfo result = findCellInfo(infoList, hValue.getId(), vValue.getId());
                cellInfos.add(result);
                if(!hHeadings.contains(hValue.getLabel())) hHeadings.add(hValue.getLabel());
            }
            cellInfoRows.add(cellInfos);
        }
        
        crossTabReportWrapperBean.setCellInfos(cellInfoRows);
        crossTabReportWrapperBean.setHorizontalHeadings(hHeadings);
        crossTabReportWrapperBean.setVerticalHeadings(vHeadings);
    }

    protected abstract List<CrossTabCellInfo> getExistingCrossTabCellInfos(CrossTabReportWrapperBean wrapperBean);

    protected final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        final CrossTabReportWrapperBean reportWrapperBean = (CrossTabReportWrapperBean) command;

        ReportValidator validator = (ReportValidator) getValidator();
        switch (page) {
            case CORE_DETAILS_IDX:
                // set preferred metric to null if none selected
                final String parameter = request.getParameter(PREFERRED_METRIC_FIELD);
                if (!StringUtils.hasText(parameter)) {
                    reportWrapperBean.setPreferredMetric(null);
                }

                break;
            case ADD_COLUMNS_INDX:
                validateColumns(validator, reportWrapperBean, errors);
                break;
        }

        // when going to add columns page check menu sections and do validation
        if (page != ADD_COLUMNS_INDX && getTargetPage(request, page) == ADD_COLUMNS_INDX) {

            // clear menu sections
            clearMenuItemsIfRequired(request, reportWrapperBean);

            // validate core values
            validator.validateCoreValues(reportWrapperBean, errors);
        }

        if (getTargetPage(request, page) == CELL_LABELS_INDX) {
            reportWrapperBean.checkColumnAttributes();
            if (!isFinishRequest(request)) {
                try {
                    assignCellLabels(reportWrapperBean);
                } catch (MaxNumCellsExceededException e) {
                    errors.reject(e.getKey(), e.getMessage());
                }
            }
        }
    }

    protected final void validateColumns(ReportValidator validator, ReportWrapperBean command, Errors errors) throws TalentStudioException {
        validator.validateCrosstabColumns(command, getDynamicAttributeService(), organisationManager, maxCellsNumber, errors);
    }

    protected final void initDrilldownReports(CrossTabReportWrapperBean bean, Long userId) throws TalentStudioException {
        // check that access and population have been set
        if (bean.getPopulationId() != null && StringUtils.hasText(bean.getAccess())) {
            bean.setDrillDownReports(getReportService().findCompatibleReports(bean.getReport(), userId));
        }
    }

    protected final void initMetrics(CrossTabReportWrapperBean bean, Long userId) throws TalentStudioException {
        bean.setMetrics(metricService.findAll(userId, bean.getType(), bean.getAccess()));
    }

    public final void setMetricService(IMetricService metricService) {
        this.metricService = metricService;
    }

    public final void setOrganisationManager(IOrganisationUnitService organisationManager) {
        this.organisationManager = organisationManager;
    }

    public final void setMaxCellsNumber(int maxCellsNumber) {
        this.maxCellsNumber = maxCellsNumber;
    }

    protected CrossTabCellInfo findCellInfo(List<CrossTabCellInfo> cellInfoList, Long horizontalId, Long verticalId) {
        for (Iterator<CrossTabCellInfo> crossTabCellInfoIterator = cellInfoList.iterator(); crossTabCellInfoIterator.hasNext();) {
            CrossTabCellInfo crossTabCellInfo = crossTabCellInfoIterator.next();
            if(horizontalId.equals(crossTabCellInfo.getHorizontalValueId()) && verticalId.equals(crossTabCellInfo.getVerticalValueId())) {
                return crossTabCellInfo;
            }
        }
        return new CrossTabCellInfo(horizontalId, verticalId, "");
    }

    private IOrganisationUnitService organisationManager;

    private int maxCellsNumber;

    private IMetricService metricService;

    protected static final int CHOOSE_METRIC_IDX = 1;
    protected static final int ADD_COLUMNS_INDX = 2;
    protected static final int CELL_LABELS_INDX = 3;

    private static final String PREFERRED_METRIC_FIELD = "preferredMetric";
}
