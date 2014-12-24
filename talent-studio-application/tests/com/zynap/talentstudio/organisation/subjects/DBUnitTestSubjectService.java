/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 25-May-2006 15:20:31
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;

import java.util.*;

public class DBUnitTestSubjectService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-subject-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        subjectService = (ISubjectService) getBean("subjectService");
    }

    public void testFindById() throws Exception {

        long startTime = System.currentTimeMillis();
        Subject result = subjectService.findById(SUB_USER_ID);
        long endTime = System.currentTimeMillis();

        long difference = (endTime - startTime);
        System.out.println("time took : " + difference + " milli secs");
        Set set = result.getSubjectAssociations();
        System.out.println("set = " + set);
    }

    public void testFindTeam() throws Exception {
        long startTime = System.currentTimeMillis();
        Collection<SubjectDTO> team = subjectService.findTeam(USER_ID);

        long endTime = System.currentTimeMillis();

        long difference = (endTime - startTime);
        System.out.println("time took : " + difference + " milli secs");

        List subordinateIds = Arrays.asList(SUB_IDS);
        assertEquals(subordinateIds.size(), team.size());
        for (SubjectDTO subject : team) {
            assertTrue(subordinateIds.contains(subject.getId()));
        }
    }

    public void testFindTeamNoTeam() throws Exception {
        Collection<SubjectDTO> team = subjectService.findTeam(SUB_USER_ID);
        assertEquals(0, team.size());
    }

    public void testFindTeamOneLevelOnly() throws Exception {
        Collection<SubjectDTO> team = subjectService.findTeam(SUPER_USER_ID);
        assertEquals(1, team.size());
        List subordinateIds = Arrays.asList(SUB_IDS);
        SubjectDTO subordinate = team.iterator().next();
        assertFalse("subordinates in the list are below the first level hence should not have been found", subordinateIds.contains(subordinate.getId()));
    }

    private ISubjectService subjectService;
    private final Long USER_ID = new Long(-33);
    private final Long SUB_USER_ID = new Long(-322);
    private final Long SUPER_USER_ID = new Long(-323);
    private final Long[] SUB_IDS = {new Long(-34), new Long(-321), new Long(-322)};
}