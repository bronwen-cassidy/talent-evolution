/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

import com.zynap.domain.orgbuilder.ISearchConstants;
import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.HibernateDynamicAttributeDao;
import com.zynap.talentstudio.organisation.positions.Position;

import java.util.Collection;
import java.util.List;

public class TestHibernateSubjectDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        subjectDao = (HibernateSubjectDao) applicationContext.getBean("hibSubjectDao");
        dynamicAttributeDao = (HibernateDynamicAttributeDao) applicationContext.getBean("dynamicAttrDao");
        populationEngine = (IPopulationEngine) applicationContext.getBean("populationEngine");
    }

    /**
     * Test adding 2 subjects with same name - is valid for 2 subjects to have the same first name and/or last name.
     *
     * @throws Exception
     */
    public void testDuplicateSubjectName() throws Exception {
        Subject subject = new Subject(new CoreDetail("Mr", "Benny22", "Castor4"));
        subjectDao.create(subject);

        Subject subject2 = new Subject(new CoreDetail("Mr", "Benny22", "Castor4"));
        subjectDao.create(subject2);
        assertFalse(subject.equals(subject2));
    }

    /**
     * Create a subject that cannot login.
     *
     * @throws Exception
     */
    public void testCreateSimpleSubject() throws Exception {
        Subject subject = new Subject(new CoreDetail("Mr", "Benny22", "Castor4"));
        subjectDao.create(subject);
        assertTrue(subject.getId() != null);
        assertFalse(subject.isCanLogIn());

        // check that subject associations is not null
        Subject actual = (Subject) subjectDao.findById(subject.getId());
        assertNotNull(actual.getSubjectAssociations());
    }

    /**
     * Test disabling a subject.
     *
     * @throws Exception
     */
    public void testDisableSubject() throws Exception {
        Subject subject = new Subject(new CoreDetail("Mr", "Benny22", "Castor4"));
        subjectDao.create(subject);

        subject.setActive(false);
        subjectDao.update(subject);
        assertFalse(subject.isActive());
    }

    /**
     * Test creation of subject associations.
     *
     * @throws Exception
     */
    public void testCreateSubjectAssociations() throws Exception {
        Subject subject = new Subject(new CoreDetail("Mr", "Benny22", "Castor4"));
        subject.setComments("This is a comment for benny");
        subject.setActive(true);

        // use default org unit and default position
        OrganisationUnit organisationUnit = new OrganisationUnit(DEFAULT_ORG_UNIT_ID);
        Position position = new Position(DEFAULT_POSITION_ID, organisationUnit);
        SubjectAssociation one = new SubjectAssociation(ACTING_SUBJECT_PRIMARY_QUALIFIER, subject, position);
        subject.addSubjectAssociation(one);
        Subject expected = (Subject) subjectDao.create(subject);

        Subject actual = (Subject) subjectDao.findById(expected.getId());
        assertEquals(expected, actual);
        assertEquals(1, actual.getSubjectAssociations().size());
    }

    /**
     * Set dynamic attributes.
     *
     * @throws Exception
     */
    public void testSetDynamicAttributes() throws Exception {

        Subject subject = new Subject(new CoreDetail("Mr", "Benny22", "Castor4"));
        subjectDao.create(subject);

        Subject foundSubject = (Subject) subjectDao.findById(subject.getId());
        assertEquals(0, foundSubject.getDynamicAttributeValues().size());

        Collection allAttributes = dynamicAttributeDao.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);
        DynamicAttribute dynamicAttribute = (DynamicAttribute) ((List) allAttributes).get(0);
        subject.addAttributeValue(AttributeValue.create("test value", dynamicAttribute));
        subjectDao.update(foundSubject);

        Subject foundSubject2 = (Subject) subjectDao.findById(subject.getId());
        assertEquals(1, foundSubject2.getDynamicAttributeValues().size());
    }

    public void testAddDynamicAttributes() throws Exception {

        Subject subject = new Subject(new CoreDetail("Mr", "Benny22", "Castor4"));

        Collection allAttributes = dynamicAttributeDao.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);
        DynamicAttribute dynamicAttribute = (DynamicAttribute) ((List) allAttributes).get(0);
        AttributeValue attributeValue = AttributeValue.create("test value", dynamicAttribute);
        subject.addAttributeValue(attributeValue);

        subjectDao.create(subject);

        Subject foundSubject = (Subject) subjectDao.findById(subject.getId());
        assertEquals(1, foundSubject.getDynamicAttributeValues().size());
    }

    public void testSearch() throws Exception {
        SubjectSearchQuery query = new SubjectSearchQuery();
        query.setActive(ISearchConstants.ACTIVE);
        query.setFirstName("hap");
        Population population = populationEngine.getAllSubjectsPopulation();
        query.getPopulationForSearch(population);
        populationEngine.find(population, ROOT_USER_ID);
    }

    public void testSearchWithResults() throws Exception {

        Subject subject = new Subject(new CoreDetail("Benny", "Salisbury", "Ben", "Mr", "benny@one.com", "122344433"));
        SubjectAssociation association = new SubjectAssociation(ACTING_SUBJECT_PRIMARY_QUALIFIER, subject, DEFAULT_POSITION);
        subject.addSubjectAssociation(association);
        subjectDao.create(subject);
        SubjectSearchQuery query = new SubjectSearchQuery();
        query.setActive(ISearchConstants.ACTIVE);
        query.setFirstName("ben");
        Population population = populationEngine.getAllSubjectsPopulation();
        population = query.getPopulationForSearch(population);
        Collection results = populationEngine.find(population, ROOT_USER_ID);
        assertFalse("should have had at least the one we added", results.isEmpty());
    }

    public void testFindAllSubjectDTOs() throws Exception {
        Collection<SubjectDTO> subjects = subjectDao.findAllSubjectDTOs();
        assertNotNull(subjects);
    }

    public void testFindTeam() throws Exception {
        final List<SubjectDTO> list = subjectDao.findTeam(new Long(-132));
        System.out.println("list = " + list);
    }

    private HibernateSubjectDao subjectDao;
    private HibernateDynamicAttributeDao dynamicAttributeDao;
    private IPopulationEngine populationEngine;

}
