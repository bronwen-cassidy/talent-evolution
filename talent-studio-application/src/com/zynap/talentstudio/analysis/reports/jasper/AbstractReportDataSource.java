package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.DataFormatter;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.crosstab.ArtefactAttributeViewFormatter;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * User: amark.
 * Date: 17-May-2006
 * Time: 12:36:33
 */
public abstract class AbstractReportDataSource implements JRRewindableDataSource {

    protected AbstractReportDataSource(Report report, Collection<Object> nodes, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, JasperDataSourceFactory factory, User currentUser) {
        this.records = new ArrayList<Object>(nodes);
        this.questionnaireAnswers = questionnaireAnswers;
        this.currentUser = currentUser;
        this.report = report;
        this.factory = factory;
        this.factory.refreshAttributes();
    }

    public final void moveFirst() {
        init();
    }

    /**
     * To be overriden by subclasses - prepares record iterator, etc.
     */
    protected abstract void init();

    /**
     * Set currentQuestionCollection to null;
     * <br/> Set maxDynamicPosition to null.
     * <br/> Also reset currentDynamicPosition to zero.
     */
    protected final void clearCurrentAnswers() {
        currentQuestionCollection = null;
        maxDynamicPosition = null;
        currentDynamicPosition = 0;
    }

    /**
     * Find current answers (if dealing with a Node and we have questionnaire answers.)
     * <br/> Set maxDynamicPosition to a value or null.
     * <br/> Also reset currentDynamicPosition to zero.
     *
     * @param currentRecord the rocord that is currently being considered
     */
    protected void getCurrentAnswers(Object currentRecord) {

        currentQuestionCollection = null;

        if (currentRecord instanceof Node && questionnaireAnswers != null) {
            final Node node = (Node) currentRecord;
            currentQuestionCollection = questionnaireAnswers.get(node.getId());
        }

        // set max dynamic position (can be null) and reset current dynamic position
        maxDynamicPosition = currentQuestionCollection != null ? currentQuestionCollection.getMaxDynamicPosition() : null;
        currentDynamicPosition = 0;
        if (report.isLastLineItem() && maxDynamicPosition != null) {
            currentDynamicPosition = maxDynamicPosition.intValue();
        }
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
    protected Object getValueFromNode(AnalysisParameter analysisParameter, Node currentNode) {

        Object value = DataFormatter.EMPTY_VALUE;

        // if dynamic position is zero or is grouped column must return non line item values this prevents duplications of data so when 2 rows
        // of dynamicLineItem values the coreDetails Information will not be repeated
        boolean displayNonLineItemValues = (currentDynamicPosition == 0 || isGrouped(analysisParameter) || report.isLastLineItem() || analysisParameter.isIdField());
        boolean notDisplayImageCell = (currentDynamicPosition > 0 && isImageDisplayable(analysisParameter));
        // when repeating rows of grouped data as in the case of line items we do not want the 'grouped' value displayed as a '-' as the '-'
        // represents no information available, in this situation, this is not the case
        if (!displayNonLineItemValues) value = "  ";

        try {
            if (analysisParameter.isQuestionnaireAttribute()) {
                value = getQuestionnaireValue(analysisParameter, displayNonLineItemValues);
            } else if (displayNonLineItemValues && analysisParameter.isDynamicAttribute()) {
                value = getDynamicAttributeValue(analysisParameter, currentNode);
            } else if (displayNonLineItemValues && analysisParameter.isDerivedAttribute()) {
                value = AnalysisAttributeHelper.getDerivedAttributeValue(analysisParameter.getName(), currentNode);
            } else if (analysisParameter.isAssociatedArtefactAttribute()) {
                // probably a grouped organisation unit let us try and return a collection and see where we go from there
                value = getAssociatedArtefactAttributeValues(currentNode, getColumn(analysisParameter));
            } else if (displayNonLineItemValues) value = AnalysisAttributeHelper.getCoreAttributeValue(analysisParameter.getName(), currentNode);

        } catch (Exception e) {
            throw new TalentStudioRuntimeException(e);
        }

        return notDisplayImageCell ? null : value;
    }

    private Object getQuestionnaireValue(AnalysisParameter analysisParameter, boolean displayNonLineItemValues) {
        // get value always
        // if dynamic line item return appropriate value based on currentDynamicPosition
        // if not dynamic line item but currentDynamicPosition is zero or column is grouped return value always
        // otherwise value is left as DataFormatter.EMPTY_VALUE.
        Object value = null;
        if (!displayNonLineItemValues) value = "  ";

        AttributeValue attributeValue = AnalysisAttributeHelper.findAnswer(analysisParameter, currentQuestionCollection);
        if (attributeValue != null) {
            final boolean dynamic = attributeValue.getDynamicAttribute().isDynamic();
            if (dynamic) {
                final NodeExtendedAttribute nodeExtendedAttribute;
                if (report.isLastLineItem()) {
                    nodeExtendedAttribute = attributeValue.getAttributeAtLastDynamicPosition();
                } else {
                    nodeExtendedAttribute = attributeValue.getAttributeAtDynamicPosition(currentDynamicPosition);
                }
                if (nodeExtendedAttribute == null) {
                    
                   value = currentQuestionCollection.isParticipant(analysisParameter.getQuestionnaireWorkflowId()) ? ReportConstants.BLANK_VALUE : ReportConstants.NO_VALUE;
                } else {
                    value = ArtefactAttributeViewFormatter.formatValue(nodeExtendedAttribute, null);
                }
            } else if (displayNonLineItemValues) {
                value = attributeValue;
            }
        } else {
            value = currentQuestionCollection != null && currentQuestionCollection.isParticipant(analysisParameter.getQuestionnaireWorkflowId()) ? ReportConstants.BLANK_VALUE : ReportConstants.NO_VALUE;
        }

        return value;
    }

    private boolean isImageDisplayable(AnalysisParameter analysisParameter) {
        Column column = getColumn(analysisParameter);
        return column != null && column.isColorDisplayable();
    }

    protected String getAssociatedArtefactAttributeValues(Node node, Column column) {
        String attributeName = column.getAttributeName();
        String[] values = AnalysisAttributeHelper.splitAttributeNames(attributeName);
        return getAssociatedArtefactAttributeCells(node, values);
    }

    private String getAssociatedArtefactAttributeCells(Node targetNode, String[] values) {

        List associatedNodes = AnalysisAttributeHelper.getAssociatedNodes(targetNode, values);
        Set<Object> nestedData = new LinkedHashSet<Object>();
        StringBuffer nestedCells = new StringBuffer();

        for (int i = 0; i < associatedNodes.size(); i++) {
            Node associatedNode = (Node) associatedNodes.get(i);
            String attributeName = values[2];
            final int arrayLength = values.length - 2;
            if (arrayLength > 1) {
                String[] newValues = new String[arrayLength];
                System.arraycopy(values, 2, newValues, 0, arrayLength);
                attributeName = StringUtils.arrayToDelimitedString(newValues, ".");
            }
            final String result = AnalysisAttributeHelper.evaluateProperty(attributeName, associatedNode);
            nestedData.add(result);
        }

        int i = 0;
        for (Object result : nestedData) {
            if (i > 0 && i < associatedNodes.size()) nestedCells.append(", ");
            nestedCells.append(result);
            i++;
        }

        return nestedCells.toString();
    }

    protected Object getDynamicAttributeValue(AnalysisParameter analysisParameter, Node currentNode) {

        Object value;
        String nodeType = currentNode.getNodeType();
        Collection<DynamicAttribute> attributes = factory.getAllActiveAttributes(nodeType);
        DynamicAttribute dynamicAttribute = AnalysisAttributeHelper.findDynamicAttribute(analysisParameter, attributes);
        if (dynamicAttribute == null) {
            factory.refreshAttributes();
            attributes = factory.getAllActiveAttributes(nodeType);
            dynamicAttribute = AnalysisAttributeHelper.findDynamicAttribute(analysisParameter, attributes);
        }
        if (dynamicAttribute != null && dynamicAttribute.isCalculated()) {
            value = dynamicAttribute.getCalculation().execute(currentNode).getValue();
        } else {
            value = AnalysisAttributeHelper.findAttributeValue(analysisParameter, currentNode);
        }

        return value;
    }

    /**
     * Get field value.
     * <br/> If field points to a Collection of associated artefacts will return a Collection.
     * <br/> Otherwise will return an Object ( the object may be an Integer or an AttibuteValue or a String depending on the analysisParameter type.)
     *
     * @param field             the field containing the name of the attribute to find the answer for
     * @param analysisParameter the parameter object which holds questionnaire id informaiton
     * @param currentNode       the current node being evaluated
     * @return Object or null
     */
    protected final Object getFieldValue(JRField field, AnalysisParameter analysisParameter, Node currentNode) {

        Object value;

        // important - handles loading of associated artefact collections for sub reports
        if (Collection.class.isAssignableFrom(field.getValueClass())) {
            value = getAssociatedArtefacts(analysisParameter, currentNode);

        } else if (analysisParameter.isOrganisationUnitAttribute()) {
            // get the orgunit and create a new analysis parameter
            try {
                final String orgunitPrefix = AnalysisAttributeHelper.getOrganisationUnitPrefix(analysisParameter.getName());
                currentNode = (Node) PropertyUtils.getProperty(currentNode, orgunitPrefix);
                // to get the new suffix we need substring the old suffix
                String newSuffix = analysisParameter.getName().substring(orgunitPrefix.length() + 1);
                analysisParameter = AnalysisAttributeHelper.getAttributeFromName(newSuffix);
                value = getValueFromNode(analysisParameter, currentNode);
            } catch (Exception e) {
                value = null;
            }
        } else {
            value = getValueFromNode(analysisParameter, currentNode);
        }

        return value;
    }

    /**
     * Check maxDynamicPosition vs. currentDynamicPosition.
     * <br/> used when iterating nodes with dynamic line item answers to ensure that the datasource does not move to the next
     * record until we have displayed all values for the dynamic line items.
     * <br/> Will return true if maxDynamicPosition is null (ie: there are no dynamic line items) or currentDynamicPosition == maxDynamicPosition.
     *
     * @return true or false
     */
    protected final boolean checkMaxDynamicPosition() {

        // go to next record is true by default
        boolean goToNextRecord = true;

        // do not go to next record if max dynamic position is not null and current position is less than max dynamic position
        if (!report.isLastLineItem()) {
            if (maxDynamicPosition != null && currentDynamicPosition < maxDynamicPosition.intValue()) {
                currentDynamicPosition++;
                goToNextRecord = false;
            }
        }

        return goToNextRecord;
    }

    /**
     * Get associated artefacts from Node based on specified property.
     * <br/> used when build up AssociatedArtefactDataSources - see TabularReportDesigner.addSubReport(...)
     * <br/> Also makes sure that has access is set to true or false as appropriate.
     *
     * @param attribute the attribute to get the answer for
     * @param node      the node being evaluated
     * @return Collection
     */
    private Collection getAssociatedArtefacts(AnalysisParameter attribute, Node node) {
        Collection value = null;
        try {
            value = (Collection) PropertyUtils.getProperty(node, attribute.getName());
        } catch (Exception e) {
            logger.error("Failed to get asociated artefacts for attribute " + attribute.toString() + " because of:", e);
        }

        return value;
    }

    /**
     * Find column that corresponds to analysis parameter and check if it is grouped.
     *
     * @param analysisParameter holds the attribute name the column represents
     * @return true if the column is grouped, false otherwise
     */
    private boolean isGrouped(AnalysisParameter analysisParameter) {

        final Column column = getColumn(analysisParameter);
        return column != null && column.isGrouped();
    }

    /**
     * Find column for analysisParameter.
     *
     * @param analysisParameter the parameter containing the name of the attribute the questionnaire it belongs to (if any) and the role (if any)
     * @return Column or null
     */
    protected Column getColumn(AnalysisParameter analysisParameter) {

        final List columns = report.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            final Column column = (Column) columns.get(i);
            if (AnalysisAttributeHelper.getName(analysisParameter).equals(AnalysisAttributeHelper.getName(column.getAnalysisParameter()))) {
                return column;
            }
        }

        return null;
    }

    /**
     * Logger.
     */
    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Iterator for records.
     */
    protected Iterator recordIterator;

    /**
     * The report.
     */
    protected final Report report;

    /**
     * List of records - will either be a Node or an ArtefactAssociation.
     */
    protected final List<Object> records;

    /**
     * Current record - will either be a Node or an ArtefactAssociation.
     */
    protected Object currentRecord;

    /**
     * The user running the report.
     */
    protected final User currentUser;

    /**
     * Facade used to access population engine.
     */
    protected final JasperDataSourceFactory factory;

    /**
     * The Map of QuestionAttributeValuesCollections keyed on node id (defaults must be null to ensure that AssociatedArtefactDataSource knows when to load data).
     */
    protected Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers;

    /**
     * Max position for dynamic line items (can be null.)
     */
    protected Integer maxDynamicPosition;

    /**
     * Current set of questionnaire answers (can be null.)
     */
    protected QuestionAttributeValuesCollection currentQuestionCollection;

    /**
     * Current position (used to keep track of iteration if dynamic line items.)
     */
    protected int currentDynamicPosition;
}
