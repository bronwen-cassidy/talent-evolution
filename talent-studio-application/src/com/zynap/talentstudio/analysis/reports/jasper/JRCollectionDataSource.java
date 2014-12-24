package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRField;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.DataFormatter;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.organisation.Node;

import org.apache.commons.collections.comparators.ComparatorChain;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.List;

/**
 * Data source used with Jasper for tabular reports.
 */
public class JRCollectionDataSource extends AbstractReportDataSource {

    public JRCollectionDataSource(Report report, Collection<Object> nodes, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, JasperDataSourceFactory jasperDataSourceFactory,
                                  List<GroupingAttribute> groupingAttributes, String orderAttributeName, int orderDirection, User currentUser) {

        super(report, nodes, questionnaireAnswers, jasperDataSourceFactory, currentUser);
        sortResults(groupingAttributes, orderAttributeName, orderDirection);

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
                getCurrentAnswers(currentRecord);
            } else {
                currentRecord = null;
                clearCurrentAnswers();
            }
        }

        return hasNext;
    }

    public Object getFieldValue(final JRField field) {

        Object value = null;

        if (currentRecord != null) {
            AnalysisParameter analysisParameter = AnalysisAttributeHelper.getAttributeFromName(field.getName());
            value = getFieldValue(field, analysisParameter, (Node) currentRecord);
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
     * Group nodes by grouped attribute names (if any) and order by orderAttributeName in orderDirection.
     *
     * @param groupedAttributeNames String array of attribute ids to group by (can be null)
     * @param orderAttributeName order-by-attribute id (can be null)
     * @param orderDirection the order direction 0 is descending, 1 is ascending
     */
    private void sortResults(List<GroupingAttribute> groupedAttributeNames, String orderAttributeName, int orderDirection) {

        ComparatorChain chain = null;
        int order = orderDirection == 0 ? DEFAULT_ORDER : orderDirection;
        // add group attributes first if any - use default order not order passed in
        if (groupedAttributeNames != null && groupedAttributeNames.size() > 0) {
            chain = new ComparatorChain();
            for (GroupingAttribute groupingAttribute : groupedAttributeNames) {
                RowComparator comparator = new RowComparator(groupingAttribute.getAttributeName(), DEFAULT_ORDER);
                chain.addComparator(comparator);
            }
        }

        // add order by last with direction
        if (StringUtils.hasText(orderAttributeName)) {
            if (chain == null) chain = new ComparatorChain();
            chain.addComparator(new RowComparator(orderAttributeName, order));
        }

        if (chain != null) {
            Collections.sort(this.records, chain);
        }
    }

    private final class RowComparator implements Comparator<Node> {

        private final String attribute;

        private final int orderDirection;

        public RowComparator(String name, int orderDirection) {
            this.attribute = name;
            this.orderDirection = orderDirection;
        }

        public int compare(Node node1, Node node2) {

            AnalysisParameter attr = AnalysisAttributeHelper.getAttributeFromName(attribute);

            final Object value1 = getValueForComparison(attr, node1);
            final Object value2 = getValueForComparison(attr, node2);

            int output = 0;
            if (value1 == null && value2 != null) {
                output = 1;
            } else if (value2 == null && value1 != null) {
                output = -1;
            } else if (value1 instanceof Comparable) {
                output = ((Comparable) value1).compareTo(value2);
            }

            return orderDirection * output;
        }

        private Object getValueForComparison(AnalysisParameter analysisParameter, Node node) {

            Object value;
            if (analysisParameter.isQuestionnaireAttribute()) {
                value = AnalysisAttributeHelper.findAnswer(questionnaireAnswers, node.getId(), analysisParameter);
            } else if (analysisParameter.isDynamicAttribute()) {
                value = getDynamicAttributeValue(analysisParameter, node);
            } else if (analysisParameter.isDerivedAttribute()) {
                value = AnalysisAttributeHelper.getDerivedAttributeValue(analysisParameter.getUnqualifiedName(), node);
            } else {
                if (analysisParameter.isAssociatedArtefactAttribute()) {
                    value = getAssociatedArtefactAttributeValues(node, getColumn(analysisParameter));                    
                } else {
                    value = AnalysisAttributeHelper.getCoreAttributeValue(analysisParameter.getName(), node);
                }
            }
            
            return DataFormatter.formatValue(value);
        }
    }

    /**
     * Default grouping order 1 is ascending.
     */
    public static final int DEFAULT_ORDER = 1;
}
