/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.dao.DataAccessException;

import java.sql.Types;
import java.util.Map;
import java.util.HashMap;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 08-Mar-2008 11:49:35
 */
public class JdbcSecurityManager extends JdbcDaoSupport {

    public void refreshLoggedInUsersPermits() {
        try {
            new DomainPermitsUpdater(getJdbcTemplate()).execute();
        } catch (DataAccessException e) {
            throw e;
        }
    }

    public void refreshAllUsersPermits() {
        try {
            new RefreshAllDomainPermitsUpdater(getJdbcTemplate()).execute();
        } catch (DataAccessException e) {
            throw e;
        }
    }

    private static class DomainPermitsUpdater extends StoredProcedure {

        public DomainPermitsUpdater(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_app_sp.add_users_permits");
            setJdbcTemplate(jdbcTemplate);
        }

        public void execute() {
            super.execute(new HashMap());
        }
    }

    /**
     * daily run batch job to refresh all permits for all users
     */
    private static class RefreshAllDomainPermitsUpdater extends StoredProcedure {

        public RefreshAllDomainPermitsUpdater(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_app_sp.refresh_all_cache_permits");
            setJdbcTemplate(jdbcTemplate);
        }

        public void execute() {
            super.execute(new HashMap());
        }
    }
}
