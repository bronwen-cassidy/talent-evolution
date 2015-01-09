/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.DateCalculation;
import com.zynap.talentstudio.help.HelpTextItem;
import com.zynap.talentstudio.help.IHelpTextService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.util.ArrayUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

public class TestDynamicAttributeService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        attributeService = (IDynamicAttributeService) applicationContext.getBean("dynamicAttrService");
        positionService = (IPositionService) getBean("positionService");
    }

    public void testGetAllAttributes() throws Exception {

        final String nodeType = Node.POSITION_UNIT_TYPE_;

        Collection<DynamicAttribute> allAttributes = attributeService.getAllAttributes(nodeType);
        assertFalse(allAttributes.isEmpty());
        for (Iterator iterator = allAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
        }

        Collection<DynamicAttribute> activeAttributes = attributeService.getAllActiveAttributes(nodeType, true);
        assertTrue(allAttributes.containsAll(activeAttributes));
    }

    public void testGetSearchableAttributes() throws Exception {
        final String nodeType = Node.POSITION_UNIT_TYPE_;

        Collection searchableAttributes = attributeService.getSearchableAttributes(nodeType);
        assertFalse(searchableAttributes.isEmpty());
        for (Iterator iterator = searchableAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            assertTrue(dynamicAttribute.isActive());
            assertTrue(dynamicAttribute.isSearchable());
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
        }
    }

    public void testGetTypedAttributes() throws Exception {
        final String nodeType = Node.SUBJECT_UNIT_TYPE_;
        final String attributeType = DynamicAttribute.DA_TYPE_STRUCT;

        final Collection typedAttributes = attributeService.getTypedAttributes(nodeType, attributeType);
        assertFalse(typedAttributes.isEmpty());
        for (Iterator iterator = typedAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            assertTrue(dynamicAttribute.isActive());
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
            assertEquals(attributeType, dynamicAttribute.getType());
            assertNotNull(dynamicAttribute.getRefersToType());
        }
    }

    public void testGetAllActiveAttributes() throws Exception {
        final String nodeType = Node.POSITION_UNIT_TYPE_;

        Collection allActiveAttributes = attributeService.getAllActiveAttributes(nodeType, true);
        assertFalse(allActiveAttributes.isEmpty());
        for (Iterator iterator = allActiveAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            assertTrue(dynamicAttribute.isActive());
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
        }
    }

    public void testListActiveAttributes() throws Exception {
        final String[] nodeTypes = new String[]{Node.POSITION_UNIT_TYPE_, DynamicAttribute.NODE_TYPE_FUNCTION};
        final String[] attributeTypes = new String[]{DynamicAttribute.DA_TYPE_DATE};

        Collection collection = attributeService.listActiveAttributes(nodeTypes, false, attributeTypes);
        assertFalse(collection.isEmpty());
        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            DynamicAttributeDTO dynamicAttributeDTO = (DynamicAttributeDTO) iterator.next();
            assertNotNull(dynamicAttributeDTO.getId());
            assertNotNull(dynamicAttributeDTO.getLabel());
        }
    }

    public void testCreate() throws Exception {

        DynamicAttribute dynamicAttribute = new DynamicAttribute(null, "funally3", DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, true, true, false);
        attributeService.create(dynamicAttribute);

        // check that external ref label has been set
        assertNotNull(dynamicAttribute.getExternalRefLabel());

        DynamicAttribute real = attributeService.findById(dynamicAttribute.getId());
        assertEquals(dynamicAttribute, real);
    }

    public void testCreateCalculated() throws Exception {

        DynamicAttribute dynamicAttribute = new DynamicAttribute(null, "funally3", DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, true, true, false);
        dynamicAttribute.setCalculated(true);

        Collection attributes = attributeService.getAllActiveAttributes(DynamicAttribute.NODE_TYPE_FUNCTION, true);
        DynamicAttribute now = (DynamicAttribute) attributes.iterator().next();

        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.MONTHS);
        Expression expression = new Expression();
        expression.setAttribute(now);
        expression.setOperator(Expression.PLUS);
        expression.setIndex(0);
        calculation.addExpression(expression);

        Expression expression0 = new Expression();
        expression0.setValue("3");
        expression0.setIndex(1);
        calculation.addExpression(expression0);

        dynamicAttribute.setCalculation(calculation);
        List<Expression> expected = calculation.getExpressions();
        attributeService.create(dynamicAttribute);

        // check that external ref label has been set
        assertNotNull(dynamicAttribute.getExternalRefLabel());

        DynamicAttribute real = attributeService.findById(dynamicAttribute.getId());
        Calculation realCalc = real.getCalculation();
        List<Expression> actual = realCalc.getExpressions();

        assertEquals(expected, actual);
        assertEquals(calculation, realCalc);
        assertEquals(dynamicAttribute, real);
    }

    public void testUpdate() throws Exception {

        Collection attributes = attributeService.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);

        // get the first and swap it from active to inactive
        DynamicAttribute attribute = (DynamicAttribute) attributes.iterator().next();
        boolean active = attribute.isActive();
        attribute.setActive(!active);
        final String originalExternalRefLabel = attribute.getExternalRefLabel();

        attributeService.update(attribute);

        // check that external ref label has not been modified
        DynamicAttribute actual = attributeService.findById(attribute.getId());
        assertEquals(attribute, actual);
        assertEquals(originalExternalRefLabel, actual.getExternalRefLabel());
    }

    public void testDelete() throws Exception {

        // add a new da
        DynamicAttribute dynamicAttribute = new DynamicAttribute(null, "funally3", DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, true, true, false);
        attributeService.create(dynamicAttribute);

        // delete it
        final Long id = dynamicAttribute.getId();
        attributeService.delete(id);

        // check it has been deleted
        try {
            attributeService.findById(id);
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testUsedByNode() throws Exception {

        final Position defaultPosition = positionService.findByID(DEFAULT_POSITION_ID);
        final String value = "fred";

        // add a value for a dynamic attribute to the default position
        DynamicAttribute dynamicAttribute = attributeService.getAllActiveAttributes(Node.POSITION_UNIT_TYPE_, true).iterator().next();
        final AttributeValue attributeValue = AttributeValue.create(value, defaultPosition, dynamicAttribute);
        defaultPosition.addAttributeValue(attributeValue);
        positionService.update(defaultPosition);

        // check it is now marked as used
        final boolean used = attributeService.usedByNode(dynamicAttribute.getId());
        assertTrue(used);
    }

    public void testGetNodeLabel() throws Exception {
        final String nodeLabel = attributeService.getNodeLabel(DEFAULT_ORG_UNIT_ID.toString());
        assertEquals(DEFAULT_ORG_UNIT_LABEL, nodeLabel);
    }

    public void testGetNodeLabelNullId() throws Exception {
        final String nodeLabel = attributeService.getDomainObjectLabel(null);
        assertNotNull(nodeLabel);
        assertEquals(0, nodeLabel.length());
    }

    public void testGetDomainObjectLabelNode() throws Exception {
        DynamicAttribute nodeType = new DynamicAttribute("node", DynamicAttribute.DA_TYPE_OU);
        assertTrue(nodeType.isNodeType());
        AttributeValue mock = AttributeValue.create(DEFAULT_ORG_UNIT_ID.toString(), nodeType);
        String domainObjectLabel = attributeService.getDomainObjectLabel(mock);
        assertEquals(DEFAULT_ORG_UNIT_LABEL, domainObjectLabel);
    }

    public void testGetDomainObjectLabelAdmin() throws Exception {
        DynamicAttribute userType = new DynamicAttribute("user", DynamicAttribute.DA_TYPE_LAST_UPDATED_BY);
        assertTrue(userType.isLastUpdatedByType());
        AttributeValue mock = AttributeValue.create(ROOT_USER_ID.toString(), userType);
        String domainObjectLabel = attributeService.getDomainObjectLabel(mock);
        User adminUser = (User) ((IUserService) getBean("userService")).findById(ROOT_USER_ID);
        assertEquals(adminUser.getLabel(), domainObjectLabel);
    }

    public void testGetDomainObjectLabelNone() throws Exception {
        DynamicAttribute textType = new DynamicAttribute("date", DynamicAttribute.DA_TYPE_TEXTFIELD);
        assertFalse(textType.isLastUpdatedByType());
        AttributeValue mock = AttributeValue.create("-99", textType);
        String domainObjectLabel = attributeService.getDomainObjectLabel(mock);
        assertNotNull(domainObjectLabel);
        assertEquals("", domainObjectLabel);
    }

    public void testGetDomainObjectInvalidId() throws Exception {
        final IDomainObject domainObject = attributeService.getDomainObject(AttributeValue.create("-999", new DynamicAttribute("label", DynamicAttribute.DA_TYPE_POSITION)));
        assertNull(domainObject);
    }

    public void testFindAll() throws Exception {

        // make one subject attribute inactive
        Collection<DynamicAttribute> positionAttributes = attributeService.getAllAttributes(Node.POSITION_UNIT_TYPE_);
        Collection<DynamicAttribute> subjectAttributes = attributeService.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);

        DynamicAttribute selectedAttribute = subjectAttributes.iterator().next();
        selectedAttribute.setActive(false);
        attributeService.update(selectedAttribute);

        // get all and check that it is present in the list
        final List all = attributeService.findAll();
        assertFalse(all.isEmpty());
        assertTrue(all.contains(selectedAttribute));

        // check that all position and person attributes are in there
        assertTrue(all.containsAll(subjectAttributes));
        assertTrue(all.containsAll(positionAttributes));
    }

    public void testFindAllDefault() throws Exception {
        long start = System.currentTimeMillis();
        attributeService.findAll();
        long end = System.currentTimeMillis();
        System.out.println("time taken = " + (end - start) + " millisecs");
    }

    public void testGetActiveAttributes() throws Exception {

        final boolean searchableOnly = true;
        final String nodeType = Node.POSITION_UNIT_TYPE_;
        final String[] attributeTypes = {DynamicAttribute.DA_TYPE_STRUCT, DynamicAttribute.DA_TYPE_MULTISELECT};

        final Collection activeAttributes = attributeService.getActiveAttributes(nodeType, searchableOnly, attributeTypes);
        assertFalse(activeAttributes.isEmpty());

        for (Iterator iterator = activeAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            assertTrue(ArrayUtils.contains(attributeTypes, dynamicAttribute.getType()));
            assertEquals(searchableOnly, dynamicAttribute.isSearchable());
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
            assertTrue(dynamicAttribute.isActive());
        }
    }

    public void testFindHelpTextItem() throws Exception {
        final HelpTextItem helpTextItem = attributeService.findHelpTextItem(new Long(-100));
        assertNull(helpTextItem);
    }

    public void testAddHelpTextItem() throws Exception {

        final DynamicAttribute dynamicAttribute = new DynamicAttribute(null, "attr1", DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, true, true, false);
        attributeService.create(dynamicAttribute);
        final Long daId = dynamicAttribute.getId();

        final String helpText = "Some content";

        final HelpTextItem helpTextItem = new HelpTextItem(daId, helpText.getBytes());
        dynamicAttribute.setHelpTextItem(helpTextItem);
        // add the helptext item first
        IHelpTextService helpTextService = (IHelpTextService) getBean("helpTextService");
        helpTextService.update(helpTextItem);
        
        HelpTextItem found = attributeService.findHelpTextItem(daId);
        assertEquals(helpTextItem, found);
        assertEquals(helpText, found.getContentAsString());

        final String newHelpText = "new content";
        helpTextItem.setBlobValue(newHelpText.getBytes());
        helpTextService.update(helpTextItem);

        found = attributeService.findHelpTextItem(daId);
        assertEquals(newHelpText, found.getContentAsString());

        // delete the help text item
        helpTextService.delete(helpTextItem);

        found = attributeService.findHelpTextItem(daId);
        assertNull(found);
    }

    public void testListAllAttributes() throws Exception {
        List subjectAttributes = attributeService.listAllAttributes(Node.SUBJECT_UNIT_TYPE_);
        assertFalse(subjectAttributes.isEmpty());
    }

    public void testGetSearchableAttributeDtos() throws Exception {
        final Collection<DynamicAttributeDTO> attributeDTOCollection = attributeService.getSearchableAttributeDtos("S");
        assertNotNull(attributeDTOCollection);
    }

    private IDynamicAttributeService attributeService;
    private IPositionService positionService;
    private static final String DEFAULT_ORG_UNIT_LABEL = "Default Org Unit";
}
