/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Controller used for displaying simple reporting structure.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ReportChartController extends ZynapDefaultFormController {

    protected final Object formBackingObject(HttpServletRequest request) throws Exception {

        ReportChartWrapper chartWrapper = (ReportChartWrapper) HistoryHelper.recoverCommand(request, ReportChartWrapper.class);

        if (chartWrapper != null) {
            // if coming back to chart reload chart  
            setPosition(chartWrapper.getPosition().getId(), request, chartWrapper);
        } else {
            
            String displayConfigKey = request.getParameter(ControllerConstants.DISPLAY_CONFIG_KEY);
            final Preference preference = getDomainObjectPreferenceCollection(request);
            chartWrapper = new ReportChartWrapper(displayConfigKey, preference.getPreferenceCollection());
            final Long userId = ZynapWebUtils.getUserId(request);

            try {
                Subject subject = subjectService.findByUserId(userId);
                final List<Position> positions = subject.getPrimaryPositions();
                final boolean hasPositions = positions != null && !positions.isEmpty();
                chartWrapper.setSecure((preference.isSecure() || ZynapWebUtils.isMultiTenant(request)) && hasPositions);

                chartWrapper.setSubjectPositions(positions);
                if (hasPositions) {
                    setPosition(positions.get(0).getId(), request, chartWrapper);
                }

            } catch (Throwable e) {
                // no need to do anything no subject for this user                
            }
        }

        return chartWrapper;
    }

    protected Preference getDomainObjectPreferenceCollection(HttpServletRequest request) throws Exception {
        final Preference preference = new Preference();
        preference.setPreferenceCollection(new DomainObjectPreferenceCollection());
        return preference;
    }

    protected final boolean isFormSubmission(HttpServletRequest request) {
        return ZynapWebUtils.isPostRequest(request) || RequestUtils.getBooleanParameter(request, SUBMIT_PARAM, false);
    }

    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {

        ReportChartWrapper chartWrapper = (ReportChartWrapper) command;

        Map model = new HashMap();

        final String positionIdParam = request.getParameter(ParameterConstants.ARTEFACT_ID);
        if (StringUtils.hasText(positionIdParam)) {
            final Long positionId = Long.valueOf(positionIdParam);
            setPosition(positionId, request, chartWrapper);
        }

        return model;
    }

    protected final ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        return showForm(request, errors, getFormView());
    }

    private void setPosition(final Long positionId, final HttpServletRequest request, ReportChartWrapper chartWrapper) throws TalentStudioException {

        final Long userId = ZynapWebUtils.getUserId(request);
        Position position = getPositionService().findReportStructureFor(positionId, userId);
        chartWrapper.setPosition(position);
    }

    public IPositionService getPositionService() {
        return positionService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private IPositionService positionService;
    private ISubjectService subjectService;

    public static final String SUBMIT_PARAM = "submitParam";
}
