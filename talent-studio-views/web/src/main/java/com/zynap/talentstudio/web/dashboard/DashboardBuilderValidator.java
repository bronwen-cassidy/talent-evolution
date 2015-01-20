/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.DashboardItem;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-May-2010 09:59:30
 */
public class DashboardBuilderValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.isAssignableFrom(DashboardBuilderWrapper.class);
    }

    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.label.required");
        ValidationUtils.rejectIfEmpty(errors, "populationId", "error.required.field");
        // combine the roles and groups -> should not be empty if not a personal builder
        DashboardBuilderWrapper wrapper = (DashboardBuilderWrapper) target;
        if (wrapper.isPersonType()) {
            if (wrapper.getRoleSize() + wrapper.getGroupSize() == 0) errors.reject("error.groups.or.roles.required");
        }

        Dashboard dashboard = wrapper.getModifiedDashboard();

        // at least one item is required
        final List<DashboardItem> itemWrapperList = dashboard.getDashboardItems();
        if (itemWrapperList.isEmpty()) {
            errors.reject("error.one.item.required");
        }
        int index = 0;
        for (DashboardItem itemWrapper : itemWrapperList) {
            if (!StringUtils.hasText(itemWrapper.getLabel())) errors.rejectValue("dashboardItems[" + index + "].label", "error.label.required");
        }
    }    
}
