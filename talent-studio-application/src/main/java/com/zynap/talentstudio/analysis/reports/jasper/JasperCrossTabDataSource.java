package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRField;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.DataFormatter;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTableKey;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.talentstudio.organisation.Node;

import java.util.*;

/**
 * User: amark
 * Date: 16-May-2006
 * Time: 10:15:12
 */
public final class JasperCrossTabDataSource extends AbstractReportDataSource {

    public JasperCrossTabDataSource(Report report, Collection nodes, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, JasperDataSourceFactory dataSourceFactory, String[] groupedAttributeNames, Collection rowHeaders, Collection columnHeaders, User user) {

        super(report, nodes, questionnaireAnswers, dataSourceFactory, user);

        this.rowHeaders = rowHeaders;
        this.columnHeaders = columnHeaders;
        this.groupedAttributeNames = groupedAttributeNames;

        init();
    }

    public boolean next() {

        // first attempt to get current element from currentResults (which iterates the Nodes in the List for the row)
        boolean hasNext = getNextRecord();

        // if none found move to next row
        if (!hasNext) {
            hasNext = recordIterator.hasNext();
            if (hasNext) {

                // if results has next does not matter if we there are entries in the list
                // IMPORTANT as this ensures that grid entries with no values are still processed

                final Map.Entry entry = (Map.Entry) recordIterator.next();
                currentKey = (CrossTableKey) entry.getKey();
                final List list = (List) entry.getValue();
                if (list != null) {
                    // set current results, has next must be true
                    currentResults = list.iterator();
                    getNextRecord();
                }
            }
        }

        return hasNext;
    }

    public Object getFieldValue(JRField field) {

        final String name = field.getName();

        Object value = null;
        if (getVerticalGroupingAttribute().equals(name)) {
            value = currentKey.getRowValue();
        } else if (getHorizontalGroupingAttribute().equals(name)) {
            value = currentKey.getColValue();
        } else {
            if (currentRecord != null) {
                AnalysisParameter analysisParameter = AnalysisAttributeHelper.getAttributeFromName(name);
                value = getFieldValue(field, analysisParameter, (Node) currentRecord);
            }
        }

        return value;
    }

    private boolean getNextRecord() {

        boolean goToNextRecord = checkMaxDynamicPosition();

        // if got next record is false has next is true
        boolean hasNext = !goToNextRecord;

        if (goToNextRecord && currentResults != null) {
            hasNext = currentResults.hasNext();
            if (hasNext) {
                currentRecord = currentResults.next();
                getCurrentAnswers(currentRecord);
            } else {
                currentRecord = null;
                clearCurrentAnswers();
            }
        }

        return hasNext;
    }

    /**
     * Get grouping column value(s) for node.
     *
     * @param analysisParameter
     * @param node
     * @return Collection of values (will never be empty - will always contain at least value - ReportConstants.NA).
     */
    private Collection getGroupingColumnValue(final AnalysisParameter analysisParameter, Node node) throws Exception {

        Collection<String> values = new ArrayList<String>();

        Object valueFromNode;
        final String attributeName = analysisParameter.getName();
        if (analysisParameter.isAssociatedArtefactAttribute() && AnalysisAttributeHelper.isOrgUnitAttribute(attributeName)) {

            final String[] attributes = AnalysisAttributeHelper.splitAttributeNames(attributeName);

            // add value for each one to collection of values to return
            final List associatedNodes = AnalysisAttributeHelper.getAssociatedNodes(node, attributes);
            for (int i = 0; i < associatedNodes.size(); i++) {
                Node associatedNode = (Node) associatedNodes.get(i);
                valueFromNode = AnalysisAttributeHelper.evaluateProperty(attributes[attributes.length - 1], associatedNode);
                values.add(formatValue(valueFromNode));
            }
        } else {

            if (analysisParameter.isQuestionnaireAttribute()) {
                valueFromNode = AnalysisAttributeHelper.findAnswer(questionnaireAnswers, node.getId(), analysisParameter);
            } else if (analysisParameter.isDynamicAttribute()) {
                valueFromNode = AnalysisAttributeHelper.findAttributeValue(analysisParameter, node);
            } else if (analysisParameter.isDerivedAttribute()) {
                valueFromNode = AnalysisAttributeHelper.getDerivedAttributeValue(analysisParameter.getName(), node);
            } else {
                valueFromNode = AnalysisAttributeHelper.getCoreAttributeValue(analysisParameter.getName(), node);
            }

            values.add(formatValue(valueFromNode));
        }

        if (values.isEmpty()) {
            values.add(ReportConstants.NA);
        }

        return values;
    }

