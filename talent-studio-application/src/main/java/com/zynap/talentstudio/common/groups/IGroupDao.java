/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 21-Nov-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.common.groups;

import com.zynap.talentstudio.common.IDefaultCrudDao;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.exception.TalentStudioException;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Nov-2007 08:53:39
 */
public interface IGroupDao extends IDefaultCrudDao {

    List<Group> find(String groupType) throws TalentStudioException;

    List<HomePage> findHomePages(Long groupId) throws TalentStudioException;
}
