package com.zynap.talentstudio;

import junit.framework.TestCase;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.arenas.IArenaMenuHandler;
import com.zynap.talentstudio.arenas.MockArenaMenuHandlerImpl;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.audit.SessionLog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.ArrayList;

/**
 * User: amark
 * Date: 15-Aug-2005
 * Time: 13:38:36
 */
public abstract class ZynapTestCase extends TestCase implements UnitTestConstants {

    protected final UserPrincipal getUserPrincipal(Subject subject) {
        return new UserPrincipal(subject.getUser(), null, new SessionLog());
    }

    protected final UserPrincipal getSysUserPrincipal() {
        return new UserPrincipal(ROOT_USER, new ArrayList<IPermit>(), new SessionLog());
    }

    protected final UserPrincipal getAdminUserPrincipal() {
        return new UserPrincipal(new User(ADMINISTRATOR_USER_ID, "administrator", "admin", "strator"), new ArrayList<IPermit>(), new SessionLog());
    }

    protected final IArenaMenuHandler getArenaMenuHandler() {
        return new MockArenaMenuHandlerImpl();
    }

    protected final void assertTextPresent(final String string, String text) {
        assertNotNull(string);
        assertTrue(string.indexOf(text) >= 0);
    }

    protected final void assertTextNotPresent(String string, final String label) {
        assertNotNull(string);
        assertTrue(string.indexOf(label) < 0);
    }

    protected final void assertEquals(double d1, double d2) {
        assertEquals(d1, d2, 0);        
    }

    public Errors getErrors(DataBinder binder) {
        return binder.getBindingResult();
    }

    protected final Log logger = LogFactory.getLog(getClass());
}