    private String formatValue(Object valueFromNode) {
        return (valueFromNode != null ? DataFormatter.formatValue(valueFromNode) : ReportConstants.NA);
    }

    private String getVerticalGroupingAttribute() {
        return this.groupedAttributeNames[0];
    }

    private String getHorizontalGroupingAttribute() {
        return groupedAttributeNames[1];
    }

    /**
     * Builds results.
     * <br/> Builds Map that uses permutations of horizontal and vertical headings as keys and has a list as the value.
     * <br/> The nodes get added to the list based on the values they have for the vertical and horizontal attributes.
     */
    protected void init() {

        try {

            // first build Map with one entry for one each combination of the heading label and the row label
            final Map<CrossTableKey, List<Node>> temp = new LinkedHashMap<CrossTableKey, List<Node>>();
            for (Iterator iterator = rowHeaders.iterator(); iterator.hasNext();) {
                final Row row = (Row) iterator.next();

                for (Iterator columnIterator = columnHeaders.iterator(); columnIterator.hasNext();) {
                    final Heading heading = (Heading) columnIterator.next();

                    final CrossTableKey key = new CrossTableKey(row.getHeading().getLabel(), heading.getLabel());
                    temp.put(key, new ArrayList<Node>());
                }
            }

            final AnalysisParameter verticalGroupingAttribute = AnalysisAttributeHelper.getAttributeFromName(getVerticalGroupingAttribute());
            final AnalysisParameter horizontalGroupingAttribute = AnalysisAttributeHelper.getAttributeFromName(getHorizontalGroupingAttribute());

            // iterate the nodes and put them in the correct List based on the values of the horizontal and vertical attributes
            for (Iterator iterator = records.iterator(); iterator.hasNext();) {
                final Node node = (Node) iterator.next();

                // get all values
                final Collection verticalGroupingValues = getGroupingColumnValue(verticalGroupingAttribute, node);
                final Collection horizontalGroupingValues = getGroupingColumnValue(horizontalGroupingAttribute, node);

                for (Iterator verticalGroupingValuesIterator = verticalGroupingValues.iterator(); verticalGroupingValuesIterator.hasNext();) {
                    final String verticalGroupingValue = (String) verticalGroupingValuesIterator.next();

                    for (Iterator horizontalGroupingValuesIterator = horizontalGroupingValues.iterator(); horizontalGroupingValuesIterator.hasNext();) {
                        final String horizontalGroupingValue = (String) horizontalGroupingValuesIterator.next();

                        final CrossTableKey key = new CrossTableKey(verticalGroupingValue, horizontalGroupingValue);
                        final List<Node> list = temp.get(key);
                        if (list != null) {
                            list.add(node);
                        }
                    }
                }
            }

            recordIterator = temp.entrySet().iterator();

        } catch (Exception e) {
            throw new TalentStudioRuntimeException(e);
        }
    }

    /**
     * Collection of rows.
     */
    private final Collection rowHeaders;

    /**
     * Collection of Headings.
     */
    private final Collection columnHeaders;

    /**
     * Array of Strings containg the grouped attribute labels (size is always 2, and the first one is the vertical grouping attribute.
     */
    private final String[] groupedAttributeNames;

    /**
     * Iterator for results within a given row (the recorditerator iterates the headings - this iterator iterates the actual Nodes.)
     */
    private Iterator currentResults;

    /**
     * The current cross table key.
     */
    private CrossTableKey currentKey;
}
