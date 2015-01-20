/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.managers;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.ChartReportAttribute;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.chart.ChartDataStructure;
import com.zynap.talentstudio.analysis.reports.chart.ChartSlice;
import com.zynap.talentstudio.analysis.reports.crosstab.ArtefactAttributeViewFormatter;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.BarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.PieChartProducer;
import com.zynap.talentstudio.web.analysis.reports.data.ChartFilledReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 25-May-2010 08:40:20
 */
public class ChartReportFiller {

    public FilledReport fillReport(Report report, List<? extends Subject> subjects, List<NodeExtendedAttribute> answers) throws TalentStudioException {

        ChartReport cReport = (ChartReport) report;
        Map<ChartMapLookupKey, Boolean> lookupMap = buildLookupMap(subjects, cReport.getChartReportAttributes());

        final Map<String, ChartSlice> values = new LinkedHashMap<String, ChartSlice>();
        List<Column> columns = report.getColumns();
        Column nullValueColumn = null;

        for (Column column : columns) {
            String colValue = column.getValue();

            if (Column.NO_VALUE.equals(colValue)) {
                nullValueColumn = column;
                colValue = column.getLabel();
            }

            values.put(colValue, new ChartSlice(column));
        }

        processAnswers(cReport, answers, values, lookupMap);

        assignNullValueValues(lookupMap, values, nullValueColumn);

        ChartDataStructure cds = new ChartDataStructure(values, report);
        AbstractChartProducer producer;
        if (cReport.isPieChartType()) {
            producer = new PieChartProducer(cds);
        } else {
            producer = new BarChartProducer(cds);
        }
        return new ChartFilledReport(cds, producer, cReport.getChartType());
    }

    private void assignNullValueValues(Map<ChartMapLookupKey, Boolean> lookupMap, Map<String, ChartSlice> values, Column nullValueColumn) {
        if (nullValueColumn != null) {
            ChartSlice chartSlice = values.get(nullValueColumn.getLabel());
            for (Map.Entry<ChartMapLookupKey, Boolean> entry : lookupMap.entrySet()) {
                if (entry.getValue().equals(Boolean.FALSE)) {
                    final ChartMapLookupKey lk = entry.getKey();
                    final Questionnaire node = new Questionnaire();
                    node.setSubjectId(lk.subjectId);
                    final NodeExtendedAttribute answer = new NodeExtendedAttribute(nullValueColumn.getValue(), node, new DynamicAttribute(lk.dynamicAttributeId));
                    chartSlice.add(answer);
                }
            }
        }
    }

    private Map<ChartMapLookupKey, Boolean> buildLookupMap(List<? extends Subject> subjects, final List<ChartReportAttribute> attributeSet) {
        Map<ChartMapLookupKey, Boolean> lookupMap = new HashMap<ChartMapLookupKey, Boolean>();
        for (ChartReportAttribute attr : attributeSet) {
            for (Subject subject : subjects) {
                lookupMap.put(new ChartMapLookupKey(subject.getId(), attr.getDynamicAttributeId(), attr.getQuestionnaireWorkflowId()), Boolean.FALSE);
            }
        }
        return lookupMap;
    }

    private void processAnswers(ChartReport report, List<NodeExtendedAttribute> answers, Map<String, ChartSlice> values, Map<ChartMapLookupKey, Boolean> lookupMap) {

        boolean lastLineItem = report.isLastLineItem();
        // need to preprocess
        if (lastLineItem) {
            Map<String, AttributeValue> mappedAnswers = new HashMap<String, AttributeValue>();
            for (NodeExtendedAttribute answer : answers) {
                String key = answer.getNode().getId() + "_" + answer.getDynamicAttribute().getId();
                AttributeValue value = mappedAnswers.get(key);
                if (value == null) {
                    value = AttributeValue.create(answer);
                    mappedAnswers.put(key, value);
                } else {
                    value.addValue(answer, false);
                }
            }

            Collection<AttributeValue> valueCollection = mappedAnswers.values();
            answers = new ArrayList<NodeExtendedAttribute>();
            for (AttributeValue attributeValue : valueCollection) {
                answers.add(attributeValue.getLastNodeExtendedAttribute());
            }
        }
        for (NodeExtendedAttribute answer : answers) {
            // each one is valid as an answer unless we are a dynamic_position and the report is a last-line-item report then we have to do something different
            String key = ArtefactAttributeViewFormatter.formatValue(answer, null);
            ChartSlice cds = values.get(key);
            if (cds != null) {
                cds.add(answer);
                Questionnaire questionnaire = (Questionnaire) answer.getNode();
                Long subjectId = questionnaire.getSubjectId();
                Long dynamicAttributeId = answer.getDynamicAttribute().getId();
                Long queWorkflowId = questionnaire.getQuestionnaireWorkflowId();
                ChartMapLookupKey lookupKey = new ChartMapLookupKey(subjectId, dynamicAttributeId, queWorkflowId);
                lookupMap.put(lookupKey, Boolean.TRUE);
            }
        }
    }

    private class ChartMapLookupKey {

        public ChartMapLookupKey(Long subjectId, Long dynamicAttributeId, Long qwfId) {
            this.subjectId = subjectId;
            this.dynamicAttributeId = dynamicAttributeId;
            this.questionnaireWorkflowId = qwfId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ChartMapLookupKey that = (ChartMapLookupKey) o;

            if (!subjectId.equals(that.subjectId)) return false;
            if (!dynamicAttributeId.equals(that.dynamicAttributeId)) return false;
            if (questionnaireWorkflowId == null && that.questionnaireWorkflowId != null) return false;
            if (questionnaireWorkflowId != null && !questionnaireWorkflowId.equals(that.questionnaireWorkflowId)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = subjectId.hashCode();
            result = 31 * result + dynamicAttributeId.hashCode();
            result = 31 * result + (questionnaireWorkflowId != null ? questionnaireWorkflowId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "subject = " + subjectId + " dynamicAttribute = " + dynamicAttributeId + " workflow = " + questionnaireWorkflowId;
        }

        private Long subjectId;
        private Long dynamicAttributeId;
        private Long questionnaireWorkflowId;
    }

}
