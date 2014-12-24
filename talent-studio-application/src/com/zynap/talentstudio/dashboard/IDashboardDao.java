/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.dashboard;

import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.roles.Role;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2010 17:45:05
 */
public interface IDashboardDao extends IFinder, IModifiable {

    void addParticipants(Dashboard dashboard, Collection<Subject> results);

    void deleteParticipants(Serializable dashboardId);

    List<Dashboard> findSubjectDashboards(Group userGroup, Collection<Role> userRoles, Subject subject);

    List<Dashboard> findPersonalDashboards(Subject subject);
}
