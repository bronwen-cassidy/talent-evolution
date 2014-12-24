package com.zynap.talentstudio.web.security.admin;

import com.meterware.httpunit.WebForm;

import com.zynap.talentstudio.web.ZynapWebTestCase;

/**
 * User: amark
 * Date: 12-May-2005
 * Time: 13:47:32
 */
public class TestUserAdministration extends ZynapWebTestCase {

    public void testAddUser() throws Exception {

        beginAt(LIST_USER_URL);

        addUser();

        deleteUser();

        // check now back on the list user page
        assertResponseURLEquals(LIST_USER_URL);

        // submit and check that the new user is not in the list
        final WebForm searchForm = getForm("user");
        searchForm.submit();

        assertTablePresent(HTML_TABLE_NAME);
        assertTextNotInTable(HTML_TABLE_NAME, "angus");
    }

    public void testAddUserCancel() throws Exception {

        beginAt(ADD_USER_URL);
        getCancelForm().submit();

        assertResponseURLEquals(LIST_USER_URL);
    }

    public void testEditUser() throws Exception {

        beginAt(LIST_USER_URL);

        addUser();

        // go to edit page
        final WebForm editPageForm = getForm("edit");
        editPageForm.submit();
        assertResponseURLEquals(EDIT_USER_URL);

        // set username to blank - should cause validation error
        WebForm editForm = getForm("_add");
        editForm.setParameter("loginInfo.username", "");
        assertResponseURLEquals(EDIT_USER_URL);

        // cancel and check url
        getCancelForm().submit();
        assertResponseURLEquals(VIEW_USER_URL);

        // goback to edit page, set email address and submit
        editPageForm.submit();
        editForm = getForm("_add");
        final String emailAddress = "amark@zynap.com";
        editForm.setParameter("contactEmail", emailAddress);
        editForm.submit();

        // check email address now on view page
        assertResponseURLEquals(VIEW_USER_URL);
        assertTextPresent(emailAddress);

        deleteUser();
    }

    public void testEditUserValidation() throws Exception {

        beginAt(LIST_USER_URL);

        addUser();

        // go to edit page
        final WebForm editPageForm = getForm("edit");
        editPageForm.submit();
        assertResponseURLEquals(EDIT_USER_URL);

        // set username to blank - should cause validation error
        WebForm editForm = getForm("_add");
        editForm.setParameter("loginInfo.username", "");
        assertResponseURLEquals(EDIT_USER_URL);

        // set username to something and save changes
        final String newUserName = "angus2";
        editForm.setParameter("loginInfo.username", newUserName);
        editForm.submit();

        checkForErrors();

        // check username now on view page
        assertResponseURLEquals(VIEW_USER_URL);
        assertTextPresent(newUserName);

        deleteUser();
    }

    public void testEditUserCancel() throws Exception {

        beginAt(LIST_USER_URL);

        addUser();

        // go to edit page
        final WebForm editPageForm = getForm("edit");
        editPageForm.submit();
        assertResponseURLEquals(EDIT_USER_URL);

        getCancelForm().submit();
        assertResponseURLEquals(VIEW_USER_URL);

        deleteUser();
    }

    public void testListUser() throws Exception {

        beginAt(LIST_USER_URL);
        assertAppTitleEquals("Users");

        // submit and check that administrator is listed
        final WebForm searchForm = getForm("user");
        searchForm.submit();

        assertTablePresent(HTML_TABLE_NAME);
        assertTextInTable(HTML_TABLE_NAME, "administrator");
    }

    public void testResetPassword() throws Exception {

        beginAt(LIST_USER_URL);

        addUser();

        // go to change password page
        final WebForm changePasswordForm = getForm("_reset");
        changePasswordForm.submit();
        assertResponseURLEquals(RESET_USER_PWD_URL);

        // check which fields are on the page
        assertFormElementEmpty("newPassword");
        assertFormElementEmpty("newPasswordAgain");
        assertFormElementNotPresent("oldPassword");

        // cancel and check url
        getCancelForm().submit();
        assertResponseURLEquals(VIEW_USER_URL);

        deleteUser();
    }

    private void addUser() throws Exception {

        // click on add button
        getForm("_add").submit();
        assertResponseURLEquals(ADD_USER_URL);
        assertAppTitleEquals("Add User");

        // supply passwords, username and secondname and check for validation error on first name
        final WebForm addForm = getForm("_add");
        addForm.setParameter("loginInfo.username", "angus");
        addForm.setParameter("loginInfo.password", "angus");
        addForm.setParameter("loginInfo.repeatedPassword", "angus");

        addForm.setParameter("secondName", "mark");

        addForm.submit();
        assertTextPresent("&#039;First Name&#039; is a required field.");

        // add first name and submit and check that now on view user page
        addForm.setParameter("firstName", "Angus");
        addForm.submit();
        assertResponseURLEquals(VIEW_USER_URL);
    }

    private void deleteUser() throws Exception {
        final WebForm deleteForm = getForm("_deleteuser");
        deleteForm.submit();
        assertAppTitleEquals("Delete User");

        // confirm the deletion
        final WebForm confirmDeleteForm = getForm("_confirm");
        confirmDeleteForm.submit();
    }

    private static final String HTML_TABLE_NAME = "userList";

    private static final String LIST_USER_URL = "/admin/listuser.htm";
    private static final String ADD_USER_URL = "/admin/adduser.htm";
    private static final String EDIT_USER_URL = "/admin/edituser.htm";
    private static final String VIEW_USER_URL = "/admin/viewuser.htm";
    private static final String RESET_USER_PWD_URL = "/admin/resetpassword.htm";
}
