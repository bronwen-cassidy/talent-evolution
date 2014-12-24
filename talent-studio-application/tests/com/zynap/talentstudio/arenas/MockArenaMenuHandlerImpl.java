package com.zynap.talentstudio.arenas;

import com.zynap.domain.UserPrincipal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:19:46
 * To change this template use File | Settings | File Templates.
 */
public class MockArenaMenuHandlerImpl implements IArenaMenuHandler {

    public Collection getSecuredArenas(UserPrincipal userPrincipal) {
        return new HashSet();
    }

    public void reload() {
    }

    public Collection getActiveArenas() {
        return new ArrayList();
    }

    public Collection getInactiveArenas() {
        return new ArrayList();
    }
}
