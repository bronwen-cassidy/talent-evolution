/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.exception.TalentStudioException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.StoredProcedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class JdbcOrganisationManagerDao extends JdbcDaoSupport implements IJdbcOrganisationDao {

    /**
     * Constructor.
     */
    public JdbcOrganisationManagerDao() {
    }

    /**
     * Disable (make inactive) the selected org unit, its children org units and the positions belonging to the
     * parent org units and the children.
     *
     * @param organisationUnit
     * @param userId
     * @throws TalentStudioException
     */
    public void disable(com.zynap.talentstudio.organisation.OrganisationUnit organisationUnit, Long userId) throws TalentStudioException {
        final String sql = "zynap_org_unit_sp.delete_org_unit";
        DeleteProcedure deleteProc = new DeleteProcedure(getJdbcTemplate(), sql);
        deleteProc.execute(organisationUnit.getId(), userId);

    }

    private static class DeleteProcedure extends StoredProcedure {

        public DeleteProcedure(JdbcTemplate template, String sql) {
            super(template, sql);
            declareParameter(new SqlParameter("org_unit_id_", Types.INTEGER));
            declareParameter(new SqlParameter("user_id_", Types.INTEGER));
            compile();
        }

        public void execute(Serializable organisationUnitId, Long userId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("org_unit_id_", organisationUnitId);
            in.put("user_id_", userId);
            execute(in);
        }
    }

}