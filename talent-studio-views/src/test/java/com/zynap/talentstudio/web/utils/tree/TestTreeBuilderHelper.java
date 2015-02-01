package com.zynap.talentstudio.web.utils.tree;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Aug-2005
 * Time: 14:15:04
 * To change this template use File | Settings | File Templates.
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;

import java.util.List;

public class TestTreeBuilderHelper extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        organisationUnitService = (IOrganisationUnitService) applicationContext.getBean("organisationUnitService");
    }

    public void testBuildOrgUnitTree() throws Exception {

        final List hierarchyAndPositions = organisationUnitService.findOrgUnitTree(DEFAULT_ORG_UNIT_ID);
        final List list = TreeBuilderHelper.buildPositionsTree(hierarchyAndPositions);
        assertEquals(1, list.size());
    }

      public void testBuildSubjectTree() throws Exception {

        final List hierarchyAndPositions = organisationUnitService.findOrgUnitTree(DEFAULT_ORG_UNIT_ID);
        final List list = TreeBuilderHelper.buildSubjectTree(hierarchyAndPositions);
        assertEquals(1, list.size());
    }

    TreeBuilderHelper treeBuilderHelper;
    private IOrganisationUnitService organisationUnitService;
}