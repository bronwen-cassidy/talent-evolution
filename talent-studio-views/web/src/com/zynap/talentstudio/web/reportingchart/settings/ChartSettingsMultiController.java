/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.talentstudio.preferences.IPreferencesManager;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.preferences.PreferenceConstants;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ChartSettingsMultiController extends ZynapMultiActionController {

    public ModelAndView listSystemChartSettingsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Collection preferences = getPreferencesManager().getSystemPreferences();

        return getModelAndView(preferences, PreferenceConstants.SYSTEM_PREFERENCE_TYPE);
    }

    public ModelAndView listUserChartSettingsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Collection preferences = getPreferencesManager().getPreferences(ZynapWebUtils.getUserId(request));

        return getModelAndView(preferences, PreferenceConstants.USER_PREFERENCE_TYPE);
    }

    public ModelAndView viewChartSettingsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Long preferenceId = getPreferenceId(request);
        final Preference preference = getPreferencesManager().getPreference(preferenceId);

        ViewSettingsWrapperBean bean = new ViewSettingsWrapperBean(preference);

        Map model = new HashMap();
        model.put(BEAN_KEY, bean);

        return new ModelAndView(DISPLAY_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    public IPreferencesManager getPreferencesManager() {
        return _preferencesManager;
    }

    public void setPreferencesManager(IPreferencesManager preferencesManager) {
        _preferencesManager = preferencesManager;
    }

    public static final Long getPreferenceId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, PREFERENCE_ID);
    }

    private ModelAndView getModelAndView(Collection preferences, final String type) {
        Map model = new HashMap();
        model.put(PREFERENCES_KEY, preferences);
        model.put(TYPE_KEY, type);
        return new ModelAndView(LIST_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    private IPreferencesManager _preferencesManager;

    static final String LIST_VIEW = "listchartsettings";
    static final String DISPLAY_VIEW = "viewchartsettings";

    public static final String PREFERENCE_ID = "preferenceId";
    public static final String PREFERENCES_KEY = "preferences";
    public static final String TYPE_KEY = "preferenceType";
    public static final String BEAN_KEY = "bean";
}
