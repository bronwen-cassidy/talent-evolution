/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 26-Mar-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.web;

import com.zynap.domain.admin.User;

import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.OrganisationUnit;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Mar-2007 13:04:39
 */
public class NodeInfo implements Serializable {


    public NodeInfo(Subject subject, List<Position> positions, List<OrganisationUnit> organisationUnits) {
        this.subject = subject;
        this.positions = positions;
        this.organisationUnits = organisationUnits;
    }

    public NodeInfo(OrganisationUnit organisationUnit) {
        organisationUnits.add(organisationUnit);
    }

    public Subject getSubject() {
        return subject;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public List<OrganisationUnit> getOrganisationUnits() {
        return organisationUnits;
    }

    private Subject subject;
    private List<Position> positions = new ArrayList<Position>();
    private List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();
}
