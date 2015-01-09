package com.zynap.talentstudio.analysis.reports.jasper;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.DataFormatter;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.crosstab.ArtefactAttributeViewFormatter;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * User: amark.
 * Date: 17-May-2006
 * Time: 12:36:33
 */
public class CSVReportDataSource extends JRCollectionDataSource {

    protected CSVReportDataSource(Report report, Collection<Object> nodes, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, JasperDataSourceFactory jasperDataSourceFactory,
                                  List<GroupingAttribute> groupingAttributes, String orderAttributeName, int orderDirection, User currentUser) {
        super(report, nodes, questionnaireAnswers, jasperDataSourceFactory, groupingAttributes, orderAttributeName, orderDirection, currentUser);
    }

    /**
     * Get value from node.
     * <br/> If current dynamic position is zero or corresponding column is grouped return value.
     * <br/> If current dynamic position is greater than zero we are dealing with dynamic line items with more than one answer
     * so only return the questionnaire answer or DataFormatter.EMPTY_VALUE for any AnalysisParameters that are not questionnaire based.
     *
     * @param analysisParameter the parameter containing the attributes for the column
     * @param currentNode       the current node being considered, and providing the answers
     * @return Object or null
     */
    protected final Object getValueFromNode(AnalysisParameter analysisParameter, Node currentNode) {

        Object value = DataFormatter.EMPTY_VALUE;

        try {

            if (analysisParameter.isQuestionnaireAttribute()) {
                AttributeValue attributeValue = AnalysisAttributeHelper.findAnswer(analysisParameter, currentQuestionCollection);
                if (attributeValue != null) {
                    final boolean dynamic = attributeValue.getDynamicAttribute().isDynamic();
                    if (dynamic) {
                        NodeExtendedAttribute nodeExtendedAttribute;
                        if (report.isLastLineItem()) {
                            nodeExtendedAttribute = attributeValue.getAttributeAtLastDynamicPosition();
                        } else {
                            nodeExtendedAttribute = attributeValue.getAttributeAtDynamicPosition(currentDynamicPosition);
                        }
                        if (nodeExtendedAttribute == null) value = ReportConstants.BLANK_VALUE;
                        else value = ArtefactAttributeViewFormatter.formatValue(nodeExtendedAttribute, null);
                    } else {
                        value = attributeValue;
                    }
                }
            } else if (analysisParameter.isDynamicAttribute()) {
                value = getDynamicAttributeValue(analysisParameter, currentNode);
            } else if (analysisParameter.isDerivedAttribute()) {
                value = AnalysisAttributeHelper.getDerivedAttributeValue(analysisParameter.getName(), currentNode);
            } else if (analysisParameter.isAssociatedArtefactAttribute()) {
                value = getAssociatedArtefactAttributeValues(currentNode, getColumn(analysisParameter));
            } else value = AnalysisAttributeHelper.getCoreAttributeValue(analysisParameter.getName(), currentNode);

        } catch (Exception e) {
            value = null;
        }

        return value;
    }
}