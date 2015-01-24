package com.zynap.domain.admin;

import com.zynap.domain.Auditable;
import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.security.roles.Role;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: bcassidy
 * Date: 02-Feb-2014
 * Time: 17:54:12
 */
public class User extends ZynapDomainObject implements Auditable {

    public User() {
    }

    public User(Long id) {
        setId(id);
    }

    public User(LoginInfo loginInfo, CoreDetail coreDetail) {
        this.coreDetail = coreDetail;
        setLoginInfo(loginInfo);
    }

    public User(LoginInfo loginInfo, CoreDetail coreDetail, Set<Role> userRoles) {
        this(loginInfo, coreDetail);
        this.userRoles = userRoles;
    }

    public User(Long id, String username, String firstName, String lastName) {
        this(id);
        LoginInfo info = new LoginInfo();
        info.setUsername(username);
        setLoginInfo(info);
        setCoreDetail(new CoreDetail(firstName, lastName));
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        if (loginInfo != null) {
            loginInfo.setUser(this);
            this.loginInfo = loginInfo;
        }
    }

    public void setUserType(String userType) {
        this.userType = UserType.create(userType);
    }

    public String getUserType() {
        return userType.toString();
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public CoreDetail getCoreDetail() {
        return coreDetail;
    }

    public void setCoreDetail(CoreDetail coreDetail) {
        this.coreDetail = coreDetail;
    }

    public Collection<Role> getUserRoles() {
        if (userRoles == null) userRoles = new HashSet<Role>();
        return userRoles;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    /**
     * Add role.
     * <br> Sets the Role to be assigned.
     *
     * @param role The Role
     */
    public void addRole(Role role) {
        role.setAssigned(true);
        getUserRoles().add(role);
    }

    public Collection<SessionLog> getSessionLogs() {
        if (sessionLogs == null) sessionLogs = new HashSet<SessionLog>();
        return sessionLogs;
    }

    public void setSessionLogs(Collection<SessionLog> sessionLogs) {
        this.sessionLogs = sessionLogs;
    }

    public void addSessionLog(SessionLog sessionLog) {
        sessionLog.setUser(this);
        getSessionLogs().add(sessionLog);
    }

    public Collection<SecurityDomain> getSecurityDomains() {
        if (securityDomains == null) securityDomains = new HashSet<SecurityDomain>();
        return securityDomains;
    }

    public void setSecurityDomains(Collection<SecurityDomain> securityDomains) {
        this.securityDomains = securityDomains;
    }

    public Collection getSubjects() {
        if (subjects == null) subjects = new HashSet();
        return subjects;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setSubjects(Set subjects) {
        this.subjects = subjects;
    }

    public String getLabel() {
        return coreDetail != null ? coreDetail.getName() : null;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("active", isActive())
                .append("coreDetails", getCoreDetail())
                .append("loginInfo", getLoginInfo())
                .toString();
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public Subject getSubject() {
        final Collection currentSubjects = getSubjects();
        return (currentSubjects.isEmpty() ? null : (Subject) currentSubjects.iterator().next());
    }

    public boolean isHasSubject() {
        return getSubject() != null;
    }

    public Long getSubjectId() {
        final Subject subject = getSubject();
        return subject != null ? subject.getId() : null;
    }

    /**
     * @return lastName firstName concatenation
     */
    public String getDisplayName() {
        return coreDetail != null ? coreDetail.getLastNameFirstName() : null;
    }

    public String getUserName() {
        return loginInfo != null ? loginInfo.getUsername() : null;
    }

    public boolean isSuperUser() {

        for (Role role : userRoles) {
            if (role.isAdministrationRole()) {
                return true;
            }
        }
        return false;
    }

    public User createAuditable() {
        User u = new User();
        u.setId(this.id);
        if (loginInfo != null) {
            u.setLoginInfo(loginInfo.createAuditable());
        }
        if (coreDetail != null) {
            u.setCoreDetail(coreDetail);
        }
        if (group != null) {
            u.setGroup(group.createAuditable());
        }
        return u;
    }

    private UserType userType = UserType.USER;

    private boolean root = false;

    private LoginInfo loginInfo;

    private CoreDetail coreDetail;

    // the users home page group
    private Group group;

    private Set<Role> userRoles;

    private Collection<SessionLog> sessionLogs;

    private Collection<SecurityDomain> securityDomains;

    private Set subjects;
}
