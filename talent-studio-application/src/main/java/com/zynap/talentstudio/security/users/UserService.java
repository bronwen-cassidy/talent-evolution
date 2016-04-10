/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.users;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.SearchAdaptor;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.PasswordHistory;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserPassword;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.exception.UserLoginFailedException;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class UserService implements IUserService {

    public void delete(IDomainObject domainObject) throws TalentStudioException {
        userDao.delete(domainObject);
    }

    public void create(IDomainObject domainObject) throws TalentStudioException {

        User user = (User) domainObject;

        LoginInfoValidator.validateLoginInfo(user, userDao, true);

        userDao.create(user);
    }

    public void update(IDomainObject domainObject) throws TalentStudioException {
        User user = (User) domainObject;
        LoginInfoValidator.validateLoginInfo(user, userDao, false);
        userDao.update(user);
    }

    public void update(LoginInfo loginInfo) throws TalentStudioException {
        userDao.update(loginInfo);
    }

    public <T> T findById(Serializable id) throws TalentStudioException {
        return userDao.findById(id);
    }

    public Collection search(Long principalId, SearchAdaptor searchAdaptor) {
        return userDao.search(principalId, searchAdaptor);
    }

    public UserPrincipal logInUser(LoginInfo loginInfo, String sessionId, String remoteAddr) throws TalentStudioException {

        // on login only need to check password expiry and if password has been locked
        LoginInfo checked = LoginInfoValidator.validateLoginPassword(loginInfo, userDao);

        User user = checked.getUser();
        if (!user.isActive()) {
            throw new UserLoginFailedException();
        }

        checked.resetNumberLoginAttempts();
        userDao.update(checked);

        // add session log
        SessionLog sessionLog = createSessionLog(sessionId, remoteAddr, user);

        user = userDao.findById(user.getId());
        final Group group = user.getGroup();
        final OrganisationUnit unit = organisationUnitService.findOrgUnitByUser("" + user.getId());
        if (group != null) group.getHomePages().size();

        return new UserPrincipal(user, getPermitManagerDao().getAccessPermits(user.getId()), sessionLog, unit);
    }

    public String getDecryptedPassword(String plainPassword) {
        return userDao.decrypt(plainPassword);
    }

    public String getEncryptedPassword(String plainPassword) {
        return userDao.encrypt(plainPassword);
    }

    public void assignUserPermits(Long userId) throws TalentStudioException {
        userDao.assignUserPermits(userId);        
    }

    private SessionLog createSessionLog(String sessionId, String remoteAddr, User user) throws TalentStudioException {
        SessionLog sessionLog = new SessionLog(sessionId, remoteAddr, STATUS_OPEN, new Date(), null, null, user);
        user.addSessionLog(sessionLog);
        userDao.create(sessionLog);
        return sessionLog;
    }


    /**
     * Get all users except root-level user ("zynapsys").
     *
     * @return Collection of {@link com.zynap.domain.admin.User} objects.
     */
    public List<User> getAppUsers() {
        return userDao.getAppUsers();
    }

    public List<UserDTO> listAppUsers() {
        return userDao.listAppUsers();
    }

    public List<User> find(Long[] ids) throws TalentStudioException {
        return userDao.find(ids);
    }

    public void logOutUser(Long userId) throws TalentStudioException {
        List<SessionLog> logs = userDao.getOpenSessionLogs(userId);
        for (SessionLog sessionLog : logs) {
            logOffUser(sessionLog);
        }
    }

    public List<UserDTO> findSystemUsers() {
        return userDao.findSystemUsers();
    }

    public String encrypt(String plainPassword) {
       return userDao.encrypt(plainPassword);
    }

    public String decrypt(String plainPassword) {
       return userDao.decrypt(plainPassword);
    }

    public LoginInfo changePassword(UserPassword pwdObject) throws TalentStudioException {

        User user;
        final String username = pwdObject.getUsername();
        if (StringUtils.hasText(username)) {
            user = findByUserName(username);
        } else {
            user = (User) userDao.findById(pwdObject.getUserId());
        }

        // add entry to password history
        LoginInfo original = user.getLoginInfo();
        PasswordHistory passwordHistory = LoginInfoValidator.validateChangePassword(pwdObject.getNewPassword(), pwdObject.getOldPassword(), original, userDao);
        original.addPasswordHistory(passwordHistory);
        original.setPassword(passwordHistory.getPasswordChanged());

        // reset force password change
        if (original.getForcePasswordChange()) original.setForcePasswordChange(false);
        original.resetNumberLoginAttempts();
        original.setLocked(false);
        userDao.update(original);
        return original;
    }

    /**
     * Find user by username.
     *
     * @param username The username
     * @return User
     * @throws TalentStudioException
     */
    public User findByUserName(String username) throws TalentStudioException {
        return userDao.findByUserName(username);
    }

    public OrganisationUnit getUserDefaultOrganisationUnit(String username) throws TalentStudioException {
        return this.userDao.getUserDefaultOrganisationUnit(username);
    }

    public User getUserById(Long id) throws TalentStudioException {
        return (User) userDao.findById(id);
    }

    public User findBySubjectId(Long id) throws DomainObjectNotFoundException {
        return userDao.findBySubjectId(id);
    }

    public SessionLog getSessionLog(Long sessionId) {
        return userDao.getSessionLog(sessionId);
    }

    private void logOffUser(SessionLog sessionLog) {
        sessionLog.setLoggedOutDate(new Date());
        sessionLog.setLoggedOutReason(LOGGED_OUT_REASON);
        sessionLog.setStatus(STATUS_CLOSED);
        userDao.update(sessionLog);
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public IPermitManagerDao getPermitManagerDao() {
        return _permitManagerDao;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this._permitManagerDao = permitManagerDao;
    }

    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    private IPermitManagerDao _permitManagerDao;
    private IUserDao userDao;
    private IOrganisationUnitService organisationUnitService;
}