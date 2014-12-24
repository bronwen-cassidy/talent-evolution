/* Copyright: Copyright (c) 2004
 * Company:
 */
package com.zynap.talentstudio.web.history;

import com.zynap.talentstudio.ZynapTestCase;

import java.util.Collection;


/**
 * .
 *
 * @author Angus Mark
 */
public class TestCommandHistory extends ZynapTestCase {

    private CommandHistory commandHistory;

    protected void setUp() throws Exception {
        super.setUp();

        commandHistory = new CommandHistory();
    }

    public void testReset() throws Exception {

        final String previousUri1 = "viewuser.htm";
        final String previousUri2 = "viewuser.htm";

        final Object command1 = new Object();
        final Object command2 = new Object();

		commandHistory.saveCommand(previousUri1, command1);
		commandHistory.saveCommand(previousUri2, command2);

        assertTrue(commandHistory.hasSavedCommands());

        // check that there are 2 commands in the stack
        assertCorrectNumberOfCommands(2);

        // get command - should pop last one off stack
        commandHistory.reset();

        // check that there are no command objects left
        assertFalse(commandHistory.hasSavedCommands());
    }

    public void testRecoverCommand() throws Exception {

        final String uri = "viewuser.htm";

        final Object command1 = new Object();
        final Object command2 = new Object();

        commandHistory.saveCommand(uri, command1);
        commandHistory.saveCommand(uri, command2);

        // check that there are two commands in the saved stack
        assertCorrectNumberOfCommands(2);

        // get command - should pop last one off stack
        final Object command = commandHistory.recoverCommand();
        assertEquals(command2, command);

        // should now only be one command in the stack
        assertCorrectNumberOfCommands(1);
    }

    public void testSaveCommand() throws Exception {

        final String uri = "viewuser.htm";

        // save two commands
        final Object command1 = new Object();
        final Object command2 = new Object();
        commandHistory.saveCommand(uri, command1);
        commandHistory.saveCommand(uri, command2);

        assertCorrectNumberOfCommands(2);
    }

    private void assertCorrectNumberOfCommands(int expected) {
        Collection savedCommands = commandHistory.getSavedCommands();
        assertEquals(expected, savedCommands.size());
    }
}
