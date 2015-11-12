/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.reports.functions.OperandWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.support.CrossTabReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;
import com.zynap.talentstudio.web.utils.ZynapValidationUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ReportValidator implements Validator {

    public boolean supports(Class clazz) {
        return (ReportWrapperBean.class.isAssignableFrom(clazz));
    }

    public void validate(Object obj, Errors errors) {
    }

    public void validateCoreValues(Object obj, Errors errors) {
        ReportWrapperBean rep = (ReportWrapperBean) obj;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.label.required", "Label is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "access", "error.scope.required", "Scope is a required field");
        ZynapValidationUtils.rejectIfNull(errors, "populationId", rep.getPopulationId(), "error.population.required", "Population is a required field");

        if (rep.hasAssignedMenuItems() && rep.isPrivate()) {
            errors.reject("error.cannot.publish.private.report", "You cannot publish a private report");
        }

        if (rep.hasAssignedGroups() && rep.isPrivate()) {
            errors.reject("error.private.report.no.groups", "You cannot publish a private report to any groups");
        }

        if (rep.isCrossTabReport()) {
            CrossTabReportWrapperBean crossTabReportWrapperBean = (CrossTabReportWrapperBean) rep;
            final Integer displayLimit = crossTabReportWrapperBean.getDisplayLimit();
            if (displayLimit != null && displayLimit.intValue() <= 0) {
                errors.rejectValue("displayLimit", "error.display.limit.required", "Display limit must be greater than zero.");
            }
        }

        if(rep.isChartReport()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "chartType", "error.required.field", "Please choose a chart type");
            if(((ChartReport) rep.getReport()).isSpiderChartType()) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "operator", "error.required.field", "Operator is required when running against a population");        
            }
        }

        final Report report = rep.getReport();
        if (report.getDisplayOption() == RunCrossTabReportWrapper.ARTEFACTS_VALUE) {
            if (report.getDisplayReport() == null) {
                errors.rejectValue("resultFormat", "error.display.resultFormat.required", "A display report must be selected for artefact");
            }
        }
    }

    public void validateCrosstabColumns(ReportWrapperBean command, IDynamicAttributeService dynamicAttributeService, IOrganisationUnitService organisationManager, int maxCellsNumber, Errors errors) throws TalentStudioException {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "verticalColumn.attribute", "vertical.column.required", "Vertical column is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "horizontalColumn.attribute", "horizontal.column.required", "Horizontal column is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "verticalColumn.label", "vertical.columnLabel.required", "Vertical column label is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "horizontalColumn.label", "horizontal.columnLabel.required", "Horizontal column label is required");

        // if no errors check that the columns are not the same and that the potential number of cells is acceptable
        if (!errors.hasErrors()) {
            final CrossTabReportWrapperBean crossTabReportWrapperBean = ((CrossTabReportWrapperBean) command);

            final BasicAnalysisAttribute verticalAnalysisAttribute = crossTabReportWrapperBean.getVerticalColumn().getAnalysisAttribute();
            final BasicAnalysisAttribute horizontalAnalysisAttribute = crossTabReportWrapperBean.getHorizontalColumn().getAnalysisAttribute();

            if (horizontalAnalysisAttribute.getAnalysisParameter().equals(verticalAnalysisAttribute.getAnalysisParameter())) {
                errors.rejectValue("verticalColumn.attribute", "crosstab.columns.identical", "Horizontal and vertical columns cannot be identical");
            }

            if (!CrossTabReportRunner.checkNumberOfResults(horizontalAnalysisAttribute, verticalAnalysisAttribute, maxCellsNumber, dynamicAttributeService, organisationManager)) {
                errors.rejectValue("verticalColumn.attribute", "crosstab.excesive.values", "Number of different values combination for horizontal and vertical columns cannot be greater than " + new Integer(maxCellsNumber));
            }
        }
    }

    public void validateMetricColumns(Object command, Errors errors) {
        MetricReportWrapperBean rep = (MetricReportWrapperBean) command;
        final List columns = rep.getColumns();

        // if there are columns check that they all have a label and that there are no duplicates
        if (columns != null && !columns.isEmpty()) {
            for (int i = 0; i < columns.size(); i++) {
                String labelFieldName = "columns[" + i + "].label";
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, labelFieldName, "column.label.required", "Label is required");
            }
            int duplicateCount = 0;
            for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                final ColumnWrapperBean target = (ColumnWrapperBean) iterator.next();

                // only do the following if this is a not a formula column
                if (!target.isFormula()) {
                    duplicateCount = CollectionUtils.countMatches(columns, new Predicate() {
                        public boolean evaluate(Object object) {
                            ColumnWrapperBean source = (ColumnWrapperBean) object;
                            final Metric targetMetric = target.getMetric();
                            final Metric sourceMetric = source.getMetric();

                            return (targetMetric != null && targetMetric.equals(sourceMetric));
                        }
                    });
                }
            }
            if (duplicateCount > 1) {
                errors.reject("error.duplicate.metrics", "Duplicate Metrics not allowed");
            }
        } else {
            errors.reject("metrics.required", "At least one metric must be selected before saving");
        }

        final ColumnWrapperBean groupingColumn = rep.getGroupingColumn();
        if (groupingColumn.isAttributeSet()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "groupingColumn.label", "error.grouping.column.label.required", "Label is required");
        } else if (rep.getDrillDownReportId() != null) {
            // reject as we cannot have a drill down without a grouping column
            errors.rejectValue("drillDownReportId", "error.grouping.column.required", "Cannot have a drill down report without selecting a group");
        }
    }

    public void validateTabularColumns(Object command, Errors errors) {
        ReportWrapperBean reportWrapperBean = (ReportWrapperBean) command;

        final List<ColumnWrapperBean> columnWrapperBeans = reportWrapperBean.getColumns();

        for (int i = 0; i < columnWrapperBeans.size(); i++) {
            ColumnWrapperBean wrapper = columnWrapperBeans.get(i);
            String fieldName = "columns[" + i + "].label";
            String attributeNameField = "columns[" + i + "].attribute";

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldName, "error.label.required", "Label is a required field");
            if (!wrapper.isFormula()) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, attributeNameField, "error.attribute.required", "Attribute is a required field");
            }
        }
    }

    public void validateFormula(TabularReportWrapperBean reportWrapperBean, Errors errors) {
        validateFormulaOperands(reportWrapperBean, errors, "attribute");
    }

    public void validateFormula(MetricReportWrapperBean reportWrapperBean, Errors errors) {
        validateFormulaOperands(reportWrapperBean, errors, "metricId");
    }

    private void validateFormulaOperands(ReportWrapperBean reportWrapperBean, Errors errors, String field) {

        final List<OperandWrapperBean> operands = reportWrapperBean.getSelectedFunction().getOperands();
        final int numberOfOperands = operands.size();

        Stack leftBrackets = new Stack();

        int i = 0;
        for (Iterator<OperandWrapperBean> iterator = operands.iterator(); iterator.hasNext(); i++) {

            OperandWrapperBean operandWrapperBean = iterator.next();

            String attributeNameField = "selectedFunction.operands[" + i + "]." + field;
            String operatorField = "selectedFunction.operands[" + i + "].operator";

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, attributeNameField, "error.attribute.required", "Element is a required field");
            if (i < numberOfOperands - 1) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, operatorField, "error.operator.required", "Operator is a required field");
            }

            // push left onto stack
            if (StringUtils.hasText(operandWrapperBean.getLeftBracket())) {
                leftBrackets.push(Integer.toString(i));
            }

            // pop when you find a right - if none found or stack is empty there is a problem
            if (StringUtils.hasText(operandWrapperBean.getRightBracket())) {
                Object popped = null;
                if (!leftBrackets.empty()) {
                    popped = leftBrackets.pop();
                }

                if (popped == null) {
                    addBracketError(errors, i);
                }
            }
        }

        // if brackets left over in the stack then the brackets are mismatched
        for (Iterator iterator = leftBrackets.iterator(); iterator.hasNext();) {
            String pos = (String) iterator.next();
            addBracketError(errors, Integer.parseInt(pos));
        }

        // check the last operand does not have a trailing operand
        int lastIndex = operands.size() - 1;
        OperandWrapperBean operandWrapperBean = operands.get(lastIndex);
        if (StringUtils.hasText(operandWrapperBean.getOperator())) {
            errors.rejectValue("selectedFunction.operands[" + lastIndex + "].operator", "error.extra.operator", "Operator must be followed by a value");
        }
    }

    private void addBracketError(Errors errors, int i) {
        errors.rejectValue("selectedFunction.operands[" + i + "].leftBracket", "function.brackets.notmatched", "Please make sure your brackets match.");
    }

    public void validateChartColumns(ReportWrapperBean bean, Errors errors) {
        if (bean.getColumns() == null || bean.getColumns().isEmpty()) {
            errors.reject("error.columns.required", "At least one answer must be selected");
        }
        ChartReportWrapperBean chartBean = (ChartReportWrapperBean) bean;
        int numSelected = 0;
        int index = 0;
        List<String> values = new ArrayList<String>();
        List<Column> columns = chartBean.getReportColumns();
        List<String> labels = new ArrayList<String>();

        for (Column column : columns) {
            final String val = column.getValue();
            if (StringUtils.hasText(val)) {
                numSelected++;
                String fieldName = "reportColumns[" + index + "].label";
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldName, "error.label.required", "Label is a required field");
                if (values.contains(val)) {
                    errors.rejectValue("reportColumns[" + index + "].value", "error.duplicate.value");
                }
                if (labels.contains(column.getLabel())) {
                    errors.rejectValue(fieldName, "error.duplicate.label");
                }                
                values.add(val);
                labels.add(column.getLabel());
            }
            index++;
        }
        if (numSelected < 1) {
            errors.reject("error.columns.required", "At least one value must be provided");
        }
    }

    public void validateChartAttributes(ChartReportWrapperBean command, Errors errors) {
        List<ColumnWrapperBean> columnWrapperBeans = command.getColumns();
        int attrSetCount = 0;
        for (ColumnWrapperBean columnWrapperBean : columnWrapperBeans) {
            if (columnWrapperBean.isAttributeSet()) {
                attrSetCount++;
            }
        }
        if (attrSetCount < 1) errors.reject("error.atleast.attribute.required", "At least one attribute must be selected");
    }

    public void validateSpiderChartColumns(SpiderChartWrapperBean command, Errors errors) {
        List<ColumnWrapperBean> columnWrapperBeans = command.getColumns();
        if(columnWrapperBeans.isEmpty()) errors.reject("error.one.group.required", "At Least one group is required");
        int index = 0;
        List<String> labels = new ArrayList<String>();
        for (ColumnWrapperBean bean : columnWrapperBeans) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columns[" + index + "].label", "error.required.field");
            String label = bean.getLabel();
            if(labels.contains(label)) errors.rejectValue("columns[" + index + "].label", "error.duplicate.value");
            labels.add(label);
            index++;
        }        
    }

    public void validateSpiderChartAttributes(SpiderChartWrapperBean bean, Errors errors) {
        List<ChartColumnAttributeWrapper> chartAttributeWrappers = bean.getColumnAttributes();
        if(chartAttributeWrappers.isEmpty()) errors.reject("error.one.attribute.required", "At Least one Attribute is required");
        for (int index = 0; index < chartAttributeWrappers.size(); index++) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columnAttributes[" + index + "].columnLabel", "error.required.field");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columnAttributes[" + index + "].label", "error.required.field");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columnAttributes[" + index + "].attribute", "error.required.field");
        }
    }
}
