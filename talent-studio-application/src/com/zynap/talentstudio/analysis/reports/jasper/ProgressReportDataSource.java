package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRField;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.ReportWorkflow;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;

import java.util.Collection;
import java.util.Map;

/**
 * Data source used with Jasper for tabular reports.
 */
public class ProgressReportDataSource extends AbstractReportDataSource {

    public ProgressReportDataSource(Report report, Collection<Object> workflows, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, Subject currentNode, JasperDataSourceFactory factory, User currentUser) {
        super(report, workflows, questionnaireAnswers, factory, currentUser);
        this.subject = currentNode;
        currentQuestionCollection = questionnaireAnswers.get(subject.getId());
        init();
    }

    public boolean next() {

        boolean goToNextRecord = checkMaxDynamicPosition();
        // if got next record is false has next is true
        boolean hasNext = !goToNextRecord;
        if (goToNextRecord && recordIterator != null) {
            hasNext = recordIterator.hasNext();
            if (hasNext) {
                currentRecord = recordIterator.next();
                calculateDynamicPosition();
            }
        }

        return hasNext;
    }

    protected void calculateDynamicPosition() {
        // set max dynamic position (can be null) and reset current dynamic position
        maxDynamicPosition = currentQuestionCollection != null ? currentQuestionCollection.getMaxDynamicPosition() : null;
        currentDynamicPosition = 0;
        if (report.isLastLineItem() && maxDynamicPosition != null) {
            currentDynamicPosition = maxDynamicPosition.intValue();
        }
    }

    public Object getFieldValue(final JRField field) {

        Object value = null;

        if (currentRecord != null) {
            String fieldName = field.getName();
            QuestionnaireWorkflow workflow = ((ReportWorkflow) currentRecord).getWorkflow();
            if (ReportConstants.WORKFLOW_NAME.equals(fieldName)) {
                value = ((ReportWorkflow) currentRecord).getLabel();
            } else if ("workflowId".equals(fieldName)) {
                value = workflow.getId().toString();
            } else if ("id".equals(fieldName)) {
                return subject.getId() != null ? subject.getId().toString() : "";    
            } else {
                fieldName = AnalysisAttributeHelper.buildQuestionCriteriaId(fieldName, workflow.getId(), ((ReportWorkflow) currentRecord).getRole());
                AnalysisParameter analysisParameter = AnalysisAttributeHelper.getAttributeFromName(fieldName);
                value = getFieldValue(field, analysisParameter, subject);
            }
        }

        return value;
    }

    /**
     * Set record iterator.
     */
    protected void init() {
        if (records != null) {
            recordIterator = records.iterator();
        }
    }


    /**
     * Default grouping order 1 is ascending.
     */
    public static final int DEFAULT_ORDER = 1;
    private Subject subject;
}