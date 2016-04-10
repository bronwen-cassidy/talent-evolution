/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.DateCalculation;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.help.HelpTextItem;
import com.zynap.talentstudio.help.IHelpTextService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.util.ArrayUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    public void testGetAllAttributes() throws Exception {

        final String nodeType = Node.POSITION_UNIT_TYPE_;

        Collection<DynamicAttribute> allAttributes = dynamicAttrService.getAllAttributes(nodeType);
        assertFalse(allAttributes.isEmpty());
        for (DynamicAttribute dynamicAttribute : allAttributes) {
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
        }

        Collection<DynamicAttribute> activeAttributes = dynamicAttrService.getAllActiveAttributes(nodeType, true);
        assertTrue(allAttributes.containsAll(activeAttributes));
    }

    @Test
    public void testGetSearchableAttributes() throws Exception {
        final String nodeType = Node.POSITION_UNIT_TYPE_;

        Collection searchableAttributes = dynamicAttrService.getSearchableAttributes(nodeType);
        assertFalse(searchableAttributes.isEmpty());
        for (Object searchableAttribute : searchableAttributes) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) searchableAttribute;
            assertTrue(dynamicAttribute.isActive());
            assertTrue(dynamicAttribute.isSearchable());
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
        }
    }

    @Test
    public void testGetTypedAttributes() throws Exception {
        final String nodeType = Node.SUBJECT_UNIT_TYPE_;
        final String attributeType = DynamicAttribute.DA_TYPE_STRUCT;

        final Collection typedAttributes = dynamicAttrService.getTypedAttributes(nodeType, attributeType);
        assertFalse(typedAttributes.isEmpty());
        for (Object typedAttribute : typedAttributes) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) typedAttribute;
            assertTrue(dynamicAttribute.isActive());
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
            assertEquals(attributeType, dynamicAttribute.getType());
            assertNotNull(dynamicAttribute.getRefersToType());
        }
    }

    @Test
    public void testGetAllActiveAttributes() throws Exception {
        final String nodeType = Node.POSITION_UNIT_TYPE_;

        Collection allActiveAttributes = dynamicAttrService.getAllActiveAttributes(nodeType, true);
        assertFalse(allActiveAttributes.isEmpty());
        for (Object allActiveAttribute : allActiveAttributes) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) allActiveAttribute;
            assertTrue(dynamicAttribute.isActive());
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
        }
    }

    @Test
    public void testListActiveAttributes() throws Exception {
        final String[] nodeTypes = new String[]{Node.POSITION_UNIT_TYPE_, DynamicAttribute.NODE_TYPE_FUNCTION};
        final String[] attributeTypes = new String[]{DynamicAttribute.DA_TYPE_DATE};

        Collection collection = dynamicAttrService.listActiveAttributes(nodeTypes, false, attributeTypes);
        assertFalse(collection.isEmpty());
        for (Object aCollection : collection) {
            DynamicAttributeDTO dynamicAttributeDTO = (DynamicAttributeDTO) aCollection;
            assertNotNull(dynamicAttributeDTO.getId());
            assertNotNull(dynamicAttributeDTO.getLabel());
        }
    }

    @Test
    public void testCreate() throws Exception {

        DynamicAttribute dynamicAttribute = new DynamicAttribute(null, "funally1", DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, true, true, false);
        dynamicAttrService.create(dynamicAttribute);

        // check that external ref label has been set
        assertNotNull(dynamicAttribute.getExternalRefLabel());

        DynamicAttribute real = dynamicAttrService.findById(dynamicAttribute.getId());
        assertEquals(dynamicAttribute, real);
    }

    @Test
    public void testCreateCalculated() throws Exception {

        DynamicAttribute dynamicAttribute = new DynamicAttribute(null, "funally2", DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, true, true, false);
        dynamicAttribute.setCalculated(true);

        Collection attributes = dynamicAttrService.getAllActiveAttributes(DynamicAttribute.NODE_TYPE_FUNCTION, true);
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
        dynamicAttrService.create(dynamicAttribute);

        // check that external ref label has been set
        assertNotNull(dynamicAttribute.getExternalRefLabel());

        DynamicAttribute real = dynamicAttrService.findById(dynamicAttribute.getId());
        Calculation realCalc = real.getCalculation();
        List<Expression> actual = realCalc.getExpressions();

        assertEquals(expected, actual);
        assertEquals(calculation, realCalc);
        assertEquals(dynamicAttribute, real);
    }

    @Test
    public void testUpdate() throws Exception {

        Collection attributes = dynamicAttrService.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);

        // get the first and swap it from active to inactive
        DynamicAttribute attribute = (DynamicAttribute) attributes.iterator().next();
        boolean active = attribute.isActive();
        attribute.setActive(!active);
        final String originalExternalRefLabel = attribute.getExternalRefLabel();

        dynamicAttrService.update(attribute);

        // check that external ref label has not been modified
        DynamicAttribute actual = dynamicAttrService.findById(attribute.getId());
        assertEquals(attribute, actual);
        assertEquals(originalExternalRefLabel, actual.getExternalRefLabel());
    }

    @Test
    public void testDelete() throws Exception {

        // add a new da
        DynamicAttribute dynamicAttribute = new DynamicAttribute(null, "funally3", DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, true, true, false);
        dynamicAttrService.create(dynamicAttribute);

        // delete it
        final Long id = dynamicAttribute.getId();
        dynamicAttrService.delete(id);

        // check it has been deleted
        try {
            dynamicAttrService.findById(id);
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    @Test
    @Transactional
    public void testUsedByNode() throws Exception {

        final Position defaultPosition = positionService.findById(DEFAULT_POSITION_ID);
        defaultPosition.getExtendedAttributes().size();
        final String value = "fred";

        // add a value for a dynamic attribute to the default position
        DynamicAttribute dynamicAttribute = dynamicAttrService.getAllActiveAttributes(Node.POSITION_UNIT_TYPE_, true).iterator().next();
        final AttributeValue attributeValue = AttributeValue.create(value, defaultPosition, dynamicAttribute);
        defaultPosition.addAttributeValue(attributeValue);
        positionService.update(defaultPosition);

        // check it is now marked as used
        final boolean used = dynamicAttrService.usedByNode(dynamicAttribute.getId());
        assertTrue(used);
    }

    @Test
    public void testGetNodeLabel() throws Exception {
        final String nodeLabel = dynamicAttrService.getNodeLabel(DEFAULT_ORG_UNIT_ID.toString());
        assertEquals(DEFAULT_ORG_UNIT_LABEL, nodeLabel);
    }

    @Test
    public void testGetNodeLabelNullId() throws Exception {
        final String nodeLabel = dynamicAttrService.getDomainObjectLabel(null);
        assertNotNull(nodeLabel);
        assertEquals(0, nodeLabel.length());
    }

    @Test
    public void testGetDomainObjectLabelNode() throws Exception {
        DynamicAttribute nodeType = new DynamicAttribute("node", DynamicAttribute.DA_TYPE_OU);
        assertTrue(nodeType.isNodeType());
        AttributeValue mock = AttributeValue.create(DEFAULT_ORG_UNIT_ID.toString(), nodeType);
        String domainObjectLabel = dynamicAttrService.getDomainObjectLabel(mock);
        assertEquals(DEFAULT_ORG_UNIT_LABEL, domainObjectLabel);
    }

    @Test
    public void testGetDomainObjectLabelAdmin() throws Exception {
        DynamicAttribute userType = new DynamicAttribute("user", DynamicAttribute.DA_TYPE_LAST_UPDATED_BY);
        assertTrue(userType.isLastUpdatedByType());
        AttributeValue mock = AttributeValue.create(ROOT_USER_ID.toString(), userType);
        String domainObjectLabel = dynamicAttrService.getDomainObjectLabel(mock);
        User adminUser =  ((IUserService) getBean("userService")).findById(ROOT_USER_ID);
        assertEquals(adminUser.getLabel(), domainObjectLabel);
    }

    @Test
    public void testGetDomainObjectLabelNone() throws Exception {
        DynamicAttribute textType = new DynamicAttribute("date", DynamicAttribute.DA_TYPE_TEXTFIELD);
        assertFalse(textType.isLastUpdatedByType());
        AttributeValue mock = AttributeValue.create("-99", textType);
        String domainObjectLabel = dynamicAttrService.getDomainObjectLabel(mock);
        assertNotNull(domainObjectLabel);
        assertEquals("", domainObjectLabel);
    }

    @Test
    public void testGetDomainObjectInvalidId() throws Exception {
        final IDomainObject domainObject = dynamicAttrService.getDomainObject(AttributeValue.create("-999", new DynamicAttribute("label", DynamicAttribute.DA_TYPE_POSITION)));
        assertNull(domainObject);
    }

    @Test
    public void testFindAll() throws Exception {

        // make one subject attribute inactive
        Collection<DynamicAttribute> positionAttributes = dynamicAttrService.getAllAttributes(Node.POSITION_UNIT_TYPE_);
        Collection<DynamicAttribute> subjectAttributes = dynamicAttrService.getAllAttributes(Node.SUBJECT_UNIT_TYPE_);

        DynamicAttribute selectedAttribute = subjectAttributes.iterator().next();
        selectedAttribute.setActive(false);
        dynamicAttrService.update(selectedAttribute);

        // get all and check that it is present in the list
        final List all = dynamicAttrService.findAll();
        assertFalse(all.isEmpty());
        assertTrue(all.contains(selectedAttribute));

        // check that all position and person attributes are in there
        assertTrue(all.containsAll(subjectAttributes));
        assertTrue(all.containsAll(positionAttributes));
    }

    @Test
    public void testFindAllDefault() throws Exception {
        long start = System.currentTimeMillis();
        dynamicAttrService.findAll();
        long end = System.currentTimeMillis();
        System.out.println("time taken = " + (end - start) + " millisecs");
    }

    @Test
    public void testGetActiveAttributes() throws Exception {

        final boolean searchableOnly = true;
        final String nodeType = Node.POSITION_UNIT_TYPE_;
        final String[] attributeTypes = {DynamicAttribute.DA_TYPE_STRUCT, DynamicAttribute.DA_TYPE_MULTISELECT};

        final Collection activeAttributes = dynamicAttrService.getActiveAttributes(nodeType, searchableOnly, attributeTypes);
        assertFalse(activeAttributes.isEmpty());

        for (Iterator iterator = activeAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            assertTrue(ArrayUtils.contains(attributeTypes, dynamicAttribute.getType()));
            assertEquals(searchableOnly, dynamicAttribute.isSearchable());
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
            assertTrue(dynamicAttribute.isActive());
        }
    }

    @Test
    public void testFindHelpTextItem() throws Exception {
        final HelpTextItem helpTextItem = dynamicAttrService.findHelpTextItem(new Long(-100));
        assertNull(helpTextItem);
    }

    @Test
    public void testAddHelpTextItem() throws Exception {

        final DynamicAttribute dynamicAttribute = new DynamicAttribute(null, "attr1", DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, true, true, false);
        dynamicAttrService.create(dynamicAttribute);
        final Long daId = dynamicAttribute.getId();

        final String helpText = "Some content";

        final HelpTextItem helpTextItem = new HelpTextItem(daId, helpText.getBytes());
        dynamicAttribute.setHelpTextItem(helpTextItem);
        // add the helptext item first
        IHelpTextService helpTextService = (IHelpTextService) getBean("helpTextService");
        helpTextService.update(helpTextItem);
        
        HelpTextItem found = dynamicAttrService.findHelpTextItem(daId);
        assertEquals(helpTextItem, found);
        assertEquals(helpText, found.getContentAsString());

        final String newHelpText = "new content";
        helpTextItem.setBlobValue(newHelpText.getBytes());
        helpTextService.update(helpTextItem);

        found = dynamicAttrService.findHelpTextItem(daId);
        assertEquals(newHelpText, found.getContentAsString());

        // delete the help text item
        helpTextService.delete(helpTextItem);

        found = dynamicAttrService.findHelpTextItem(daId);
        assertNull(found);
    }

    @Test
    public void testListAllAttributes() throws Exception {
        List subjectAttributes = dynamicAttrService.listAllAttributes(Node.SUBJECT_UNIT_TYPE_);
        assertFalse(subjectAttributes.isEmpty());
    }

    @Test
    public void testGetSearchableAttributeDtos() throws Exception {
        final Collection<DynamicAttributeDTO> attributeDTOCollection = dynamicAttrService.getSearchableAttributeDtos("S");
        assertNotNull(attributeDTOCollection);
    }

    @Autowired
    private IDynamicAttributeService dynamicAttrService;
    @Autowired
    private IPositionService positionService;

    private static final String DEFAULT_ORG_UNIT_LABEL = "Default Org Unit";
}
