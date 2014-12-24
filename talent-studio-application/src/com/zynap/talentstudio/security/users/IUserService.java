/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.users;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.SearchAdaptor;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserPassword;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.organisation.OrganisationUnit;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IUserService {

    void delete(IDomainObject domainObject) throws TalentStudioException;

    void create(IDomainObject domainObject) throws TalentStudioException;

    void update(IDomainObject domainObject) throws TalentStudioException;

    void update(LoginInfo loginInfo) throws TalentStudioException;

    IDomainObject findById(Serializable id) throws TalentStudioException;

    Collection search(Long principalId, SearchAdaptor searchAdaptor);

    UserPrincipal logInUser(LoginInfo loginInfo, String sessionId, String remoteAddr) throws TalentStudioException;

    void assignUserPermits(Long userId) throws TalentStudioException;

    LoginInfo changePassword(UserPassword pwdObject) throws TalentStudioException;

    String encrypt(String plainPassword);

    String decrypt(String plainPassword);
    

    /**
     * Get all users except root-level user ("zynapsys").
     *
     * @return Collection of {@link com.zynap.domain.admin.User} objects.
     */
    List<User> getAppUsers();

    List<UserDTO> listAppUsers();

    /**
     * Find user by username.
     * @param username The username
     * @return User
     * @throws TalentStudioException
     */
    User findByUserName(String username) throws TalentStudioException;

    OrganisationUnit getUserDefaultOrganisationUnit(String username) throws TalentStudioException;

    User getUserById(Long userId) throws TalentStudioException;

    User findBySubjectId(Long id) throws DomainObjectNotFoundException;

    SessionLog getSessionLog(Long sessionId) throws TalentStudioException;

    List<User> find(Long[] ids) throws TalentStudioException;

    /**
     * Used to log off the user given the user id rather than the sessionId.
     * The method will make sure all open sessions are closed and cleared up
     * @param userId
     * @throws TalentStudioException
     */
    void logOutUser(Long userId) throws TalentStudioException;

    /**
     * Finds the users with no subject only
     * @return all users except for the root users
     */
    List<UserDTO> findSystemUsers();

    String STATUS_OPEN = "OPEN";
    String STATUS_CLOSED = "CLOSED";
    String LOGGED_OUT_REASON = "Logged Out";
}
