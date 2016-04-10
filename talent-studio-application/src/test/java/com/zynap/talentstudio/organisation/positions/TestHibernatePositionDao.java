package com.zynap.talentstudio.organisation.positions;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 31-Jan-2005
 * Time: 18:38:43
 */

import com.zynap.domain.orgbuilder.ISearchConstants;
import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.ILookupManagerDao;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.ArtefactAssociationHelper;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.NodeAudit;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValueFile;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeDao;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestHibernatePositionDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {

        super.setUp();

        hibernatePositionDao = (IPositionDao) applicationContext.getBean("hibPositionDao");
        permitManagerDao = (IPermitManagerDao) applicationContext.getBean("permitManDao");
        lookupManagerDao = (ILookupManagerDao) applicationContext.getBean("lookupManDao");
        populationEngine = (IPopulationEngine) applicationContext.getBean("populationEngine");
        dynamicAttributeDao = (IDynamicAttributeDao) applicationContext.getBean("dynamicAttrDao");
    }

    public void testSearch() throws Exception {
        PositionSearchQuery searchQuery = new PositionSearchQuery();
        searchQuery.setActive(ISearchConstants.ACTIVE);
        Population population = populationEngine.getAllSubjectsPopulation();
        searchQuery.getPopulationForSearch(population);
        populationEngine.find(population, ROOT_USER_ID);
    }

    public void testCreate() throws Exception {

        Position position = new Position(null, "pos", DEFAULT_ORG_UNIT);
        position.setNodeAudit(new NodeAudit(new Date(), position, ROOT_USER_ID));
        position.setTitle("title of the position");

        DynamicAttribute dat = (DynamicAttribute) dynamicAttributeDao.getAllAttributes(Node.POSITION_UNIT_TYPE_).iterator().next();
        AttributeValue at = AttributeValue.create("hola", position, dat);

        Set<AttributeValue> dynamicAttributeValues = new HashSet<AttributeValue>();
        dynamicAttributeValues.add(at);
        position.addAttributeValues(dynamicAttributeValues);

        hibernatePositionDao.create(position);
    }

    public void testCreateWithImage() throws Exception {

        Position position = new Position(null, "pos", DEFAULT_ORG_UNIT);
        position.setNodeAudit(new NodeAudit(new Date(), position, ROOT_USER_ID));
        position.setTitle("title of the position");

        final String value = "hola";
        AttributeValueFile attributeValueFile = (AttributeValueFile) AttributeValue.create(DynamicAttribute.DA_TYPE_IMAGE_O);
        attributeValueFile.setBlobValue(value.getBytes());
        attributeValueFile.initialiseNodeExtendedAttributes(value);

        Set<AttributeValue> dynamicAttributeValues = new HashSet<AttributeValue>();
        dynamicAttributeValues.add(attributeValueFile);
        position.addAttributeValues(dynamicAttributeValues);

        hibernatePositionDao.create(position);
    }

    public void testFindReportStructureFor() throws Exception {

        // check that by default access is denied to nodes
        Node node = new Position();
        assertFalse(node.isHasAccess());

        Position pos = setUpAssociations();
        Position p = hibernatePositionDao.findReportStructureFor(pos.getId(), getAdminUserPrincipal().getUserId(), getViewPositionPermitId(), getViewOrgUnitPermitId(), getViewSubjectPermitId());
        p.setHasAccess(true);
        final Collection sourceAssociations = p.getSourceAssociations();
        assertTrue(sourceAssociations.size() > 0);

        // check that access is allowed for all artefacts since we are using the admin user
        assertTrue(p.isHasAccess());
        assertTrue(p.getOrganisationUnit().isHasAccess());

        // check source associations
        for (Iterator iterator = sourceAssociations.iterator(); iterator.hasNext();) {
            PositionAssociation positionAssociation = (PositionAssociation) iterator.next();
            assertTrue(positionAssociation.getTarget().isHasAccess());
        }

        // check target associations
        final Collection targetAssociations = p.getTargetAssociations();
        for (Iterator iterator = targetAssociations.iterator(); iterator.hasNext();) {
            PositionAssociation positionAssociation = (PositionAssociation) iterator.next();
            assertTrue(positionAssociation.getSource().isHasAccess());
        }

        // check parent
        if (!p.isDefault()) {
            final Position parent = p.getParent();
            assertTrue(parent.isHasAccess());
            assertTrue(parent.getOrganisationUnit().isHasAccess());
        }
    }

    public void testDeleteParent() throws Exception {

        Position parent = new Position(IPopulationEngine.TARGET_ATTR, DEFAULT_ORG_UNIT);
        hibernatePositionDao.create(parent);

        Position child = new Position("child", DEFAULT_ORG_UNIT);
        hibernatePositionDao.create(child);

        final LookupValue qualifier = getDirectPrimaryPositionLookupValue();
        PositionAssociation association = new PositionAssociation(qualifier, parent);
        child.addSourceAssociation(association);
        child.setParent(parent);
        hibernatePositionDao.update(child);

        parent.getChildren().add(child);
        hibernatePositionDao.delete(parent);
    }

    public void testDeleteChild() throws Exception {

        Position child = new Position("child", DEFAULT_ORG_UNIT);
        hibernatePositionDao.create(child);

        Position parent = new Position(IPopulationEngine.TARGET_ATTR, DEFAULT_ORG_UNIT);
        hibernatePositionDao.create(parent);

        final LookupValue qualifier = getDirectPrimaryPositionLookupValue();
        PositionAssociation association = new PositionAssociation(qualifier, parent);
        child.addSourceAssociation(association);
        hibernatePositionDao.update(child);

        hibernatePositionDao.delete(child);
    }

    public void testDefaultPositionAssociations() throws Exception {

        Position defaultPosition = (Position) hibernatePositionDao.findById(DEFAULT_POSITION_ID);
        assertTrue(defaultPosition.isDefault());
        assertNull(defaultPosition.getParent());
        final PositionAssociation primaryAssociation = ArtefactAssociationHelper.getPrimaryAssociation(defaultPosition.getSourceAssociations());
        assertNull(primaryAssociation);

        assertTrue(defaultPosition.getSourceAssociations().isEmpty());

        // test that IllegalStateException is not thrown if you have no parent for a default position
        defaultPosition.assignNewSourceAssociations(new HashSet<ArtefactAssociation>());
    }

    public void testAddSourceAssociation() throws Exception {

        Position newPosition = (Position) hibernatePositionDao.findById(setUpAssociations().getId());
        int numberOfAssocs = newPosition.getSourceAssociations().size();

        // add new source association
        Position newTargetPosition = new Position(IPopulationEngine.TARGET_ATTR, DEFAULT_ORG_UNIT);
        hibernatePositionDao.create(newTargetPosition);
        final LookupValue qualifier = getDirectPrimaryPositionLookupValue();
        PositionAssociation newAssociation = new PositionAssociation(qualifier, newTargetPosition);
        newPosition.addSourceAssociation(newAssociation);
        hibernatePositionDao.update(newPosition);

        Position foundPosition = (Position) hibernatePositionDao.findById(newPosition.getId());
        assertEquals(numberOfAssocs + 1, foundPosition.getSourceAssociations().size());
    }

    public void testFindDescendents() throws Exception {

        /**
         * This test depends on the triggers that maintain the position hierarchy so it may not work if the transaction is rolled back in tearDown
         */
        setUpAssociations();
        Collection list = hibernatePositionDao.findDescendents(DEFAULT_POSITION_ID);
        assertTrue(list.size() > 0);
        final List<Long> longList = hibernatePositionDao.findDescendentIds(DEFAULT_POSITION_ID);
        assertEquals(list.size(), longList.size());
    }

    private Position setUpAssociations() throws Exception {

        final LookupValue qualifier = getDirectPrimaryPositionLookupValue();
        Position defaultPosition = (Position) hibernatePositionDao.findById(DEFAULT_POSITION_ID);

        Position target = new Position(IPopulationEngine.TARGET_ATTR, DEFAULT_ORG_UNIT);
        PositionAssociation association2 = new PositionAssociation(qualifier, defaultPosition);
        target.addSourceAssociation(association2);
        target.setParent(defaultPosition);
        target.setParentId(defaultPosition.getId());
        hibernatePositionDao.create(target);

        Position child = new Position("child", DEFAULT_ORG_UNIT);
        PositionAssociation association = new PositionAssociation(qualifier, target);
        child.addSourceAssociation(association);
        child.setParentId(target.getId());
        hibernatePositionDao.create(child);

        assertEquals(1, child.getSourceAssociations().size());
        assertTrue(child.getTargetAssociations().isEmpty());

        return child;
    }

    private Long getViewSubjectPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.SUBJECT_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    private Long getViewOrgUnitPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.ORGANISATION_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    private Long getViewPositionPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    private LookupValue getDirectPrimaryPositionLookupValue() throws TalentStudioException {
        return lookupManagerDao.findLookupValue("DIRECT", ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC);
    }

    private IPositionDao hibernatePositionDao;
    private ILookupManagerDao lookupManagerDao;
    private IPermitManagerDao permitManagerDao;
    private IDynamicAttributeDao dynamicAttributeDao;
    private IPopulationEngine populationEngine;
}
