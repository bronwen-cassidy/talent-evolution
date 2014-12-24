package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.Page;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.managers.TabularReportFiller;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 01-Mar-2006
 * Time: 10:21:00
 */
public final class TabularReportRunner extends ReportRunner {

    public void run(RunReportWrapperBean wrapper, Long userId) throws TalentStudioException {

        final List<AnalysisParameter> questionnaireAttributes = wrapper.getNonAssociatedQuestionnaireAttributes();

        final Population population = wrapper.getPopulation();

        assignOrderAttributes(wrapper, population);

        String orderAttributeName = null;
        if (wrapper.getSortOrder() == RunReportWrapperBean.DESCENDING) {
            orderAttributeName = wrapper.getOrderBy();
        }
        Map<Long, QuestionAttributeValuesCollection> answers = null;
        if (wrapper.isCsvExport()) {
            if (!questionnaireAttributes.isEmpty() && IPopulationEngine.P_SUB_TYPE_.equals(population.getType())) {
                answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, population, userId);
            }
            final List results = populationEngine.find(population, userId);
            wrapper.setArtefacts(results);
            FilledReport filledReport = reportFiller.fillReport(wrapper.getReport(), results, answers, wrapper.getGroupedAttributes(), orderAttributeName, wrapper.getSortOrder(), true);
            wrapper.setFilledReport(filledReport);
            
        } else {

            Page displayPage = wrapper.getPage();
            if (displayPage == null) {
                // running first page only
                // clear compiled sql
                population.setCompiledSql(null);
                displayPage = populationEngine.find(population, userId, 0, ReportConstants.RESULTS_PER_PAGE_NUM, -1);
            } else {
                //main page exists go and find the results for the selected next page
                int start = wrapper.getPageStart();
                Integer numRecords = displayPage.getNumRecords();
                if(wrapper.isRecount()) {
                    numRecords = -1;
                    start = 0;                    
                }

                displayPage = populationEngine.find(population, userId, start, start + ReportConstants.RESULTS_PER_PAGE_NUM, numRecords);
            }
            wrapper.setPage(displayPage);
            final List<? extends Node> results = displayPage.getResults();

            if (!results.isEmpty() && !questionnaireAttributes.isEmpty() && IPopulationEngine.P_SUB_TYPE_.equals(population.getType())) {
                answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, buildNodeIdsList(results), userId);
            }
            
            wrapper.setRecount(false);
            wrapper.setArtefacts(results);
            FilledReport filledReport = reportFiller.fillReport(wrapper.getReport(), results, answers, null, null, wrapper.getSortOrder(), false);
            wrapper.setFilledReport(filledReport);
        }
    }

    private List<Long> buildNodeIdsList(List<? extends Node> results) {
        List<Long> nodeIds = new ArrayList();
        for (Node result : results) {
            nodeIds.add(result.getId());
        }
        return nodeIds;
    }

    private void assignOrderAttributes(RunReportWrapperBean wrapper, Population population) {

        final String orderBy = wrapper.getOrderBy();
        final int order = wrapper.getSortOrder();
        population.setGroupingAttributes(wrapper.getGroupedAttributes());
        population.setSortDirection(order == RunReportWrapperBean.ASCENDING ? Population.ORDER_ASC : Population.ORDER_DESCENDING);

        final List<ColumnWrapperBean> orderableColumns = wrapper.getOrderableColumns();
        final List<GroupingAttribute> orderAttributes = new ArrayList<GroupingAttribute>();
        for (int i = 0; i < orderableColumns.size(); i++) {
            
            ColumnWrapperBean columnWrapperBean = orderableColumns.get(i);
            if(columnWrapperBean.getAttribute().equals(orderBy)) {
                Column column = (Column) columnWrapperBean.getAnalysisAttribute();
                if (FULL_NAME_ATTR.equals(orderBy)) {

                    final Column newColumn1 = (Column) column.clone();
                    newColumn1.setAttributeName(SubjectSearchQuery.FIRST_NAME);
                    orderAttributes.add(new GroupingAttribute(SubjectSearchQuery.FIRST_NAME, order, newColumn1));
                    final Column newColumn2 = (Column) column.clone();
                    newColumn2.setAttributeName(SubjectSearchQuery.SECOND_NAME);
                    orderAttributes.add(new GroupingAttribute(SubjectSearchQuery.SECOND_NAME, order, newColumn2));
                } else {
                    
                    orderAttributes.add(new GroupingAttribute(orderBy, order, column));
                }
            }
        }
        population.setOrderAttributes(orderAttributes);
    }

    public void setReportFiller(TabularReportFiller reportFiller) {
        this.reportFiller = reportFiller;
    }

    private TabularReportFiller reportFiller;
    private static final String FULL_NAME_ATTR = "coreDetail.name";
}
