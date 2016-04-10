/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 21-Nov-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.common.groups;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.security.homepages.HomePage;

import java.util.List;

/**
 * Group service providing an interface for grouping functionality
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Nov-2007 08:51:19
 */
public interface IGroupService extends IZynapService {

    List<Group> find(String groupType) throws TalentStudioException;

    List<HomePage> findHomePages(Long groupId) throws TalentStudioException;

    void createOrUpdate(Group group) throws TalentStudioException;

    void delete(Long groupId) throws TalentStudioException;
}
