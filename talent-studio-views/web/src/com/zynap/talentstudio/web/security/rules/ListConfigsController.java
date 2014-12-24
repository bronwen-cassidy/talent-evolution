/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.rules;

import com.zynap.talentstudio.security.rules.IConfigRuleService;
import com.zynap.talentstudio.security.rules.Config;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.ControllerConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ListConfigsController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ConfigRuleWrapper wrapper = new ConfigRuleWrapper();
        Collection configs = configRuleService.findAll();
        wrapper.setConfigs(configs);
        Map model = new HashMap();
        String tabParam = request.getParameter(ParameterConstants.TAB);
        Config first = (Config) (tabParam == null ? configs.iterator().next() : ConfigHelper.getConfig(new Long(tabParam), configs));
        wrapper.setTargetConfig(first);
        model.put(CONFIG_WRAPPER_KEY, wrapper);
        return new ModelAndView(pageView, ControllerConstants.MODEL_NAME, model);
    }

    public void setConfigRuleService(IConfigRuleService configRuleService) {
        this.configRuleService = configRuleService;
    }

    public void setPageView(String pageView) {
        this.pageView = pageView;
    }

    private IConfigRuleService configRuleService;
    private String pageView;

    private static final String CONFIG_WRAPPER_KEY = "wrapper";
}
