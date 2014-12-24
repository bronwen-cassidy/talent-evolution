/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.common.persistence;

import com.zynap.talentstudio.common.HibernateCrudAdaptor;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A support class that provides both a hibernateTemplate and a jdbcTemplate
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class ZynapPersistenceSupport extends HibernateCrudAdaptor {


    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private JdbcTemplate jdbcTemplate;
}
