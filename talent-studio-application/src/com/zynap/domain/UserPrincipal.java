/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.domain;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.audit.SessionLog;

import java.io.Serializable;
import java.util.*;

/**
 * UserPrincipal domain object. This object is passed around the system and should only
 * hold data that is important to the logged in user.
 *
 * @author Andreas Andersson, Bronwen Cassidy
 * @version $Revision: $
 *          $Id: $
 * @since 14/04/2004
 */
public final class UserPrincipal implements Serializable {

    public UserPrincipal(User user) {
        this(user, new ArrayList<IPermit>());
    }

    /**
     * Constructor.
     *
     * @param user          the user
     * @param accessPermits The user's active access permits
     */
    public UserPrincipal(User user, Collection<IPermit> accessPermits) {
        this.username = user.getLoginInfo().getUsername();
        this.id = user.getId();
        this.firstName = user.getCoreDetail().getFirstName();
        this.lastName = user.getCoreDetail().getSecondName();
        this.accessPermits = accessPermits;
        this.userType = user.getUserType();
        this.administrator = (user.isRoot() || ADMINISTRATOR_USER_ID.equals(user.getId()));
        this.root = user.isRoot();
        this.subjectId = user.getSubjectId();
        this.accessPermits = accessPermits;
        this.homePages = new HashMap<String, HomePage>();
        this.group = user.getGroup();
        if(group != null) {
            assignHomePages(new HashSet<HomePage>(group.getHomePages()));
        }
        this.userRoles=new HashSet<Role>();
        this.userRoles.addAll(user.getUserRoles());
        this.superUser = user.isSuperUser();
    }

    public UserPrincipal(User user, Collection<IPermit> accessPermits, SessionLog sessionLog) {
        this(user, accessPermits);
        this.sessionLog = new SessionLog(sessionLog.getId(), sessionLog.getSessionId());
    }

    public UserPrincipal(User user, Collection<IPermit> accessPermits, SessionLog sessionLog, OrganisationUnit unit) {
        this(user, accessPermits, sessionLog);
        if (unit != null) {
            this.userOrganisationId = unit.getId();
            this.organsitionName = unit.getLabel();
            if(unit.isCompanyRoot()) {
                this.userOrganisationRootId = unit.getId();
            } else {
                this.userOrganisationRootId = unit.getRootId();
            }
        }
    }

    private void assignHomePages(Set<HomePage> pages) {
        for (Iterator iterator = pages.iterator(); iterator.hasNext();) {
            HomePage hp = (HomePage) iterator.next();
            homePages.put(hp.getArenaId(), new HomePage(hp.getArenaId(), hp.getData(), hp.getUrl(), hp.getLabel(), hp.isInternalUrl()));
        }
    }

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Collection<IPermit> getAccessPermits() {
        return accessPermits;
    }

    public String getUserType() {
        return userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    /**
     * Check if user has the permit.
     *
     * @param permit The permit
     * @return true if the user has the specified permit
     */
    public boolean hasAccessPermit(IPermit permit) {
        return accessPermits.contains(permit);
    }

    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("UserPrincipal[");
        stringBuffer.append("\r\n id=").append(id);
        stringBuffer.append("\r\n username=").append(username);
        stringBuffer.append("\r\n firstName=").append(firstName);
        stringBuffer.append("\r\n lastName=").append(lastName);
        stringBuffer.append("\r\n userType=").append(userType);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    public User getUser() {
        return new User(id, username, firstName, lastName);
    }

    public HomePage getHomePage(String currentArenaId) {
        return homePages.get(currentArenaId);
    }

    public boolean isRoot() {
        return root;
    }

    public SessionLog getSessionLog() {
        return sessionLog;
    }

    public Collection<Role> getUserRoles() {
        return userRoles;
    }

    public Group getUserGroup() {
        return group;
    }

    public boolean isSuperUser() {
        return superUser;
    }

    public Long getUserOrganisationId() {
        return userOrganisationId;
    }

    public Long getUserOrganisationRootId() {
        return userOrganisationRootId;
    }

    public String getOrgansitionName() {
        return organsitionName;
    }

    private final Long id;
    private final boolean administrator;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String userType;
    private final Long subjectId;
    private Collection<Role> userRoles;
    private Collection<IPermit> accessPermits = new ArrayList<IPermit>();

    private static final Long ADMINISTRATOR_USER_ID = new Long(1);
    private Map<String, HomePage> homePages;
    private SessionLog sessionLog;
    private boolean root;
    private Group group;
    private boolean superUser;
    private Long userOrganisationId;
    private Long userOrganisationRootId;
    private String organsitionName;
}
