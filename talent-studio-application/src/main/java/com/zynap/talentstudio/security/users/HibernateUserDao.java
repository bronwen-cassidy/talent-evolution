/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.users;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;

import com.zynap.common.persistence.ZynapPersistenceSupport;
import com.zynap.domain.IDomainObject;
import com.zynap.domain.QueryParameter;
import com.zynap.domain.SearchAdaptor;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.security.rules.Config;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HibernateUserDao extends ZynapPersistenceSupport implements IUserDao {

    public Class getDomainObjectClass() {
        return User.class;
    }

    public String encrypt(String password) {
        return new EncryptPassword(getJdbcTemplate()).execute(password);
    }

    public String decrypt(String password) {
        return new DecryptPassword(getJdbcTemplate()).execute(password);
    }

    /**
     * Delete the user.
     * <br> Overriden to ensure that the user is cleared from any associated subjects.
     * <br> The core detail gets deleted if there are no subjects associated with the user.
     *
     * @param domainObject
     * @throws TalentStudioException
     */
    public void delete(IDomainObject domainObject) throws TalentStudioException {

        User user = (User) domainObject;

        Collection questionnaires = getHibernateTemplate().find("from Questionnaire q where q.user.id = " + user.getId());
        for (Object questionnaire : questionnaires) {
            Questionnaire q = (Questionnaire) questionnaire;
            q.setUser(null);
            getHibernateTemplate().update(q);
        }
        Collection questionnairesWF = getHibernateTemplate().find("from QuestionnaireWorkflow q where q.userId = " + user.getId());
        for (Object aQuestionnairesWF : questionnairesWF) {
            QuestionnaireWorkflow q = (QuestionnaireWorkflow) aQuestionnairesWF;
            q.setUserId(null);
            getHibernateTemplate().update(q);
        }
        // break association between user and subject - do this as you don't want to delete the subject            
        final Collection associatedSubjects = user.getSubjects();
        for (Object associatedSubject : associatedSubjects) {
            Subject subject = (Subject) associatedSubject;
            subject.setUser(null);
        }

        getHibernateTemplate().delete("from MessageItem messages where messages.toUserId = " + user.getId());

        super.delete(user);

        if (associatedSubjects.isEmpty()) {
            getHibernateTemplate().delete(user.getCoreDetail());
        }
    }

    /**
     * Search - excludes root users and current user.
     *
     * @param principalId The id of the use doing the search
     * @param query
     * @return Collection of Users
     */
    public Collection search(Long principalId, SearchAdaptor query) {
        StringBuffer sql = new StringBuffer("select user from User user where user.root = 'F' and user.id <> ").append(principalId.intValue());
        Map mappedValues = query.getMappedResults();

        for (Object o : mappedValues.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = (String) entry.getKey();
            QueryParameter qp = (QueryParameter) entry.getValue();
            qp.buildClause("user", key, sql);
        }

        // order by user name
        sql.append(" order by upper(user.loginInfo.username)");

        //noinspection unchecked
        return new HashSet(getHibernateTemplate().find(sql.toString()));
    }

    public LoginInfo getLoginInfo(String username, String password) {
        String queryString = "from LoginInfo info where info.username=? and info.password=?";
        Collection result;
        try {
            result = getHibernateTemplate().find(queryString, new Object[]{username, password});
        } catch (Exception e) {
            return null;
        }
        if (result.isEmpty()) return null;
        return (LoginInfo) result.iterator().next();
    }

    public LoginInfo getLoginInfo(Long userId) {
        String sql = "select info from LoginInfo info where info.user.id=? ";
        Collection result = getHibernateTemplate().find(sql, userId);
        if (result.isEmpty()) return null;
        return (LoginInfo) result.iterator().next();
    }

    /**
     * Get all users except root-level user ("zynapsys").
     *
     * @return Collection of {@link com.zynap.domain.admin.User} objects.
     */
    public List<User> getAppUsers() {
        //noinspection unchecked
        return getHibernateTemplate().findByNamedQuery("findAppUsers");
    }

    public Config findConfig(Long configId) {
        return (Config) getHibernateTemplate().load(Config.class, configId);
    }

    public LoginInfo getLoginInfo(String username) {
        String queryString = "from LoginInfo info where info.username=?";
        Collection result = getHibernateTemplate().find(queryString, new Object[]{username});
        if (result.isEmpty()) return null;
        return (LoginInfo) result.iterator().next();
    }

    public SessionLog getSessionLog(Long sessionId) {
        return (SessionLog) getHibernateTemplate().load(SessionLog.class, sessionId);
    }

    public void create(SessionLog sessionLog) throws TalentStudioException {
        try {
            getHibernateTemplate().save(sessionLog);
        } catch (DataAccessException e) {
            throw new TalentStudioException("Duplicate session", e);
        }
    }

    public void update(SessionLog sessionLog) {
        getHibernateTemplate().update(sessionLog);
    }

    /**
     * Find user by username.
     *
     * @param username The username
     * @return User
     * @throws TalentStudioException
     */
    public User findByUserName(String username) throws TalentStudioException {
        String queryString = "from User user where user.loginInfo.username=?";
        Collection result = getHibernateTemplate().find(queryString, new Object[]{username});
        if (result.isEmpty()) throw new DomainObjectNotFoundException(getDomainObjectClass(), "username", username);
        return (User) result.iterator().next();
    }

    public OrganisationUnit getUserDefaultOrganisationUnit(String username) throws TalentStudioException {
        String queryString = "select sa.position.organisationUnit from SubjectAssociation sa, User user where \n" +
                " sa.subject.user.id = user.id and user.loginInfo.username=? ";
        Collection result = getHibernateTemplate().find(queryString, new Object[]{username});
        if (result.isEmpty()) return (OrganisationUnit) findById(OrganisationUnit.class, OrganisationUnit.ROOT_ORG_UNIT_ID);
        return (OrganisationUnit) result.iterator().next();
    }

    public User findBySubjectId(Long subjectId) throws DomainObjectNotFoundException {

        try {
            String queryString = "select s.user from Subject s where s.id=?";
            List<User> users = getHibernateTemplate().find(queryString, new Object[]{subjectId});
            if(!users.isEmpty()) {
                return users.get(0);
            }
            //final Subject subject = (Subject) findById(Subject.class, subjectId);
            //if (subject.getUser() != null)
            //    return subject.getUser();
            else
                throw new DomainObjectNotFoundException(User.class, "subjectId", subjectId);
        } catch (TalentStudioException e) {
            throw new DomainObjectNotFoundException(User.class, "subjectId", subjectId);
        }
    }

    public List<UserDTO> listAppUsers() {
        try {
            StringBuffer query = new StringBuffer("select new ");
            query.append(UserDTO.class.getName()).append(" ( user.coreDetail.firstName, user.coreDetail.secondName, user.id, user.active ) ");
            query.append(" from User user where user.root='F' and user.id <> 1 and user.active = 'T'");
            //noinspection unchecked
            return getHibernateTemplate().find(query.toString());

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserDTO> findSystemUsers() {
        StringBuffer query = new StringBuffer("select new ");
        query.append(UserDTO.class.getName()).append(" ( user.coreDetail.firstName, user.coreDetail.secondName, user.id, user.active ) ");
        query.append(" from User user where user.root='F' and user.id <> 1 and user.active = 'T' and user.userType='SYSTEM' ");
        //noinspection unchecked
        return getHibernateTemplate().find(query.toString());
    }

    public List<User> find(Long[] ids) throws TalentStudioException {
        try {
            Session session = getSession(false);
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Expression.in("id", ids));
            //noinspection unchecked
            return criteria.list();
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new TalentStudioException(e);
        }
    }

    public void deleteHomePageGroups(Long groupId) throws TalentStudioException {

        try {
            getJdbcTemplate().update("update users set group_id = null where group_id = ?", new Object[]{groupId});
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new TalentStudioException(e);
        }
    }

    public void assignUserPermits(Long userId) throws TalentStudioException {
        new AssignPermits(getJdbcTemplate()).execute(userId);
    }

    public List<SessionLog> getOpenSessionLogs(Long userId) {
        return getHibernateTemplate().find("from SessionLog sessionLog where sessionLog.user.id=" + userId + " and sessionLog.status='OPEN'");
    }

    private static class EncryptPassword extends StoredProcedure {

        public EncryptPassword(JdbcTemplate template) {
            setSql("zynap_auth_sp.encrypt");
            setJdbcTemplate(template);
            setFunction(true);
            declareParameter(new SqlOutParameter("password_", Types.VARCHAR));
            declareParameter(new SqlParameter("text_", Types.VARCHAR));
        }

        public String execute(String text) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("text_", text);
            Map out = execute(in);
            return (String) out.get("password_");
        }
    }

    private static class DecryptPassword extends StoredProcedure {

        public DecryptPassword(JdbcTemplate template) {
            setSql("zynap_auth_sp.decrypt");
            setJdbcTemplate(template);
            setFunction(true);
            declareParameter(new SqlOutParameter("password_", Types.VARCHAR));
            declareParameter(new SqlParameter("text_", Types.VARCHAR));
        }

        public String execute(String text) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("text_", text);
            Map out = execute(in);
            return (String) out.get("password_");
        }
    }

    private static class AssignPermits extends StoredProcedure {

        public AssignPermits(JdbcTemplate template) {
            setSql("zynap_auth_sp.assignPermits");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("user_id_", Types.INTEGER));
        }

        public void execute(Long userId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("user_id_", userId);
            execute(in);
        }
    }
}
