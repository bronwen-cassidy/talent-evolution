package com.zynap.talentstudio.analysis.populations;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 24-Feb-2005
 * Time: 11:01:16
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.TestAnalysisService;
import com.zynap.talentstudio.analysis.Page;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TestHibernatePopulationEngine extends AbstractHibernateTestCase {

    public void testFindPositions() throws Exception {
        final List<? extends Position> positions = populationEngine.findPositions(populationEngine.getAllPositionsPopulation(), new Long(1));
        assertNotNull(positions);
    }

    public void testFindSubjects() throws Exception {
        final Population allSubjectsPopulation = populationEngine.getAllSubjectsPopulation();
        final List<? extends Subject> subjects = populationEngine.findSubjects(allSubjectsPopulation, new Long(1));
        assertNotNull(subjects);
    }

    public void testFindPersonalQuestionnaireAnswers() throws Exception {

        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();

        attributes.add(new AnalysisParameter("76", new Long(7), null));
        attributes.add(new AnalysisParameter("77", new Long(7), null));

        final Map list = populationEngine.findPersonalQuestionnaireAnswers(attributes, DEFAULT_POSITION, ROOT_USER_ID);
        assertNotNull(list);
    }

    public void testFindQuestionnairesCompoundPopulation() throws Exception {
        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();
        attributes.add(new AnalysisParameter("76", new Long(7), null));
        attributes.add(new AnalysisParameter("77", new Long(7), null));
        final Population leftPopulation = populationEngine.getAllSubjectsPopulation();
        final List lookupValues = lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC);
        addSubjectAssociationCriteria(leftPopulation, lookupValues, null);
        Map list = populationEngine.findQuestionnaireAnswers(attributes, leftPopulation, ROOT_USER_ID);
        assertNotNull(list);
    }

    public void testFindQuestionnaireAnswers() throws Exception {

        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();

        attributes.add(new AnalysisParameter("76", new Long(7), null));
        attributes.add(new AnalysisParameter("77", new Long(7), null));

        Population population = populationEngine.getAllSubjectsPopulation();
        Map list = populationEngine.findQuestionnaireAnswers(attributes, population, ROOT_USER_ID);
        assertNotNull(list);
    }

    public void testFindQuestionnairesByIds() throws Exception {
        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();
        List<Long> nodeIds = new ArrayList<Long>();
        nodeIds.add(new Long(1));
        nodeIds.add(new Long(141));
        attributes.add(new AnalysisParameter("207", new Long(21), null));
        attributes.add(new AnalysisParameter("77", new Long(7), null));
        Map list = populationEngine.findQuestionnaireAnswers(attributes, nodeIds, ROOT_USER_ID);
        assertNotNull(list);
    }

    public void testAllSubjects() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        Collection c = populationEngine.find(population, new Long(0));
        assertTrue(c != null);
    }

    public void testAllSubjectsMaxResults() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        Page c = populationEngine.find(population, new Long(0), 0, 10, -1);
        // must be 10
        Page b = populationEngine.find(population, new Long(0), 10, 20, -1);
        assertTrue(c.getResults() != null);
        assertTrue(b.getResults() != null);
    }

    public void testTitleNullCriteria() throws Exception {
        Population population = new Population();
        population.setLabel("Population Test");
        population.setType(IPopulationEngine.P_POS_TYPE_);
        population.setUserId(ROOT_USER_ID);
        population.setScope(AccessType.PRIVATE_ACCESS.toString());

        PopulationCriteria populationCriteria = new PopulationCriteria();
        populationCriteria.setAttributeName("title");
        populationCriteria.setComparator(IPopulationEngine.ISNULL);
        populationCriteria.setRefValue(null);
        population.addPopulationCriteria(populationCriteria);

        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        // find using pagination
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testLanguageNullCriteria() throws Exception {

        Population population = TestAnalysisService.createPopulationAllPositions();
        addNullLanguageAsFirstCriteria(population);
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
        assertTrue(c != null);
    }

    public void testQuestionNullCriteria() throws Exception {

        Population population = TestAnalysisService.createPopulationAllSubjects();
        addQuestionAsFirstCriteria(population, null);
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testQuestionNotNullCriteria() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        addQuestionAsFirstCriteria(population, "val");
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testFindUserNullCriteria() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        population.addPopulationCriteria(createUserNullCriteria());
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testQuestionWithRole() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        PopulationCriteria populationCriteria = addQuestionAsFirstCriteria(population, "val");
        populationCriteria.setRole("Peer");
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testAllPositions() throws Exception {
        Population population = TestAnalysisService.createPopulationAllPositions();
        Collection c = populationEngine.find(population, new Long(0));
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testRun() throws Exception {
        Population population = TestAnalysisService.createPopulation();
        addLanguageAsCriteria(population);
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testUpperCase() throws Exception {
        Population population = TestAnalysisService.createPopulation();
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
    }

    public void testRunAssociated() throws Exception {
        Population population = TestAnalysisService.createPopulation();
        PopulationCriteria assoc = createAssocPosPopulationCriteria(IPopulationEngine.AND, IPopulationEngine.SUBJECT_ASSOCIATION_ATTR);
        population.addPopulationCriteria(assoc);
        addLanguageAsCriteria(population);
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testRunAssociatedInverse() throws Exception {
        Population population = TestAnalysisService.createPopulationAllPositions();
        PopulationCriteria cr = new PopulationCriteria(null, "O", "0", "targetDerivedAttributes[20]", null, " > ", population);
        population.addPopulationCriteria(cr);
        PopulationCriteria assPop = createAssocPosPopulationCriteria(IPopulationEngine.AND, IPopulationEngine.TARGET_ASSOCIATIONS_ATTR);
        population.addPopulationCriteria(assPop);
        PopulationCriteria titleCriteria = getTitleAsCriteria(IPopulationEngine.AND, "targetAssociations.source");
        population.addPopulationCriteria(titleCriteria);
        Collection c = populationEngine.find(population, ROOT_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, -1);
        assertTrue(page.getResults() != null);
    }

    public void testNotSubjectPositionAssociation() throws Exception {
        Population group = TestAnalysisService.createPopulationAllSubjects();
        PopulationCriteria criteria = new PopulationCriteria(null, "O", "subjectAssociations.position.402", "42", IPopulationEngine.NOT, IPopulationEngine.EQ, group);
        group.addPopulationCriteria(criteria);
        Collection list = populationEngine.find(group, ADMINISTRATOR_USER_ID);
        assertTrue(list != null);
        Page page = populationEngine.find(group, ADMINISTRATOR_USER_ID, 0, 10, numRecords);
        assertTrue(page.getResults() != null);
    }

    public void testFindAllPositions() throws Exception {
        Population population = TestAnalysisService.createPopulationAllPositions();
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
    }

    public void testFindAllSubjects() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
    }

    public void testFindSubjectsInOrgUnit() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        addOrgUnitAsFirstCriteria(population);
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, numRecords);
        assertTrue(page.getResults() != null);
    }

    public void testFindSubjectsByPosTitle() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        addJobTitleFirstCriteria(population);
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, numRecords);
        assertTrue(page.getResults() != null);
    }

    public void testFindAssocPositions() throws Exception {
        Population population = TestAnalysisService.createPopulationAllPositions();
        PopulationCriteria assoc = createAssocPosPopulationCriteria(null, IPopulationEngine.SOURCE_ASSOCIATIONS_ATTR);
        population.addPopulationCriteria(assoc);
        Collection list2 = populationEngine.find(population, ROOT_USER_ID);
        assertTrue(list2 != null);
        Page page = populationEngine.find(population, ROOT_USER_ID, 0, 10, numRecords);
        assertTrue(page.getResults() != null);
    }

    public void testFindSubjectsByFirstName() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setAttributeName(AnalysisAttributeHelper.FIRST_NAME_ATTR);
        criteria.setComparator(IPopulationEngine.LIKE);
        criteria.setRefValue("d");
        population.addPopulationCriteria(criteria);
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, numRecords);
        assertTrue(page.getResults() != null);
    }

    public void testFindSubjectsByDateOfBirth() throws Exception {

        Population population = TestAnalysisService.createPopulationAllSubjects();
        addDateOfBirthFirstCriteria(population);
        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, numRecords);
        assertTrue(page.getResults() != null);
    }

    public void testFindPage() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();
        final Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 25, numRecords);
        assertNotNull(page);
        assertEquals(new Integer(0), page.getNumRecords());
    }

    /**
     * Test null comparator - deliberately sets ref value with null comparator to check that it is ignored when the query is built.
     *
     * @throws Exception
     */
    public void testCriteriaWithNullComparator() throws Exception {

        Population population = TestAnalysisService.createPopulationAllPositions();

        final PopulationCriteria nullCoreAttributeCriteria = new PopulationCriteria();
        nullCoreAttributeCriteria.setAttributeName(AnalysisAttributeHelper.POSITION_TITLE_ATTR);
        nullCoreAttributeCriteria.setComparator(IPopulationEngine.ISNULL);
        nullCoreAttributeCriteria.setRefValue("angus");
        population.addPopulationCriteria(nullCoreAttributeCriteria);

        final PopulationCriteria nullExtendedAttributeCriteria = new PopulationCriteria();
        nullExtendedAttributeCriteria.setOperator(IPopulationEngine.AND);
        nullExtendedAttributeCriteria.setAttributeName("38");
        nullExtendedAttributeCriteria.setComparator(IPopulationEngine.ISNULL);
        nullExtendedAttributeCriteria.setRefValue("1");
        population.addPopulationCriteria(nullExtendedAttributeCriteria);

        final PopulationCriteria nullDerivedAttributeCriteria = new PopulationCriteria();
        nullDerivedAttributeCriteria.setOperator(IPopulationEngine.AND);
        nullDerivedAttributeCriteria.setAttributeName("sourceDerivedAttributes[20]");
        nullDerivedAttributeCriteria.setComparator(IPopulationEngine.ISNULL);
        nullDerivedAttributeCriteria.setRefValue("1");
        population.addPopulationCriteria(nullDerivedAttributeCriteria);

        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, numRecords);
        assertTrue(page.getResults() != null);
    }

    public void testCriteriaWithSpecialCharacters() throws Exception {

        Population population = TestAnalysisService.createPopulationAllSubjects();

        final PopulationCriteria secondNameCriteria = new PopulationCriteria();
        secondNameCriteria.setAttributeName(AnalysisAttributeHelper.SECOND_NAME_ATTR);
        secondNameCriteria.setComparator(IPopulationEngine.EQ);
        secondNameCriteria.setRefValue("O'Grady");
        population.addPopulationCriteria(secondNameCriteria);

        final PopulationCriteria aristoPrefixCriteria = new PopulationCriteria();
        aristoPrefixCriteria.setOperator(IPopulationEngine.AND);
        aristoPrefixCriteria.setAttributeName("1");
        aristoPrefixCriteria.setComparator(IPopulationEngine.EQ);
        aristoPrefixCriteria.setRefValue("Lord O'Grady");
        population.addPopulationCriteria(aristoPrefixCriteria);

        Collection c = populationEngine.find(population, ADMINISTRATOR_USER_ID);
        assertTrue(c != null);
        Page page = populationEngine.find(population, ADMINISTRATOR_USER_ID, 0, 10, numRecords);
        assertTrue(page.getResults() != null);
    }

    private void addSubjectAssociationCriteria(Population population, List lookupValues, String firstOperator) {
        for (int i = 0; i < lookupValues.size(); i++) {
            LookupValue lookupValue = (LookupValue) lookupValues.get(i);
            PopulationCriteria criteria = new PopulationCriteria();
            criteria.setAttributeName("subjectAssociations.qualifier.id");
            criteria.setComparator(IPopulationEngine.EQ);
            criteria.setRefValue(lookupValue.getId().toString());
            if (i > 0)
                criteria.setOperator(IPopulationEngine.OR);
            else
                criteria.setOperator(firstOperator);
            population.addPopulationCriteria(criteria);
        }
    }

    private PopulationCriteria createUserNullCriteria() {
        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setAttributeName("user.loginInfo.username");
        criteria.setComparator(IPopulationEngine.ISNULL);
        criteria.setRefValue(null);
        return criteria;
    }

    private PopulationCriteria addQuestionAsFirstCriteria(Population population, String refvalue) {
        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setAttributeName("38");
        criteria.setQuestionnaireWorkflowId(new Long(0));
        criteria.setComparator(IPopulationEngine.EQ);
        criteria.setRefValue(refvalue);
        population.addPopulationCriteria(criteria);
        return criteria;
    }

    private void addNullLanguageAsFirstCriteria(Population population) {

        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setAttributeName("38");
        criteria.setComparator(IPopulationEngine.ISNULL);
        population.addPopulationCriteria(criteria);
    }

    private void addLanguageAsCriteria(Population population) {

        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setOperator(IPopulationEngine.AND);
        criteria.setAttributeName("38");
        criteria.setComparator(IPopulationEngine.EQ);
        criteria.setRefValue("264");
        population.addPopulationCriteria(criteria);
    }

    private void addOrgUnitAsFirstCriteria(Population population) {
        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setAttributeName("subjectPrimaryAssociations.position.organisationUnit.id");
        criteria.setComparator(IPopulationEngine.EQ);
        criteria.setRefValue("0");
        population.addPopulationCriteria(criteria);
    }

    private void addJobTitleFirstCriteria(Population population) {
        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setAttributeName("subjectPrimaryAssociations.position.title");
        criteria.setComparator(IPopulationEngine.LIKE);
        criteria.setRefValue("Default%");
        population.addPopulationCriteria(criteria);
    }

    private void addDateOfBirthFirstCriteria(Population population) {
        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setAttributeName(AnalysisAttributeHelper.DOB_ATTR);
        criteria.setComparator(IPopulationEngine.LT);
        criteria.setRefValue("2005-09-15");
        population.addPopulationCriteria(criteria);
    }

    private PopulationCriteria getTitleAsCriteria(String operator, String prefix) {
        PopulationCriteria c = new PopulationCriteria();
        String name = prefix != null ? prefix + "." + "title" : "title";
        c.setAttributeName(name);
        c.setComparator(IPopulationEngine.LIKE);
        c.setRefValue("Child");
        c.setOperator(operator);
        return c;
    }

    private PopulationCriteria createAssocPosPopulationCriteria(String operator, String prefix) {
        PopulationCriteria populationCriteria = new PopulationCriteria();
        populationCriteria.setLabel("Test Assoc");
        populationCriteria.setType(IPopulationEngine.OP_TYPE_);
        populationCriteria.setOperator(operator);
        populationCriteria.setAttributeName(prefix + ".qualifier.id");
        populationCriteria.setComparator(IPopulationEngine.EQ);
        populationCriteria.setRefValue("20");
        return populationCriteria;
    }

    private int numRecords = -1;
    private HibernatePopulationEngine populationEngine = (HibernatePopulationEngine) applicationContext.getBean("populationEngine");
    private ILookupManager lookupManager = (ILookupManager) applicationContext.getBean("lookupManager");
}
