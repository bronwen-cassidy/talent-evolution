/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.positions;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 15-Aug-2008 13:52:34
 */
public class PositionDto extends Position {

    public PositionDto(Long id, String title, boolean active, String organisationUnitLabel) {
        super(id, title);
        this.organisationUnitLabel = organisationUnitLabel;
        setActive(active);
    }

    public PositionDto(Long id, String title, boolean active, String organisationUnitLabel, String currentHolderInfo) {
        super(id, title);
        this.organisationUnitLabel = organisationUnitLabel;
        setActive(active);
        setCurrentHolderInfo(currentHolderInfo);
    }

    public String getOrganisationUnitLabel() {
        return organisationUnitLabel;
    }

    private String organisationUnitLabel;
}
