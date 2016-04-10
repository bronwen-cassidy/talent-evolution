package com.zynap.talentstudio.organisation;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.objectives.IObjectiveDao;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: amark
 * Date: 13-Jul-2005
 * Time: 15:58:03
 */
public class OrganisationUnitService extends DefaultService implements IOrganisationUnitService {

    public OrganisationUnit findById(Long orgUnitId) throws TalentStudioException {
        return organisationUnitDao.findById(orgUnitId);
    }

    public OrganisationUnit findOrgUnitByPositionId(String positionId) throws TalentStudioException {
        return organisationUnitDao.findOrgUnitByPositionId(new Long(positionId));
    }

    public OrganisationUnit findOrgUnitBySubjectId(String subjectId) {
        List list = organisationUnitDao.findOrgUnitsBySubjectId(new Long(subjectId));
        if (list.size() > 0)
            return (OrganisationUnit) list.get(0);
        else
            return null;
    }

    public List<OrganisationUnit> findValidParents(Long orgUnitId, Long userId) {
        //noinspection unchecked
        return organisationUnitDao.findValidParents(orgUnitId, userId, getViewPermitId());
    }

    public List<OrganisationUnit> findValidParents(Long userId) {
        return organisationUnitDao.findValidParents(null, userId, getViewPermitId());
    }

    public List<OrganisationUnit> findAllSecure() {
        //noinspection unchecked
        return organisationUnitDao.findAllSecure(UserSessionFactory.getUserSession().getId(), getViewPermitId());
    }

    public Collection search(Long userId, OrgUnitSearchQuery query) {
        return organisationUnitDao.search(query, userId, getViewPermitId());
    }

    public void updateMerge(OrganisationUnit mergedOrganisationUnit, OrganisationUnit defunctOrganisationUnit) throws TalentStudioException {

        final Set<OrganisationUnit> children = new HashSet<OrganisationUnit>(defunctOrganisationUnit.getChildren());
        for (OrganisationUnit unit : children) {
            unit.setParent(mergedOrganisationUnit);
            update(unit);
        }

        final Set<Position> positions = new HashSet<Position>(defunctOrganisationUnit.getPositions());
        for (Position position : positions) {
            position.setOrganisationUnit(mergedOrganisationUnit);
            positionService.update(position);
        }

        Collection<ObjectiveSet> objectiveSets = new ArrayList<ObjectiveSet>(defunctOrganisationUnit.getObjectiveSets());
        for (ObjectiveSet objectiveSet: objectiveSets) {
            objectiveSet.setOrganisationUnit(mergedOrganisationUnit);
            objectiveDao.update(objectiveSet);
        }

        update(mergedOrganisationUnit);
        defunctOrganisationUnit.setChildren(new HashSet<OrganisationUnit>());
        defunctOrganisationUnit.setPositions(new HashSet<Position>());
        defunctOrganisationUnit.setObjectiveSets(new HashSet<ObjectiveSet>());
        delete(defunctOrganisationUnit);
    }

    public void deleteOrgUnitCascade(OrganisationUnit organisationUnit) throws TalentStudioException {
        organisationUnitDao.deleteOrganisationCascade(organisationUnit);
    }

    public void delete(IDomainObject domainObject) throws TalentStudioException {
        super.delete(domainObject);        
    }

    public OrganisationUnit findOrgUnitBySubjectUserId(String userId) {
        return organisationUnitDao.findOrgUnitBySubjectUserId(new Long(userId));
    }

    public OrganisationUnit findOrgUnitByUser(String userId) {
        OrganisationUnit ou = organisationUnitDao.findOrgUnitByUserId(new Long(userId), getViewPermitId());
        if(ou == null) {
            ou = findOrgUnitBySubjectUserId(userId);
        }
        if(ou == null) {
            try {
                ou = organisationUnitDao.findById(OrganisationUnit.ROOT_ORG_UNIT_ID);
            } catch (TalentStudioException e) {
                e.printStackTrace();
            }
        }
        return ou;
    }

    public List<OrganisationUnit> findOrgUnitTree(final Long organisationUnitId) throws TalentStudioException {
        return organisationUnitDao.findOrgUnitTree(organisationUnitId);
    }

    public List<OrganisationUnit> findSecureOrgUnitTree(Long userId) {
        return organisationUnitDao.findSecureOrgUnitTree(userId, getViewPermitId());
    }

    public void setOrganisationUnitDao(IOrganisationDao organisationUnitDao) {
        this.organisationUnitDao = organisationUnitDao;
    }

    public IPermitManagerDao getPermitManagerDao() {
        return permitManagerDao;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    public IPositionService getPositionService() {
        return positionService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }


    public void setObjectiveDao(IObjectiveDao objectiveDao) {
        this.objectiveDao = objectiveDao;
    }

    protected IFinder getFinderDao() {
        return organisationUnitDao;
    }

    protected IModifiable getModifierDao() {
        return organisationUnitDao;
    }

    public IJdbcOrganisationDao getJdbcOrganisationDao() {
        return jdbcOrganisationDao;
    }

    public void setJdbcOrganisationDao(IJdbcOrganisationDao jdbcOrganisationDao) {
        this.jdbcOrganisationDao = jdbcOrganisationDao;
    }

    private Long getViewPermitId() {
        if (viewPermitId == null) {
            viewPermitId = permitManagerDao.getViewOrgUnitPermitId();
        }

        return viewPermitId;
    }

    private static Long viewPermitId;

    private IPermitManagerDao permitManagerDao;

    private IOrganisationDao organisationUnitDao;

    private IPositionService positionService;

    private IObjectiveDao objectiveDao;

    private IJdbcOrganisationDao jdbcOrganisationDao;
}
