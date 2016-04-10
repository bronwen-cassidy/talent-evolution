/* 
 * Copyright (c) TalentScope Ltd. 2008
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation;
/**
 * Class or Interface description.
 *
 * @author taulant.bajraktari
 * @since 05-Jun-2008 10:03:50
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.exception.TalentStudioException;

public class TestMergeOrganisationUnitService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "merge-organisation-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        organisationUnitService = (IOrganisationUnitService) getBean("organisationUnitService");
    }


    ///////////////////////////////////////////
    //  NOTE : USING CORRECTLY RESTORED DATASET
    //////////////////////
   //  restore db - clean
    //
    // category -> ou->  Group Chief Executive ->  sales,Finance
   //
   // find two ou - and try to merge them
   // 1. get sales
   // 2. get finance
   // 3. merge , update 1, remove 2

   public void testUpdateMerge() throws Exception {


       //both casses should not not be null

       //using ids, as they have complex data set, using integrity and etc
       final OrganisationUnit salesOrgUnit=organisationUnitService.findById((long) 4);
       assertNotNull(salesOrgUnit);

       final OrganisationUnit finaceOrgUnit =organisationUnitService.findById((long) 10);
       assertNotNull(salesOrgUnit);

       //do the merge
       organisationUnitService.updateMerge(salesOrgUnit, finaceOrgUnit);

       //commitAndStartNewTx();

       try {
           organisationUnitService.findById((long) 10);
           fail("DomainObjectNotFoundException should have been thrown");
       } catch (TalentStudioException e) {
           //e.printStackTrace();
           // ok expected
       }

   }
    private IOrganisationUnitService organisationUnitService;
}