/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 21-Nov-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.common.groups;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.security.users.IUserDao;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Nov-2007 08:53:13
 */
public class GroupService extends DefaultService implements IGroupService {

    public List<Group> find(String groupType) throws TalentStudioException {
        return groupDao.find(groupType);
    }

    public List<HomePage> findHomePages(Long groupId) throws TalentStudioException {
        return groupDao.findHomePages(groupId);
    }

    public void createOrUpdate(Group group) throws TalentStudioException {
        if(group.getId() == null) {
            groupDao.create(group);
        } else {
            groupDao.update(group);
        }
    }

    public void delete(Long groupId) throws TalentStudioException {
        Group group = findById(groupId);
        if (group.isHomePageType()) {
            userDao.deleteHomePageGroups(groupId);
        }
        groupDao.delete(group);
    }

    public Group findById(Long groupId) throws TalentStudioException {
        return (Group) groupDao.findByID(groupId);
    }

    protected IFinder getFinderDao() {
        return groupDao;
    }

    protected IModifiable getModifierDao() {
        return groupDao;
    }

    public void setGroupDao(IGroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    private IGroupDao groupDao;
    private IUserDao userDao;
}
