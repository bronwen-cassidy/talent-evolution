/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.admin;

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.security.roles.AccessRole;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.util.collections.DomainObjectCollectionHelper;
import com.zynap.talentstudio.web.utils.controller.RoleUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class UserWrapperBean implements Serializable {


    public UserWrapperBean(User user) {
        this(user, user.getUserRoles());
    }

    public UserWrapperBean(User user, Collection<Role> allRoles) {
        if (user == null) user = new User();
        this.user = user;
        this.homePageGroup = user.getGroup();
        if(homePageGroup == null) homePageGroup = new Group();
        this.groupId = homePageGroup.getId();
        userRoles = new ArrayList<Role>(user.getUserRoles());
        accessRoles = RoleUtils.initRoles(allRoles, user.getUserRoles());
        RoleUtils.sortRoles(accessRoles);
    }

    /**
     * Reset ids.
     */
    public void resetIds() {
        user.setId(null);        
        getLoginInfo().setId(null);
        getCoreDetail().setId(null);
    }

    /**
     * Get modified user.
     * <br/> Assigns roles back to user.
     *
     * @return User
     */
    public User getModifiedUser() {

        // clear current roles
        user.getUserRoles().clear();

        // retrieve the home role (all users should have this role by default)
        AccessRole homeRole = (AccessRole) DomainObjectCollectionHelper.findById(accessRoles, Role.HOME_ROLE_ID);
                                                                               
        // providing the home role is not null.. add it to the user
        if (homeRole != null) {
            user.addRole(homeRole);
        }

        // add afresh
        for (Iterator iterator = accessRoles.iterator(); iterator.hasNext();) {
            Role role = (Role) iterator.next();
            if (role.isAssigned()) {
                user.addRole(role);
            }
        }

        if(groupId != null) {
            // find the group
            for(Group group : groups) {
                if(groupId.equals(group.getId())) {
                    user.setGroup(group);
                }
            }
        } else {
            user.setGroup(null);
        }

        return user;
    }

    /**
     * Get list of roles assigned to user.
     * <br/> Used for viewing user - sorted alphabetically.
     *
     * @return Collection of Roles
     */
    public Collection<Role> getUserRoles() {
        return userRoles;
    }

    /**
     * Returns true only if there is at least one item in the list (home role)
     *
     * @return boolean
     */
    public boolean isUserRolesEmpty() {
        return userRoles == null || userRoles.size() < 2;
    }

    /**
     * Get list of all roles - the ones the user has will be marked as assigned.
     * <br/> Used for editing / adding users.
     *
     * @return Collection of Roles
     */
    public Collection<Role> getAccessRoles() {
        return accessRoles;
    }

    /**
     * Spring binding method.
     *
     * @param roleIds setter used by spring binding
     */
    public void setAccessRoleIds(Long[] roleIds) {

        // make all roles as unassigned
        clearRoles();
        RoleUtils.updateAssigned(roleIds, accessRoles);

    }

    /**
     * Spring binding method.
     *
     * @return new Long[0]
     */
    public Long[] getAccessRoleIds() {
        return new Long[0];
    }

    /**
     * Sets all roles to be unassigned.
     */
    public void clearRoles() {
        RoleUtils.clearRoles(accessRoles);
    }

    public LoginInfo getLoginInfo() {
        if (user.getLoginInfo() == null) user.setLoginInfo(new LoginInfo());
        return user.getLoginInfo();
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        user.setLoginInfo(loginInfo);
    }

    public CoreDetail getCoreDetail() {
        if (user.getCoreDetail() == null) user.setCoreDetail(new CoreDetail());
        return user.getCoreDetail();
    }

    public void setCoreDetail(CoreDetail coreDetail) {
        user.setCoreDetail(coreDetail);
    }

    public void setTitle(String title) {
        getCoreDetail().setTitle(title);
    }

    public String getTitle() {
        return getCoreDetail().getTitle();
    }

    public void setFirstName(String firstName) {
        getCoreDetail().setFirstName(firstName);
    }

    public String getFirstName() {
        return getCoreDetail().getFirstName();
    }

    public void setSecondName(String name) {
        getCoreDetail().setSecondName(name);
    }

    public String getSecondName() {
        return getCoreDetail().getSecondName();
    }

    public void setContactTelephone(String telephone) {
        getCoreDetail().setContactTelephone(telephone);
    }

    public String getContactTelephone() {
        return getCoreDetail().getContactTelephone();
    }

    public void setContactEmail(String email) {
        getCoreDetail().setContactEmail(email);
    }

    public String getContactEmail() {
        return getCoreDetail().getContactEmail();
    }

    public void setPrefGivenName(String name) {
        getCoreDetail().setPrefGivenName(name);
    }

    public String getPrefGivenName() {
        return getCoreDetail().getPrefGivenName();
    }

    public boolean isActive() {
        return user.isActive();
    }

    public void setActive(boolean active) {
        user.setActive(active);
    }

    public boolean isRoot() {
        return user.isRoot();
    }

    public Long getId() {
        return user.getId();
    }

    public void clearPasswordFields() {
        getLoginInfo().setPassword(null);
        getLoginInfo().setRepeatedPassword(null);
    }

    public Group getHomePageGroup() {
        return homePageGroup;
    }

    public void setHomePageGroup(Group homePageGroup) {
        this.homePageGroup = homePageGroup;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    private User user;

    /**
     * Collection that holds all roles in the app, not just the ones assigned to the user (used for edit.)
     */
    private List<Role> accessRoles;

    /**
     * Collection that holds all roles assigned to user (used for view.)
     */
    private List<Role> userRoles;
    private Group homePageGroup;
    private List<Group> groups = new ArrayList<Group>();
    private Long groupId;
}
