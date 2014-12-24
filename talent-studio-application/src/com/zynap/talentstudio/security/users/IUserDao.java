/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.users;

import com.zynap.domain.SearchAdaptor;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.rules.Config;

import java.util.Collection;
import java.util.List;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IUserDao extends IModifiable, IFinder {

    LoginInfo getLoginInfo(String username, String password);

    LoginInfo getLoginInfo(String username);

    String encrypt(String password);
    
    String decrypt(String password);

    Collection search(Long principalId, SearchAdaptor query);

    Config findConfig(Long configId);

    void create(SessionLog sessionLog) throws TalentStudioException;

    SessionLog getSessionLog(Long sessionId);

    void update(SessionLog sessionLog);

    LoginInfo getLoginInfo(Long userId);

    /**
     * Get all users except root-level user ("zynapsys").
     *
     * @return Collection of {@link com.zynap.domain.admin.User} objects.
     */    
    List<User> getAppUsers();

    /**
     * Find user by username.
     * @param username The username
     * @return User
     * @throws TalentStudioException
     */
    User findByUserName(String username) throws TalentStudioException;

    OrganisationUnit getUserDefaultOrganisationUnit(String username) throws TalentStudioException;

    User findBySubjectId(Long subjectId) throws DomainObjectNotFoundException;

    List<UserDTO> listAppUsers();

    List<User> find(Long[] ids) throws TalentStudioException;

    void deleteHomePageGroups(Long groupId) throws TalentStudioException;

    void assignUserPermits(Long userId) throws TalentStudioException;

    List<SessionLog> getOpenSessionLogs(Long userId);

    List<UserDTO> findSystemUsers();
}
