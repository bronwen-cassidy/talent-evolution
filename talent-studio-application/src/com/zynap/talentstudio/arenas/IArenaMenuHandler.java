package com.zynap.talentstudio.arenas;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.domain.UserPrincipal;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:18:45
 */
public interface IArenaMenuHandler {

    Collection<Arena> getSecuredArenas(UserPrincipal userPrincipal);

    void reload() throws TalentStudioException;

    Collection<Arena> getActiveArenas();

    Collection<Arena> getInactiveArenas();
}
