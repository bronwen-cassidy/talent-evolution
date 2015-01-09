package com.zynap.talentstudio.organisation.subjects;

/**
 * User: amark
 * Date: 04-Feb-2005
 * Time: 16:23:27
 */

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserType;
import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.security.roles.HibernateRoleManagerDao;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.users.IUserService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TestSubjectService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        subjectService = (ISubjectService) getBean("subjectService");
        userService = (IUserService) applicationContext.getBean("userService");
        hibernateRoleManagerDao = (HibernateRoleManagerDao) applicationContext.getBean("roleManDao");
    }

    public void testCreateWithAssociation() throws Exception {

        CoreDetail coreDetail = new CoreDetail("Angus", "Mark", "amark", "Mr", "angus@one.com", "0889776876");
        Subject subject = new Subject(coreDetail);
        SubjectAssociation subjectAssociation = new SubjectAssociation(ACTING_SUBJECT_PRIMARY_QUALIFIER, subject, DEFAULT_POSITION);
        subject.addSubjectAssociation(subjectAssociation);
        subject.setUser(null);
        subjectService.create(subject);
        Subject newSubject = subjectService.findById(subject.getId());
        assertFalse(newSubject.isCanLogIn());
        assertTrue(newSubject.isActive());
        assertTrue(subject.getUser() == null);

        assertNotNull(subjectAssociation.getId());
        assertEquals(1, subject.getSubjectAssociations().size());
    }

    public void testCreate() throws Exception {

        CoreDetail coreDetail = new CoreDetail("Angus", "Mark", "amark", "Mr", "angus@one.com", "0889776876");
        Subject subject = new Subject(coreDetail);
        subject.setUser(null);
        subjectService.create(subject);
        Subject newSubject = subjectService.findById(subject.getId());
        assertFalse(newSubject.isCanLogIn());
        assertTrue(newSubject.isActive());
        assertTrue(subject.getUser() == null);
    }


    public void testFindUserBySubjectId() throws Exception {

        final Subject subject = buildSubjectWithUser();
        subjectService.create(subject);

        final User user = userService.findBySubjectId(subject.getId());
        assertEquals(subject.getUser(), user);
    }

    public void testCreateSubjectWithUser() throws Exception {

        Subject subject = buildSubjectWithUser();
        subjectService.create(subject);

        Subject newSubject = subjectService.findById(subject.getId());
        assertTrue(newSubject.isCanLogIn());
        assertTrue(newSubject.isActive());
        assertEquals(UserType.SUBJECT.toString(), subject.getUser().getUserType());
    }

    public void testCreateWithUserNoPasswordHistoryAdded() throws Exception {
        Subject original = buildSubjectWithUser();
        subjectService.create(original);
        Subject expected = subjectService.findById(original.getId());
        assertEquals(1, expected.getUser().getLoginInfo().getPasswordsHistory().size());
    }

    public void testUpdateSubjectWithUser() throws Exception {

        Subject subject = buildSubjectWithUser();
        subjectService.create(subject);

        Subject newSubject = subjectService.findById(subject.getId());
        newSubject.getCoreDetail().setContactTelephone("123455654");
        newSubject.getUser().addRole(hibernateRoleManagerDao.findRole(new Long(1)));
        newSubject.getUser().getLoginInfo().setPassword("tester5567");
        subjectService.update(newSubject);
        Subject actual = subjectService.findById(newSubject.getId());
        assertEquals(actual.getCoreDetail(), actual.getUser().getCoreDetail());
    }

    public void testDisableSubject() throws Exception {

        Subject subject = buildSubjectWithUser();
        subjectService.create(subject);

        final Long id = subject.getId();
        Subject newSubject = subjectService.findById(id);
        assertTrue(newSubject.isCanLogIn());
        assertTrue(newSubject.isActive());
        assertEquals(UserType.SUBJECT.toString(), subject.getUser().getUserType());

        subjectService.disable(subject);
        final Subject modifiedSubject = subjectService.findById(id);
        assertFalse(modifiedSubject.isActive());
    }

    public void testDeleteUserFromSubject() throws Exception {

        Subject subject = buildSubjectWithUser();
        subjectService.create(subject);

        Subject newSubject = subjectService.findById(subject.getId());

        assertTrue(newSubject.isCanLogIn());
        assertTrue(newSubject.isActive());

        final User newUser = newSubject.getUser();
        assertEquals(UserType.SUBJECT.toString(), newUser.getUserType());

        userService.delete(newUser);
        newSubject.setUser(null);

        try {
            userService.findById(newUser.getId());
            fail("Incorrectly found deleted user");
        } catch (DomainObjectNotFoundException e) {
        }

        final Subject updatedSubject = subjectService.findById(newSubject.getId());
        final CoreDetail currentCoreDetail = updatedSubject.getCoreDetail();
        assertNotNull(currentCoreDetail);
        assertNull(updatedSubject.getUser());
        assertFalse(updatedSubject.isCanLogIn());
        assertEquals(subject.getCoreDetail(), currentCoreDetail);
    }

    public void testDeleteSubjectWithUser() throws Exception {

        Subject subject = buildSubjectWithUser();
        subjectService.create(subject);

        Subject newSubject = subjectService.findById(subject.getId());

        subjectService.delete(newSubject);

        try {
            subjectService.findById(newSubject.getId());
            fail("Found deleted subject");
        } catch (DomainObjectNotFoundException expected) {
        }

        // check user and core details still exists
        User user = null;
        try {
            user = (User) userService.findById(subject.getUser().getId());
        } catch (TalentStudioException e) {
            fail("User should have successfully been found");
        }
        assertNotNull(user);
        assertNotNull(user.getCoreDetail());
    }

    public void testFindByUserId() throws Exception {

        Subject newSubject = buildSubjectWithUser();
        subjectService.create(newSubject);

        // find subject by user id - check the same subject is returned
        User user = newSubject.getUser();
        Subject foundSubject = subjectService.findByUserId(user.getId());
        assertEquals(newSubject, foundSubject);

        // disable the subject and try again - should still work
        subjectService.disable(newSubject);
        assertFalse(newSubject.isActive());
        foundSubject = subjectService.findByUserId(user.getId());
        assertEquals(newSubject, foundSubject);
    }

    public void testSearch() throws Exception {
        final SubjectSearchQuery query = new SubjectSearchQuery();

        query.setOrgUnitId(DEFAULT_ORG_UNIT_ID);
        final List list = subjectService.search(ROOT_USER_ID, query);
        assertNotNull(list);
    }

    private Subject buildSubjectWithUser() {

        CoreDetail coreDetail = new CoreDetail("dr", "fred", "flintstone");
        coreDetail.setPrefGivenName("fred");
        Subject subject = new Subject(coreDetail);
        subject.setActive(true);
        subject.setDateOfBirth(new Date());

        final String username = "tester1234";
        final String password = "tester1234";
        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        User user = new User(loginInfo, coreDetail);
        Collection roles = hibernateRoleManagerDao.getActiveAccessRoles();
        user.addRole((Role) roles.iterator().next());
        subject.setUser(user);

        return subject;
    }

    public void testFindTeam() throws Exception {
        final Collection team = subjectService.findTeam(new Long(2507));
        assertNotNull(team);
    }

    private ISubjectService subjectService;
    private HibernateRoleManagerDao hibernateRoleManagerDao;
    private IUserService userService;
}
