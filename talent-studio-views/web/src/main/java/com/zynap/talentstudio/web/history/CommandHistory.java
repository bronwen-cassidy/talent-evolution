package com.zynap.talentstudio.web.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Object that holds command objects in a stack for history.
 *
 * @author Angus Mark
 */
public final class CommandHistory implements Serializable {

    private List<Object> savedCommands = new ArrayList<Object>();

    public CommandHistory() {
    }

    Collection getSavedCommands() {
        return savedCommands;
    }

    /**
     * Get the last URL.
     * <br/> Does not remove the object from the saved list.
     *
     * @return the last URL, or null.
     */
    SavedURL getLastURL() {
        final SavedURL savedURL = getLastSavedURL();
        return savedURL;
    }

    /**
     * Get the last command object.
     * <br/> Does not remove the object from the saved list.
     *
     * @return the last command object, or null.
     */
    Object getLastCommand() {
        final SavedURL savedURL = getLastSavedURL();
        return savedURL != null ? savedURL.getCommand() : null;
    }

    /**
     * Get and remove the last command object.
     * <br/> Always removes the object.
     *
     * @return The command or null
     */
    Object recoverCommand() {
        return (!savedCommands.isEmpty()) ? ((SavedURL) savedCommands.remove(0)).getCommand() : null;
    }

    /**
     * Check if the object has saved commands.
     *
     * @return true if the history has commands
     */
    boolean hasSavedCommands() {
        return !savedCommands.isEmpty();
    }

    /**
     * Removes all command objects.
     */
    void reset() {
        savedCommands.clear();
    }

    /**
     * Save the command object.
     * <br/> Adds the command object to the 1st position in the saved commands.
     *
     * @param uri
     * @param command
     */
    public void saveCommand(String uri, Object command) {
        final SavedURL element = new SavedURL(uri, command);
//        if(savedCommands.contains(element)) {
//            savedCommands.remove(element);
//        }
        savedCommands.add(0, element);
    }

    /**
     * Get first element from list of saved commands.
     * <br/> Does not remove the element.
     *
     * @return SavedURL or null
     */
    SavedURL getLastSavedURL() {
        return getSavedURL(0);
    }

    /**
     * Get first element from list of saved commands.
     * <br/> Does not remove the element.
     *
     * @return SavedURL or null
     */
    SavedURL getSavedURL(int index) {
        return (!savedCommands.isEmpty() && index < savedCommands.size() ? (SavedURL) savedCommands.get(index) : null);
    }
}
