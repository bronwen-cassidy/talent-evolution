/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.rules;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.rules.IConfigRuleService;
import com.zynap.talentstudio.security.rules.Rule;

import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditConfigRulesController extends ZynapDefaultFormController {

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        ConfigRuleWrapper wrapper = (ConfigRuleWrapper) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.TAB, wrapper.getTargetConfig().getId()));
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        ConfigRuleWrapper wrapper = (ConfigRuleWrapper) command;
        try {
            configRuleService.update(wrapper.getTargetConfig());
        } catch (TalentStudioException e) {
            showForm(request, response, errors);
        }
        
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.TAB, wrapper.getTargetConfig().getId()));
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        ConfigRuleWrapper wrapper = (ConfigRuleWrapper) command;
        Collection rules = wrapper.getTargetConfig().getRules();
        int index = 0;
        for (Iterator iterator = rules.iterator(); iterator.hasNext(); index++) {
            Rule rule = (Rule) iterator.next();
            if (rule.isBoolean()) {
                String ruleValue = request.getParameter("targetConfig.rules[" + index + "].value");
                rule.setValue(ruleValue != null ? "T" : "F");
            }
        }
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ConfigRuleWrapper wrapper = new ConfigRuleWrapper();
        final Collection configs = configRuleService.findAll();
        wrapper.setConfigs(configs);
        String tabParam = request.getParameter(ParameterConstants.CONFIG_ID_PARAM);
        wrapper.setTargetConfig(ConfigHelper.getConfig(new Long(tabParam), configs));
        return wrapper;
    }

    public IConfigRuleService getConfigRuleService() {
        return configRuleService;
    }

    public void setConfigRuleService(IConfigRuleService configRuleService) {
        this.configRuleService = configRuleService;
    }

    private IConfigRuleService configRuleService;
}
