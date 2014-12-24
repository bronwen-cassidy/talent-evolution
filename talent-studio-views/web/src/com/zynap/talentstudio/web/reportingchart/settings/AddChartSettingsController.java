package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.preferences.PreferenceConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: amark
 * Date: 21-Nov-2005
 * Time: 17:55:32
 */
public class AddChartSettingsController extends ChartSettingsController {

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        final SettingsWrapperFormBean bean = (SettingsWrapperFormBean) command;
        final Preference preference = bean.getModifiedPreference();

        final UserSession userSession = ZynapWebUtils.getUserSession(request);

        try {
            getPreferencesManager().createPreference(preference);
            userSession.reloadMenus();

            return new ModelAndView(new ZynapRedirectView(getSuccessView(), ChartSettingsMultiController.PREFERENCE_ID, preference.getId()));
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("viewName", "error.duplicate.chart.viewname", "Please select another name - the one you have chosen is in use");
            return showForm(request, response, errors);
        }
    }

    protected Preference getPreference(HttpServletRequest request) throws Exception {

        final Preference defaultPreference = getPreferencesManager().getPreference(PreferenceConstants.MASTER_REPORTING_CHART_VIEW);
        final String type = RequestUtils.getRequiredStringParameter(request, ChartSettingsMultiController.TYPE_KEY);

        // build a new DomainObjectPreferenceCollection with the current user's id
        // and the type from the request and the default preference's settings
        return new Preference(null, type, defaultPreference.getPreferenceCollection(), ZynapWebUtils.getUserSession(request).getId());
    }
}