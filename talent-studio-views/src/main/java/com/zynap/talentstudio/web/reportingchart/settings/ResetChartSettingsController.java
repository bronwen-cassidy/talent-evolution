package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.talentstudio.preferences.IPreferencesManager;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: amark
 * Date: 22-Nov-2005
 * Time: 14:34:18
 */
public class ResetChartSettingsController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        final Long preferenceId = ChartSettingsMultiController.getPreferenceId(request);
        return preferencesManager.getPreference(preferenceId);
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        Preference preference = (Preference) command;

        if (ZynapWebUtils.isConfirmed(request)) {
            final Long id = preference.getId();
            preferencesManager.resetPreference(id);
            return new ModelAndView(new ZynapRedirectView(getSuccessView(), ChartSettingsMultiController.PREFERENCE_ID, id));
        } else {
            return new ModelAndView(getFormView());
        }
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        Preference preference = (Preference) command;

        return new ModelAndView(new ZynapRedirectView(getCancelView(), ChartSettingsMultiController.PREFERENCE_ID, preference.getId()));
    }

    public void setPreferencesManager(IPreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    private IPreferencesManager preferencesManager;
}