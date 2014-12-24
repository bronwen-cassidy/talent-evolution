package com.zynap.talentstudio.web.arenas;

import com.meterware.httpunit.WebForm;

import com.zynap.talentstudio.web.ZynapWebTestCase;

import com.zynap.talentstudio.web.common.html.IdTag;

/**
 * User: amark
 * Date: 30-Nov-2005
 * Time: 13:35:13
 */
public class TestArenaConfiguration extends ZynapWebTestCase {

    public void testListArenas() throws Exception {
        beginAt(LIST_ARENAS_URL);

        assertAppTitleEquals(LIST_ARENAS_PAGE_TITLE);
    }

    public void testEditHideableArena() throws Exception {

        final String arenaName = "Performance Management";
        final String arenaLinkId = IdTag.stringToHtmlId("edit_" + arenaName);

        beginAt(LIST_ARENAS_URL);

        clickLink(arenaLinkId);
        assertAppTitleEquals(EDIT_ARENA_PAGE_TITLE);

        // check that label field in form has correct value
        WebForm form = getForm(ADD_FORM_NAME);
        assertFormElementEquals(LABEL_FORM_ELEMENT_NAME, arenaName);

        // check that active check box is disabled
        assertFormElementPresent(ACTIVE_FORM_ELEMENT_NAME);
        assertFalse(form.isDisabledParameter(ACTIVE_FORM_ELEMENT_NAME));

        assertSaveButtonPresent();
        assertCancelButtonPresent();

        // cancel and check page title
        clickButton(CANCEL_BUTTON_ID);
        assertAppTitleEquals(LIST_ARENAS_PAGE_TITLE);
        checkForErrors();

        // go back to page and make inactive
        clickLink(arenaLinkId);
        form = getForm(ADD_FORM_NAME);
        form.toggleCheckbox(ACTIVE_FORM_ELEMENT_NAME);
        clickButton(SAVE_BUTTON_ID);

        // should now be on list page again
        assertResponseURLEquals(LIST_ARENAS_URL);
        assertAppTitleEquals(LIST_ARENAS_PAGE_TITLE);

        // check arena is gone from tabs
        assertTextNotInTable(ARENA_TABS_TABLE_ID, arenaName);

        // go back to page and reactivate
        clickLink(arenaLinkId);
        form = getForm(ADD_FORM_NAME);
        form.toggleCheckbox(ACTIVE_FORM_ELEMENT_NAME);
        clickButton(SAVE_BUTTON_ID);

        // check arena is back in tabs
        assertTextInTable(ARENA_TABS_TABLE_ID, arenaName);
    }

    public void testEditRequiredArena() throws Exception {

        final String arenaName = "Administration";
        final String arenaLinkId = IdTag.stringToHtmlId("edit_" + arenaName);

        beginAt(LIST_ARENAS_URL);

        clickLink(arenaLinkId);
        assertAppTitleEquals(EDIT_ARENA_PAGE_TITLE);

        // check that label field in form has correct value
        final WebForm form = getForm(ADD_FORM_NAME);
        assertFormElementEquals(LABEL_FORM_ELEMENT_NAME, arenaName);

        // check that active check box is disabled
        assertFormElementPresent(ACTIVE_FORM_ELEMENT_NAME);
        assertTrue(form.isDisabledParameter(ACTIVE_FORM_ELEMENT_NAME));

        assertSaveButtonPresent();
        assertCancelButtonPresent();

        // cancel and check page title
        clickButton(CANCEL_BUTTON_ID);
        assertAppTitleEquals(LIST_ARENAS_PAGE_TITLE);
        checkForErrors();

        // go back to page and change name
        clickLink(arenaLinkId);
        final String newLabel = "New Arena";
        final String newArenaLinkId = IdTag.stringToHtmlId("edit_" + newLabel);

        setWorkingForm(ADD_FORM_NAME);
        setFormElement(LABEL_FORM_ELEMENT_NAME, newLabel);
        clickButton(SAVE_BUTTON_ID);

        // should now be on list page again
        assertResponseURLEquals(LIST_ARENAS_URL);
        assertAppTitleEquals(LIST_ARENAS_PAGE_TITLE);

        // check menus have been reloaded and that tab name has changed
        assertTextNotInTable(ARENA_TABS_TABLE_ID, arenaName);
        assertTextInTable(ARENA_TABS_TABLE_ID, newLabel);

        clickLink(newArenaLinkId);
        assertAppTitleEquals(EDIT_ARENA_PAGE_TITLE);

        // change name back
        setWorkingForm(ADD_FORM_NAME);
        setFormElement(LABEL_FORM_ELEMENT_NAME, arenaName);
        clickButton(SAVE_BUTTON_ID);
    }

    private static final String LIST_ARENAS_URL = "admin/listarenas.htm";

    private static final String LIST_ARENAS_PAGE_TITLE = "Arenas";
    private static final String EDIT_ARENA_PAGE_TITLE = "Edit Arena";
    private static final String ARENA_TABS_TABLE_ID = "arenatabs";
}
