/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.positions;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 20-Oct-2006 09:12:17
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;

import org.apache.commons.lang.ArrayUtils;

import java.util.List;

public class DBUnitTestPositionService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-position-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        positionService = (IPositionService) getBean("positionService");
    }

    public void testFindDescendents() throws Exception {

        List descendents = positionService.findDescendents(HEAD_OF_IT_ID);

        Long[] expected = new Long[]{HEAD_OF_MAINTENANCE_ID, HEAD_OF_DEVELOPMENT_ID, HR_OF_MAINTENANCE_ID, MGR_OF_MAINTENANCE_ID};
        assertPositionsPresent(expected, descendents);

        descendents = positionService.findDescendents(HEAD_OF_MAINTENANCE_ID);
        expected = new Long[]{HR_OF_MAINTENANCE_ID, MGR_OF_MAINTENANCE_ID};        
        assertPositionsPresent(expected, descendents);
    }

    public void testFindAvailableParentsForPositionRootUser() throws Exception {

        // should only get default position
        final List availableParentsForPosition = positionService.findAvailableParentsForPosition(DEFAULT_ORG_UNIT_ID, HEAD_OF_IT_ID, ROOT_USER_ID);
        assertEquals(1, availableParentsForPosition.size());

        assertEquals(DEFAULT_POSITION_ID, ((Position) availableParentsForPosition.get(0)).getId());
    }

    public void testDeletePosition() throws Exception {
        try {
            positionService.deletePosition(HEAD_OF_IT_ID);
            commitAndStartNewTx();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testFindAvailableParentsForPositionCurrentHolder() throws Exception {

        // should get nothing back as current holder cannot see default position
        List availableParentsForPosition = positionService.findAvailableParentsForPosition(DEFAULT_ORG_UNIT_ID, HEAD_OF_IT_ID, HEAD_OF_IT_USER_ID);
        assertTrue(availableParentsForPosition.isEmpty());

        // cannot see own position
        availableParentsForPosition = positionService.findAvailableParentsForPosition(IT_OU_ID, HEAD_OF_IT_ID, HEAD_OF_IT_USER_ID);
        assertTrue(availableParentsForPosition.isEmpty());

        // cannot see children
        availableParentsForPosition = positionService.findAvailableParentsForPosition(DEVELOPMENT_OU_ID, HEAD_OF_IT_ID, HEAD_OF_IT_USER_ID);
        assertTrue(availableParentsForPosition.isEmpty());
    }

    public void testFindAvailableParentsRootUser() throws Exception {

        // should only get default position
        final List availableParentsForPosition = positionService.findAvailableParentsForPosition(DEFAULT_ORG_UNIT_ID, null, ROOT_USER_ID);

        final Long[] expected = new Long[]{DEFAULT_POSITION_ID};
        assertPositionsPresent(expected, availableParentsForPosition);
    }

    public void testFindAvailableParents() throws Exception {

        final List availableParentsForPosition = positionService.findAvailableParentsForPosition(MAINTENANCE_OU_ID, null, ROOT_USER_ID);

        final Long[] expected = new Long[]{HEAD_OF_MAINTENANCE_ID, HR_OF_MAINTENANCE_ID};
        assertPositionsPresent(expected, availableParentsForPosition);
    }

    public void testFindAvailableParentsCurrentHolder() throws Exception {

        final List availableParentsForPosition = positionService.findAvailableParentsForPosition(MAINTENANCE_OU_ID, null, HEAD_OF_MAINTENANCE_USER_ID);

        final Long[] expected = new Long[]{HR_OF_MAINTENANCE_ID};
        assertPositionsPresent(expected, availableParentsForPosition);
    }

    public void testFindUsersPosition() throws Exception {
        Position p = positionService.findUsersPosition(new Long(-321), new Long(-2));
        assertEquals(new Long(-79), p.getId());
        assertEquals("Head Of Development", p.getLabel());
    }

    private void assertPositionsPresent(Long[] expected, List list) {

        for (int i = 0; i < list.size(); i++) {
            final Position position = (Position) list.get(i);
            final Long id = position.getId();
            assertTrue(position.isActive());
            assertTrue("Position not found: " + id, ArrayUtils.contains(expected, id));
        }

        assertEquals(expected.length, list.size());        
    }

    private IPositionService positionService;

    private static final Long IT_OU_ID = new Long(-53);
    private static final Long MAINTENANCE_OU_ID = new Long(-54);
    private static final Long DEVELOPMENT_OU_ID = new Long(-55);

    private static final Long HEAD_OF_IT_ID = new Long(-77);
    private static final Long HEAD_OF_DEVELOPMENT_ID = new Long(-79);
    private static final Long HEAD_OF_MAINTENANCE_ID = new Long(-78);
    private static final Long HR_OF_MAINTENANCE_ID = new Long(-80);
    private static final Long MGR_OF_MAINTENANCE_ID = new Long(-81);

    private static final Long HEAD_OF_IT_USER_ID = new Long(-33);
    private static final Long HEAD_OF_MAINTENANCE_USER_ID = new Long(-34);
}