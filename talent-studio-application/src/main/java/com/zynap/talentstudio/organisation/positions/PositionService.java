/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.positions;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 */
public class PositionService extends DefaultService implements IPositionService {

    public void checkAccess(Position position) {
        final Long id = UserSessionFactory.getUserSession().getId();
        positionDao.checkAccess(position, id, getViewPositionPermitId(), getViewOrgUnitPermitId(), getViewSubjectPermitId());
    }

    public boolean checkNodeAccess(Node position, Long userId) {
        return positionDao.checkNodeAccess(position, userId, getViewPositionPermitId());
    }

    public boolean checkNodeViewAccess(Node node, Long userId) {
        return positionDao.checkNodeViewAccess(node, userId, getViewPositionPermitId());
    }

    protected IFinder getFinderDao() {
        return positionDao;
    }

    protected IModifiable getModifierDao() {
        return positionDao;
    }

    public List<Position> findAvailableParentsForPosition(Long orgUnitId, Long positionId, Long userId) {
        return positionDao.findAvailableParentsForPosition(orgUnitId, positionId, userId, getViewPositionPermitId());
    }

    public List search(Long principalId, PositionSearchQuery searchQuery) throws TalentStudioException {
        Population population = buildPopulation(searchQuery);
        return populationEngine.find(population, principalId);
    }

    public List<PositionDto> searchPositions(Long userId, PositionSearchQuery searchQuery) throws TalentStudioException {
        Population population = buildPopulation(searchQuery);
        return populationEngine.findPositions(population, userId);
    }

    private Population buildPopulation(PositionSearchQuery searchQuery) throws TalentStudioException {
        Population population;
        if (searchQuery.getPopulationId() == null)
            population = populationEngine.getAllPositionsPopulation();
        else
            population = (Population) analysisService.findById(searchQuery.getPopulationId());

        population.setForSearching(true);
        population.setOrderColumns(new String[]{PositionSearchQuery.TITLE});
        searchQuery.getPopulationForSearch(population);
        return population;
    }

    public Position findReportStructureFor(Long positionId, Long principalId) throws TalentStudioException {
        return positionDao.findReportStructureFor(positionId, principalId, getViewPositionPermitId(), getViewOrgUnitPermitId(), getViewSubjectPermitId());
    }

    public Position findUsersPosition(Long userId, Long orgUnitId) {
        return positionDao.findUsersPosition(userId, orgUnitId);
    }

    public List<Position> findDescendents(Long positionId) throws TalentStudioException {
        return positionDao.findDescendents(positionId);
    }

    public List<Long> findDescendentIds(Long positionId) throws TalentStudioException {
        return positionDao.findDescendentIds(positionId);
    }

    public void deletePosition(Long positionId) throws TalentStudioException {
        positionDao.deletePositions(positionId);        
    }

    
    public void deleteAll(List<Position> positions) throws TalentStudioException {

        for (Position original : positions) {

            // clear the parent
            Position parent = original.getParent();
            if (parent != null) {
                parent.removeChild(original);
            }

            OrganisationUnit organisationUnit = original.getOrganisationUnit();
            if (organisationUnit != null) {
                
                // clear the organisation
                organisationUnit.removePosition(original);
                original.setOrganisationUnit(null);
                positionDao.delete(original);
            }
        }
    }

    public void refreshState(Position position) throws TalentStudioException {
        positionDao.refreshState(position);
    }

    public void updateCurrentHoldersInfo(Long id) {
        positionDao.updateCurrentHoldersInfo(id);
    }

    public void updateStateInfo(IDomainObject domainObject) {
        updateCurrentHoldersInfo(domainObject.getId());
    }

    private Long getViewPositionPermitId() {
        return getViewPermit().getId();
    }

    private Long getViewSubjectPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.SUBJECT_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    private Long getViewOrgUnitPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.ORGANISATION_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    private IPermit getViewPermit() {
        if (viewPermitId == null) {
            viewPermitId = permitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.VIEW_ACTION);
        }
        return viewPermitId;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setPositionDao(IPositionDao positionDao) {
        this.positionDao = positionDao;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    private IPermitManagerDao permitManagerDao;
    private IPopulationEngine populationEngine;
    private IPositionDao positionDao;
    private IAnalysisService analysisService;

    private static IPermit viewPermitId;
}
