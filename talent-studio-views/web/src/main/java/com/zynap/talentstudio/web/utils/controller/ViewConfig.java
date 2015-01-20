package com.zynap.talentstudio.web.utils.controller;

import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import java.io.Serializable;
import java.util.Map;

/**
 * User: amark
 * Date: 26-May-2005
 * Time: 11:00:21
 *
 * Config object use to hold a reusable view definition.
 */
public class ViewConfig implements Serializable {

    private String view;

    private Map additionalParameters;

    public ViewConfig() {
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Map getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(Map additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    public ZynapRedirectView getRedirectView() {
        return new ZynapRedirectView(view, additionalParameters);
    }
}
