/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Jun-2010 10:58:27
 */
public class ProgressReportValidator implements Validator {

    public boolean supports(Class clazz) {
        return (ProgressReportWrapperBean.class.isAssignableFrom(clazz));
    }

    public void validate(Object target, Errors errors) {
        ProgressReportWrapperBean bean = (ProgressReportWrapperBean) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.label.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "questionnaireDefinitionId", "error.definition.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "populationId", "error.required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workflowColumn.label", "error.required.field");
        validateColumns(bean.getColumns(), errors);
        validateWorkflows(bean.getWorkflows(), errors);
    }

    private void validateWorkflows(List<ReportWorkflowWrapper> workflows, Errors errors) {
        for (int i = 0; i < workflows.size(); i++) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workflows[" + i + "].label", "error.label.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workflows[" + i + "].questionnaireWorkflowId", "error.required.field");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workflows[" + i + "].position", "error.required.field");
        }
    }

    private void validateColumns(List<ProgressColumnWrapperBean> columns, Errors errors) {
        for (int i = 0; i < columns.size(); i++) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columns[" + i + "].label", "error.label.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columns[" + i + "].attributeName", "error.attribute.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columns[" + i + "].position", "error.required.field");
        }
    }


    public void validateDefinition(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "questionnaireDefinitionId", "error.definition.required");    
    }
}
