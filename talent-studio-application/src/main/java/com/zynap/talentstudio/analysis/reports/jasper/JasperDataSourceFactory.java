package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRDataSource;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.subjects.Subject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Class that provides a facade for reports to build data sources and to run queries using the population engine.
 *
 * User: jsueiras
 * Date: 24-Jan-2006
 * Time: 15:47:18
 */
public class JasperDataSourceFactory {

    /**
     * Method used with sub reports in tabular reports - see TabularReportDesigner.getSubReportDataSourceExpression(...)
     * @param report the report that is being run
     * @param nodes the list of nodes included in the report
     * @param user the logged in user (used to determine permissions to view)
     * @return AssociatedArtefactDataSource
     * @throws com.zynap.exception.TalentStudioException if any errors
     */
    public JRDataSource getAssociatedArtefactDataSource(Report report, Collection<Object> nodes, User user) throws TalentStudioException {
        return new AssociatedArtefactDataSource(report, nodes, this, user);
    }

    public JRDataSource getDocumentDataSource(Report report, Collection<Object> portfolioItems, User user) throws TalentStudioException {
        return new DocumentDataSource(report, portfolioItems, this, user);
    }

    /**
     * Get tabular report data source.
     *
     * @param report the report being run
     * @param nodes  the nodes representing the result
     * @param questionnaireAnswers the answers to any question columns
     * @param groupingAttributes the names of the grouping columns
     * @param orderAttributeName the attribute to order by
     * @param orderBy asc or desc
     * @param user the user required for security purposes
     * @return JRCollectionDataSource
     */
    public JRDataSource getDataSource(Report report, Collection<Object> nodes, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, List<GroupingAttribute> groupingAttributes, String orderAttributeName, int orderBy, User user) {
        return new JRCollectionDataSource(report, nodes, questionnaireAnswers, this, groupingAttributes, orderAttributeName, orderBy, user);
    }

    public JRDataSource getCSVDataSource(Report report, Collection<Object> nodes, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, List<GroupingAttribute> groupingAttributes, String orderAttributeName, int orderBy, User user) {
        return new CSVReportDataSource(report, nodes, questionnaireAnswers, this, groupingAttributes, orderAttributeName, orderBy, user);
    }

    /**
     * Get cross tab report data source.
     *
     * @param report the report being run
     * @param nodes  the results included in the report
     * @param questionnaireAnswers the ansers to any questionnaire questions
     * @param groupedAttributeNames the names of the grouping attributes
     * @param rowHeaders the list of headers for the row
     * @param columnHeaders the list of headers for the column
     * @param user the user for security purposes
     * @return JasperCrossTabDataSource
     */
    public JRDataSource getCrossTabDataSource(Report report, Collection nodes, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, String[] groupedAttributeNames, Collection rowHeaders, Collection columnHeaders, User user) {
        return new JasperCrossTabDataSource(report, nodes, questionnaireAnswers, this, groupedAttributeNames, rowHeaders, columnHeaders, user);
    }

    public JRDataSource getProgressDataSource(Report report, Collection workflows, Map<Long, QuestionAttributeValuesCollection> questionnaireAnswers, Subject subject, User user) {
        return new ProgressReportDataSource(report, workflows, questionnaireAnswers, subject, this, user);
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public String getNodeLabel(String nodeId) {
        return dynamicAttributeService.getNodeLabel(nodeId);
    }

    public String getLookupValueLabel(String value) throws TalentStudioException {
        return lookupManager.findLookupValue(new Long(value)).getLabel();
    }

    public IPopulationEngine getPopulationEngine() {
        return populationEngine;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public Collection<DynamicAttribute> getAllActiveAttributes(String nodeType) {
        
        if(Node.POSITION_UNIT_TYPE_.equals(nodeType)) {
            if(positionAttributes == null) {
                positionAttributes = getAttributes(nodeType);
            }
            return positionAttributes;
        } else {
            if(subjectAttributes == null) {
                subjectAttributes = getAttributes(nodeType);
            }
            return subjectAttributes;
        }
    }

    private Collection<DynamicAttribute> getAttributes(String nodeType) {
        return dynamicAttributeService.getAllActiveAttributes(nodeType, true);
    }

    public void refreshAttributes() {
        positionAttributes = null;
        subjectAttributes = null;        
    }

    ILookupManager lookupManager;
    IDynamicAttributeService dynamicAttributeService;
    IPopulationEngine populationEngine;

    private Collection<DynamicAttribute> positionAttributes;
    private Collection<DynamicAttribute> subjectAttributes;

}
