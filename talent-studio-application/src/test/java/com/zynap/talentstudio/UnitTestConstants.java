/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.PositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.domain.admin.User;
import com.zynap.domain.IDomainObject;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Feb-2007 15:01:08
 */
public interface UnitTestConstants {

    final LookupValue ACTING_SUBJECT_PRIMARY_QUALIFIER = new LookupValue(new Long(399));
    String ROOT_USERNAME = "talentsys";
    String ROOT_PASSWORD = "TMEvolution123";
    Long ROOT_USER_ID = IDomainObject.ROOT_USER_ID;
    User ROOT_USER = new User(ROOT_USER_ID, ROOT_USERNAME, "zynap", "sys");
    Long ADMINISTRATOR_USER_ID = new Long(1);
    Long DEFAULT_ORG_UNIT_ID = OrganisationUnit.ROOT_ORG_UNIT_ID;
    OrganisationUnit DEFAULT_ORG_UNIT = new OrganisationUnit(DEFAULT_ORG_UNIT_ID);
    Long DEFAULT_POSITION_ID = new Long(PositionService.POSITION_DEFAULT);
    Position DEFAULT_POSITION = new Position(DEFAULT_POSITION_ID);

}
