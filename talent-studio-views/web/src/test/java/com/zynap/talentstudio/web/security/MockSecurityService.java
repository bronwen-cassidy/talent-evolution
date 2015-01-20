package com.zynap.talentstudio.web.security;

import com.zynap.domain.UserPrincipal;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by bronwen.
 * Date: 26/07/12
 * Time: 11:45
 */
public class MockSecurityService implements ISecurityManager {

    public void setCheckAccess(boolean checkAccess) {
        this.checkAccess = checkAccess;
    }

    public void checkAccess(IPermit permit, UserPrincipal user, Node node) throws DomainObjectNotFoundException {
    }

    public boolean checkAccess(IPermit permit, UserPrincipal user, Long nodeId) throws DomainObjectNotFoundException {
        return this.checkAccess;
    }

    public Collection getAllDomains() {
        return null;
    }

    public SecurityDomain findDomain(Long domainId) throws TalentStudioException {
        return null;
    }

    public boolean isDomainMember(Long domainId, Long userId) throws TalentStudioException {
        return false;
    }

    public void createDomain(SecurityDomain domain) throws TalentStudioException {
    }

    public void updateDomain(SecurityDomain domain) throws TalentStudioException {
    }

    public void deleteDomain(Long domainId) throws TalentStudioException {
    }

    public Area findArea(Long areaId) throws TalentStudioException {
        return null;
    }

    public Collection<Area> getAreas() {
        return null;
    }

    public void createArea(Area area) {
    }

    public void updateArea(Area area) throws TalentStudioException {
    }

    public void deleteArea(Long areaId) throws TalentStudioException {
    }

    public List<SecurityDomain> getUserDomains(Long userId) {
        return null;
    }

    public void createAreaElements(Area updatedArea, Set<AreaElement> popElements) throws TalentStudioException {
    }

    public Set<AreaElement> getAreaElements(Long areaId) {
        return null;
    }

    public void removeAreaElements(Area area) {
    }

    public Collection<Area> getPopulationAreas() {
        return null;
    }

    private boolean checkAccess;
}
