/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.exception.TalentStudioException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 25-Sep-2009 17:42:01
 */
public class AppraisalReportBuilder {

    public Map<Report, List<Row>> buildResults(Long subjectId) throws TalentStudioException {
        // get the appraisal_reports
        List<Report> reports = reportService.findAppraisalReports(subjectId, AppraisalSummaryReport.STATUS_PUBLISHED);

        // get the completed questionnaires for the appraisals in the reports
        return buildResults(subjectId, reports);
    }

    public Map<Report, List<Row>> buildResults(Long subjectId, List<Report> reports) throws TalentStudioException {

        Map<Report, List<Row>> mappedResults = new HashMap<Report, List<Row>>();

        for (Report report : reports) {

            buildResult(subjectId, mappedResults, report);
        }

        return mappedResults;
    }

    public void buildResult(Long subjectId, Map<Report, List<Row>> mappedResults, Report report) throws TalentStudioException {

        final QuestionnaireWorkflow workflow = ((AppraisalSummaryReport) report).getPerformanceReview().getEvaluatorWorkflow();
        List<Questionnaire> questionnaires = questionnaireService.findCompletedQuestionnaires(workflow.getId(), subjectId);
        if (questionnaires.isEmpty()) return;

        List<Row> results = new ArrayList<Row>();
        List<LookupValue> headers = buildHeaders(questionnaires);
        results.add(buildHeaderRow(headers));

        Row average = new Row();
        Row weightedAverage = new Row();

        for (Column column : report.getColumns()) {
            // each column represents a row
            Row row = new Row();
            Cell.CalculationCell totalCell = new Cell.CalculationCell("", report.getOperator(), 0);

            final Integer weighting = column.getWeighting();
            int weigthValue = weighting != null ? weighting.intValue() : 0;

            for (LookupValue header : headers) {
                // get the answers for the question and role
                if (header.getLabel().equals(QUESTION_HEADER)) {
                    row.add(new Cell.DefaultCell(column.getLabel()));
                } else if (header.getLabel().equals(WEIGHTING_HEADER)) {
                    row.add(new Cell.DefaultCell(weigthValue + "%"));
                } else if (header.getLabel().equals(TOTAL_HEADER)) {
                    row.add(totalCell);
                } else {
                    Cell.CalculationCell calcCell = new Cell.CalculationCell("", report.getOperator(), weigthValue);
                    applyAnswers(header, column, calcCell, questionnaires);
                    totalCell.add(calcCell.getDisplayValue());
                    row.add(calcCell);
                }
            }
            results.add(row);
        }
        buildAverageRows(average, weightedAverage, results);
        mappedResults.put(report, results);
    }

    public Integer numArchivedReports(Long subjectId) {
        return new Integer(reportService.findAppraisalReports(subjectId, AppraisalSummaryReport.STATUS_ARCHIVED).size());
    }

    private void buildAverageRows(Row average, Row weightedAverage, List<Row> results) {

        average.add(new Cell.DefaultCell(AVERAGE_LABEL));
        weightedAverage.add(new Cell.DefaultCell(WEIGHTED_AVERAGE_LABEL));
        average.add(new Cell.DefaultCell(""));
        weightedAverage.add(new Cell.DefaultCell(""));

        // skip the header cells so start at 1
        for (int j = 1; j < results.size(); j++) {

            Row row = results.get(j);
            final List<Cell> cells = row.getCells();

            // skip the question and weighting cells which are at indexes 0 and 1, hence we start at 2
            final int dataStartIndex = 2;
            for (int i = dataStartIndex; i < (cells.size() - 1); i++) {
                final Cell cell = cells.get(i);
                final String displayValue = cell.getDisplayValue();

                Cell.CalculationCell averageCell = (Cell.CalculationCell) average.getCell(i);
                if (averageCell == null) {
                    averageCell = new Cell.CalculationCell(displayValue, cell.getOperator(), cell.getWeighting());
                    average.add(averageCell);
                }
                averageCell.add(displayValue);

                Cell.CalculationCell weightedCell = (Cell.CalculationCell) weightedAverage.getCell(i);
                if (weightedCell == null) {
                    weightedCell = new Cell.CalculationCell(displayValue, cell.getOperator(), cell.getWeighting());
                    weightedAverage.add(weightedCell);
                }
                weightedCell.add(displayValue, cell.getWeighting());
            }
        }

        calculateTotalAverages(average, weightedAverage);
        results.add(average);
        results.add(weightedAverage);
    }

    private void calculateTotalAverages(Row average, Row weightedAverage) {


        Cell.CalculationCell totalAvCell = new Cell.CalculationCell("", IPopulationEngine.AVG, 0);
        Cell.CalculationCell totalWAvCell = new Cell.CalculationCell("", IPopulationEngine.AVG, 0);

        final List<Cell> averageCells = average.getCells();
        final List<Cell> weightedAverageCells = weightedAverage.getCells();

        for (int i = 2; i < averageCells.size(); i++) {

            Cell.CalculationCell dataCell = (Cell.CalculationCell) averageCells.get(i);
            Cell.CalculationCell weightedDataCell = (Cell.CalculationCell) weightedAverageCells.get(i);

            totalAvCell.add(dataCell.getDisplayValue());
            totalWAvCell.add(weightedDataCell.getDisplayValue());
        }

        average.add(totalAvCell);
        weightedAverage.add(totalWAvCell);
    }

    private Row buildHeaderRow(List<LookupValue> headers) {
        Row row = new Row();
        for (LookupValue header : headers) {
            row.add(new Cell.DefaultCell(header.getLabel()));
        }
        return row;
    }

    private void applyAnswers(LookupValue header, Column column, Cell.CalculationCell calcCell, List<Questionnaire> questionnaires) {
        for (Questionnaire questionnaire : questionnaires) {
            if (questionnaire.getRole().equals(header)) {
                final AttributeValue answer = questionnaire.getDynamicAttributeValues().get(new DynamicAttribute(column.getDynamicAttributeId()));
                if (answer != null) {
                    calcCell.add(answer.getValue());
                }
            }
        }
    }

    private List<LookupValue> buildHeaders(List<Questionnaire> questionnaires) {
        List<LookupValue> result = new ArrayList<LookupValue>();

        for (Questionnaire questionnaire : questionnaires) {
            final LookupValue value = questionnaire.getRole();
            if (!result.contains(value)) {
                result.add(value);
            }
        }
        Collections.sort(result, new Comparator<LookupValue>() {
            public int compare(LookupValue o1, LookupValue o2) {
                return o1.compareBySortOrder(o2);
            }
        });

        result.add(0, new LookupValue(QUESTION_HEADER, QUESTION_HEADER, "", new LookupType()));
        result.add(1, new LookupValue(WEIGHTING_HEADER, WEIGHTING_HEADER, "", new LookupType()));
        result.add(new LookupValue(TOTAL_HEADER, TOTAL_HEADER, "", new LookupType()));
        return result;
    }


    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    private IQuestionnaireService questionnaireService;
    private IReportService reportService;
    private static final String QUESTION_HEADER = "Question";
    private static final String WEIGHTING_HEADER = "Weighting";
    private static final String TOTAL_HEADER = "Total Ave";
    private static final String AVERAGE_LABEL = "Average";
    private static final String WEIGHTED_AVERAGE_LABEL = "Weighted Average";
}
