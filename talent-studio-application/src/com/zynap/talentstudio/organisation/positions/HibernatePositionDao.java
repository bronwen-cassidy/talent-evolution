package com.zynap.talentstudio.organisation.positions;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.NonUniqueObjectException;
import net.sf.hibernate.UnresolvableObjectException;

import com.zynap.common.persistence.ZynapPersistenceSupport;
import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.IAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.SecurityHelper;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.domain.UserSession;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 31-Jan-2005
 * Time: 15:26:55
 * <p/>
 * todo check queries to determine which ones need to check that the position is active.
 */
public class HibernatePositionDao extends ZynapPersistenceSupport implements IPositionDao {

    public Class getDomainObjectClass() {
        return Position.class;
    }

    public void checkAccess(Position position, Long userId, Long viewPositionPermitId, Long viewOrgUnitPermitId, Long viewSubjectPermitId) {
        if (position != null) {
            SecurityHelper.checkPositionAccess(position, userId, viewPositionPermitId, viewOrgUnitPermitId, viewSubjectPermitId, getHibernateTemplate());
        }
    }

    public void update(IDomainObject domainObject) {
        try {
            getHibernateTemplate().update(domainObject);
        } catch (DataAccessException e) {
            final Throwable throwable = e.getCause();
            if(throwable instanceof NonUniqueObjectException) {
                getHibernateTemplate().saveOrUpdateCopy(domainObject);
            } else throw e;
        }
    }

    public boolean checkNodeAccess(Node position, Long id, Long viewPositionPermitId) {
        SecurityHelper.checkNodeAccess(position, id, viewPositionPermitId, getHibernateTemplate());
        return position.isHasAccess();
    }

    public boolean checkNodeViewAccess(Node node, Long userId, Long viewPositionPermitId) {
        SecurityHelper.checkNodeViewAccess(node, userId, viewPositionPermitId, getHibernateTemplate());
        return node.isHasAccess();
    }

    public void deleteAssociation(IAssociation association) {
        getHibernateTemplate().delete(association);
    }

    public void deletePositions(Long positionId) throws TalentStudioException {
        final UserSession userSession = UserSessionFactory.getUserSession();
        new DeletePositionsCascade(getJdbcTemplate()).execute(positionId, userSession.getId(), userSession.getUserName());
    }

    public void refreshState(Position position) throws TalentStudioException {
        try {
            getSession(false).refresh(position);
        } catch (UnresolvableObjectException e) {
            // for now ignore as this is what we expected
        } catch (HibernateException e) {
            logger.error("Could not clear the state of position: " + position.getId());
            throw new TalentStudioException(e);
        }
    }

    public void updateCurrentHoldersInfo(Long positionId) {
        new UpdateCurrentHoldersInfo(getJdbcTemplate()).execute(positionId);
    }

    public List<Position> findAvailableParentsForPosition(Long orgUnitId, Long positionId, Long userId, Long permitId) {

        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("select p from Position p, UserDomainPermit dp where p.organisationUnit.id=? and p.active='T'");
        stringBuffer.append(" and p.id = dp.nodeId and dp.userId=? and dp.permitId=?");

        final List<Long> params = new ArrayList<Long>();
        params.add(orgUnitId);
        params.add(userId);
        params.add(permitId);

        // if position id specified, exclude children of the specified position to avoid having a child of the position being its parent!!
        if (positionId != null) {
            stringBuffer.append(" and not exists (select ph.id from PositionsHierarchyInc ph where ph.root.id = ? and ph.descendent.id = p.id)");
            params.add(positionId);
        }

        return getHibernateTemplate().find(stringBuffer.toString(), params.toArray());
    }

    public List<Position> findDescendents(Long id) throws TalentStudioException {
        String queryString = "select ph.descendent from PositionsHierarchy ph where ph.root.id = :positionId order by hlevel desc ";
        return getHibernateTemplate().findByNamedParam(queryString, "positionId", id);
    }

    public List<Long> findDescendentIds(Long id) throws TalentStudioException {
        String queryString = "select ph.descendent.id from PositionsHierarchy ph where ph.root.id = :positionId order by hlevel desc ";
        return getHibernateTemplate().findByNamedParam(queryString, "positionId", id);
    }

    public Position findReportStructureFor(Long positionId, Long principalId, Long viewPositionPermitId, Long viewOrgUnitPermitId, Long viewSubjectPermitId) throws TalentStudioException {

        Position position = (Position) findByID(positionId);
        SecurityHelper.checkPositionAccess(position, principalId, viewPositionPermitId, viewOrgUnitPermitId, viewSubjectPermitId, getHibernateTemplate());
        // check grandparent
        final Position parent = position.getParent();
        if(parent != null) {
            final Position grandParent = parent.getParent();
            if(grandParent != null) {
                SecurityHelper.checkNodeAccess(grandParent, principalId, viewPositionPermitId, getHibernateTemplate());
            }
        }
        return position;
    }

    public Position findUsersPosition(Long userId, Long orgUnitId) {
        StringBuilder sql = new StringBuilder("select spa.position from SubjectPrimaryAssociation spa, Subject s ");
        sql.append("where s.user.id = ? and spa.subject.id=s.id ");
        List<Position> positions = getHibernateTemplate().find(sql.toString(), new Object[]{userId});
        return positions.isEmpty() ? null : positions.get(0);
    }

    private class UpdateCurrentHoldersInfo extends StoredProcedure {

        private UpdateCurrentHoldersInfo(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_node_sp.update_current_holder_info");
            declareParameter(new SqlParameter("position_id_", Types.INTEGER));
        }

        public void execute(Long id) {
            Map<String, Long> in = new HashMap<String, Long>();
            in.put("position_id_", id);
            execute(in);
        }
    }

    private class DeletePositionsCascade extends StoredProcedure {

        public DeletePositionsCascade(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_position_sp.delete_position_cascade");
            declareParameter(new SqlParameter("position_id_", Types.INTEGER));
            declareParameter(new SqlParameter("user_id_", Types.INTEGER));
            declareParameter(new SqlParameter("username_", Types.VARCHAR));
        }

        public void execute(Long id, Long userId, String userName) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("position_id_", id);
            in.put("user_id_", userId);
            in.put("username_", userName);
            execute(in);
        }
    }
}
