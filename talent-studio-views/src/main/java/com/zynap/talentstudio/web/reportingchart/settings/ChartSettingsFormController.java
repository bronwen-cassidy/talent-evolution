/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller that handles editing of reporting chart settings.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ChartSettingsFormController extends ChartSettingsController {

    protected Preference getPreference(HttpServletRequest request) throws Exception {
        final Long preferenceId = ChartSettingsMultiController.getPreferenceId(request);

        return getPreferencesManager().getPreference(preferenceId);
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        final SettingsWrapperFormBean bean = (SettingsWrapperFormBean) command;
        final Preference modifiedPreference = bean.getModifiedPreference();

        final UserSession userSession = ZynapWebUtils.getUserSession(request);

        try {
            getPreferencesManager().updatePreference(modifiedPreference);
            userSession.reloadMenus();

            return new ModelAndView(new ZynapRedirectView(getSuccessView(), ChartSettingsMultiController.PREFERENCE_ID, modifiedPreference.getId()));
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("viewName", "error.duplicate.chart.viewname", "Please select another name - the one you have chosen is in use");
            return showForm(request, response, errors);
        }
    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException e) throws Exception {
        SettingsWrapperFormBean bean = (SettingsWrapperFormBean) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ChartSettingsMultiController.PREFERENCE_ID, bean.getId()));
    }
}
