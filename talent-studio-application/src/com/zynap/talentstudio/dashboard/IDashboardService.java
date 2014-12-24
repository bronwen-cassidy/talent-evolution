/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.dashboard;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
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
 * @since 17-May-2010 17:36:05
 */
public interface IDashboardService {

    List<Dashboard> findAll() throws TalentStudioException;

    IDomainObject findById(Serializable id) throws TalentStudioException;

    void delete(Serializable dashboardId) throws TalentStudioException;

    void createOrUpdate(Dashboard result) throws TalentStudioException;

    List<Dashboard> findSubjectDashboards(Group userGroup, Collection<Role> userRoles, Subject subject);

    List<Dashboard> findPersonalDashboards(Subject subject);
}
