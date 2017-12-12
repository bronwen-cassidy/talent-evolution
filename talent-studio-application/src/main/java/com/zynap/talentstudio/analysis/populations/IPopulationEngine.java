package com.zynap.talentstudio.analysis.populations;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.Page;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.positions.PositionDto;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: jsueiras
 * Date: 23-Feb-2005
 * Time: 14:08:19
 */
public interface IPopulationEngine {

    List<? extends Node> find(Population population, Long userId) throws TalentStudioException;

    List<SubjectDTO> findSubjects(Population population, Long userId) throws TalentStudioException;

    List<PositionDto> findPositions(Population population, Long userId) throws TalentStudioException;

    Page find(Population population, Long userId, int start, int end, int numRecords) throws TalentStudioException;

    Map findCrossTab(Population population, Metric metric, AnalysisParameter rowAtt, AnalysisParameter columnAtt, Long userId) throws TalentStudioException;

    List<NodeExtendedAttribute> findQuestionnaireAnswers(Population population, Long userId, List<AnalysisParameter> attributes) throws TalentStudioException;

    Map findMetrics(Population population, Collection metrics, AnalysisParameter groupingAttribute, Long userId) throws TalentStudioException;

    Map<Long, QuestionAttributeValuesCollection> findQuestionnaireAnswers(List<AnalysisParameter> attributes, Population population, Long userId) throws TalentStudioException;

    Map<Long, QuestionAttributeValuesCollection> findQuestionnaireAnswers(List<AnalysisParameter> attributes, List<Long> nodeIds, Long userId) throws TalentStudioException;
    
    Map<Long, QuestionAttributeValuesCollection> findPersonalQuestionnaireAnswers(List<AnalysisParameter> attributes, Node node, Long userId) throws TalentStudioException;

    List<NodeExtendedAttribute> findPersonalQuestionnaireAttributeAnswers(List<AnalysisParameter> attributes, Node node, Long userId) throws TalentStudioException;

	List<NodeExtendedAttribute> findPersonalQuestionnaireAttributeAnswers(List<AnalysisParameter> attributes, List<Long> workflowIds, Node node) throws TalentStudioException;
    
    Population getAllPositionsPopulation();

    Population getAllSubjectsPopulation();

    final String EQ = " = ";
    final String GT = " > ";
    final String LT = " < ";
    final String LIKE = " LIKE ";
    final String ISNULL = " IS NULL ";


    final String OR = " OR ";
    final String AND = " AND ";

    final String NOT = " NOT ";

    final String COUNT = "count";
    final String COUNT_LABEL = "Count";
    final Long COUNT_METRIC_ID = new Long(-1);
    final Metric COUNT_METRIC = new Metric(COUNT_METRIC_ID, COUNT_LABEL, "Counts the number of occurances", AccessType.PUBLIC_ACCESS.toString(), COUNT, "id");
    final String AVG = "avg";
    final String SUM = "sum";

    final String[] availableOperators = new String[]{AND, OR};

    final String S_P_ASSOC_TYPE_ = "SA";
    final String P_P_ASSOC_TYPE_ = "PA";

    final String P_POS_TYPE_ = Node.POSITION_UNIT_TYPE_;
    final String P_SUB_TYPE_ = Node.SUBJECT_UNIT_TYPE_;
    final String O_UNIT_TYPE_ = Node.ORG_UNIT_TYPE_;
    final String P_ITEM_TYPE_ = "PI";

    final String BRCKT_TYPE_ = ")";
    final String OP_TYPE_ = "O";

    final String LEFT_BRCKT_ = "(";
    final String RIGHT_BRCKT_ = ")";

    // derived attribute method names
    String SOURCE_DA = "sourceDerivedAttributes";
    String TARGET_DA = "targetDerivedAttributes";

    // association property names
    String SUBJECT_PRIMARY_ASSOCIATIONS_ATTR = "subjectPrimaryAssociations";
    String SUBJECT_SECONDARY_ASSOCIATIONS_ATTR = "subjectSecondaryAssociations";
    String SUBJECT_ASSOCIATION_ATTR = "subjectAssociations";
    String SOURCE_ASSOCIATIONS_ATTR = "sourceAssociations";
    String TARGET_ASSOCIATIONS_ATTR = "targetAssociations";

    /* a persons organisation unit */
    String PERSONS_ORGUNIT_ATTRIBUTE_NAME = "subjectPrimaryAssociations.position.organisationUnit.label";
    String PERSONS_ORGUNIT_ATTRIBUTE = "subjectPrimaryAssociations.position.organisationUnit.id";

    // association field constants
    String SOURCE_ATTR = "source";
    String PARENT_ATTR = "parent";
    String CHILDREN_ATTR = "children";
    String TARGET_ATTR = "target";
    String SUBJECT_ATTR = "subject";
    String POSITION_ATTR = "position";

    // association property names lists
    String[] ASSOCIATED_ARTEFACT_DA = {SUBJECT_PRIMARY_ASSOCIATIONS_ATTR, SUBJECT_SECONDARY_ASSOCIATIONS_ATTR, SUBJECT_ASSOCIATION_ATTR, SOURCE_ASSOCIATIONS_ATTR, TARGET_ASSOCIATIONS_ATTR, CHILDREN_ATTR};
    String[] RELATED_ARTEFACT_PROPERTIES_NAMES = {POSITION_ATTR, SUBJECT_ATTR, TARGET_ATTR, SOURCE_ATTR, PARENT_ATTR};
    List<String> RELATED_ARTEFACT_PROPERTIES_NAMES_LIST = new ArrayList<String>(Arrays.asList(RELATED_ARTEFACT_PROPERTIES_NAMES));
    List<String> ASSOCIATION_PROPERTIES_NAMES_LIST = new ArrayList<String>(Arrays.asList(ASSOCIATED_ARTEFACT_DA));

    String _CROSS_TAB_TOTAL = "_TOTAL_";
    char[] formulaTokens = new char[]{'+', '-', '*', '/', '(', ')', ' '};
    char[] operators = new char[]{'+', '-', '*', '/'};

    public final Long ALL_POSITIONS_POPULATION_ID = new Long(-1);
    public final Long ALL_PEOPLE_POPULATION_ID = new Long(-2);
}
