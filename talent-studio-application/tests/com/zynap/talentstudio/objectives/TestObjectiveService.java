package com.zynap.talentstudio.objectives;

/**
 * User: amark
 * Date: 23-May-2006
 * Time: 17:13:34
 */

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.UserSessionFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestObjectiveService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        objectiveService = (IObjectiveService) getBean("objectiveService");
    }

    public void testFindAll() throws Exception {
        final List all = objectiveService.findAll();
        assertNotNull(all);
    }

    private ObjectiveDefinition getNewDefinition(String label, String status) {
        final ObjectiveDefinition objectiveDefinition = new ObjectiveDefinition();
        objectiveDefinition.setLabel(label);
        objectiveDefinition.setStatus(status);
        return objectiveDefinition;
    }

    public void testFindByInvalidId() throws Exception {
        try {
            objectiveService.findById(new Long(-999));
            fail("Expected exception when doing find with invalid id");
        } catch (TalentStudioException expected) {

        }
    }

    public void testCreateOrUpdate() throws Exception {

        final String label = "label";
        final String status = ObjectiveConstants.STATUS_APPROVED;
        final String newObjective1Label = "newObjective1Label";

        final ObjectiveDefinition objectiveDefinition = getNewDefinition(label, status);
        final Objective newObjective1 = createNewObjective(ObjectiveConstants.STATUS_OPEN, newObjective1Label);

        final ObjectiveSet objectiveSet = newObjective1.getObjectiveSet();
        objectiveDefinition.addObjectiveSet(objectiveSet);

        // create objective set
        objectiveService.createOrUpdate(objectiveSet);

        // check date created etc for new objective 1
        assertNotNull(newObjective1.getId());
        checkAuditingDetails(newObjective1);

        final String newObjective2Label = "newObjective2Label";

        // add a second objective
        final Objective newObjective2 = createNewObjective(newObjective2Label, objectiveSet, ObjectiveConstants.STATUS_OPEN);
        objectiveSet.addObjective(newObjective2);
        objectiveService.createOrUpdate(objectiveSet);

        // check date created etc for new objective 2
        checkAuditingDetails(newObjective2);
    }

    public void testUpdateObjective() throws Exception {

        final String label = "label";
        final String status = "sendTo";
        final String objectiveLabel = "objectiveLabel";

        final Objective newObjective = createNewObjective(status, objectiveLabel);
        final ObjectiveSet objectiveSet = newObjective.getObjectiveSet();
        final ObjectiveDefinition objectiveDefinition = getNewDefinition(label, status);
        objectiveDefinition.addObjectiveSet(objectiveSet);
        // create objective set
        objectiveService.createOrUpdate(objectiveSet);

        final Long createdBy = newObjective.getCreatedById();
        Date dateCreated = newObjective.getDateCreated();

        // do an update - check the update fields are set and that the date created etc fields do not change
        objectiveService.updateObjective(newObjective);
        assertNotNull(newObjective.getDateUpdated());
        assertNotNull(newObjective.getUpdatedById());
        assertEquals(createdBy, newObjective.getCreatedById());
        assertEquals(dateCreated, newObjective.getDateCreated());

    }

    public void testDeleteObjective() throws Exception {

        final String label = "label";
        final String status = "sendTo";
        final String objectiveLabel = "objectiveLabel";

        final Objective newObjective = createNewObjective(status, objectiveLabel);
        final ObjectiveSet objectiveSet = newObjective.getObjectiveSet();
        final ObjectiveDefinition objectiveDefinition = getNewDefinition(label, status);
        objectiveDefinition.addObjectiveSet(objectiveSet);
        // create objective set
        objectiveService.createOrUpdate(objectiveSet);

        // delete
        objectiveService.deleteObjective(newObjective);

        // find should now fail
        try {
            objectiveService.findObjective(newObjective.getId());
            fail();
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testApproveObjective() throws Exception {

        final String label = "label";
        final String status = "sendTo";
        final String objectiveLabel = "objectiveLabel";

        final Objective newObjective = createNewObjective(status, objectiveLabel);
        final ObjectiveSet objectiveSet = newObjective.getObjectiveSet();
        final ObjectiveDefinition objectiveDefinition = getNewDefinition(label, status);
        objectiveDefinition.addObjectiveSet(objectiveSet);
        // create objective set
        objectiveService.createOrUpdate(objectiveSet);

        // approve and check update and approved fields are set
        objectiveService.approveObjective(newObjective, UserSessionFactory.getUserSession().getUser());
        assertNotNull(newObjective.getDateUpdated());
        assertNotNull(newObjective.getUpdatedById());
        assertEquals(ObjectiveConstants.STATUS_APPROVED, newObjective.getStatus());
    }

    public void testFindArchivedObjectiveSets() throws Exception {
        Subject subject = new Subject(new CoreDetail("firstname", "secondName"));
        ISubjectService subjectService = (ISubjectService) getBean("subjectService");
        subjectService.create(subject);

        ObjectiveDefinition definition = objectiveService.getPublishedDefinition();

        ObjectiveSet objectiveSet = new ObjectiveSet();
        objectiveSet.setStatus(ObjectiveConstants.STATUS_ARCHIVED);
        objectiveSet.setType(ObjectiveConstants.USER_TYPE);
        objectiveSet.setObjectiveDefinition(definition);
        objectiveSet.setSubject(subject);
        objectiveSet.setPublishedDate(new Date());
        objectiveSet.setLabel("testing");
        objectiveService.createOrUpdate(objectiveSet);

        List archivedObjectiveSets = objectiveService.getArchivedObjectiveSets(subject.getId());
        assertEquals(1, archivedObjectiveSets.size());
    }

    private Objective createNewObjective(final String status, String objectiveLabel) {

        final ObjectiveSet objectiveSet = getNewObjectiveSet(ObjectiveConstants.CORPORATE_TYPE);

        // create with null status date created created by
        final Objective newObjective = createNewObjective(objectiveLabel, objectiveSet, status);
        objectiveSet.addObjective(newObjective);

        return newObjective;
    }

    private Objective createNewObjective(String objectiveLabel, final ObjectiveSet objectiveSet, String status) {
        return new Objective(objectiveLabel, objectiveSet, null, null, status);
    }

    private ObjectiveSet getNewObjectiveSet(String type) {
        ObjectiveSet objectiveSet = new ObjectiveSet(new Subject(), new ArrayList());
        objectiveSet.setType(type);
        return objectiveSet;
    }

    private void checkAuditingDetails(final Objective newObjective) {

        assertNotNull(newObjective.getDateCreated());
        assertNotNull(newObjective.getCreatedById());
        assertNotNull(newObjective.getDateUpdated());
        assertNotNull(newObjective.getUpdatedById());
        assertEquals(ObjectiveConstants.STATUS_OPEN, newObjective.getStatus());
    }

    private IObjectiveService objectiveService;
}