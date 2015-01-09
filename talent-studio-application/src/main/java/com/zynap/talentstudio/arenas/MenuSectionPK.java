/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.arenas;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MenuSectionPK implements Serializable {

    public MenuSectionPK() {
    }

    public MenuSectionPK(String id, String arenaId) {
        this.id = id;
        this.arenaId = arenaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArenaId() {
        return arenaId;
    }

    public void setArenaId(String arenaId) {
        this.arenaId = arenaId;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuSectionPK)) return false;

        final MenuSectionPK menuSectionPK = (MenuSectionPK) o;

        if (id != null ? !id.equals(menuSectionPK.id) : menuSectionPK.id != null) return false;
        if (arenaId != null ? !arenaId.equals(menuSectionPK.arenaId) : menuSectionPK.arenaId != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 29 * result + (arenaId != null ? arenaId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "section id [" + id + "] module id [" + arenaId + "]";
    }

    private String id;
    private String arenaId;
}
