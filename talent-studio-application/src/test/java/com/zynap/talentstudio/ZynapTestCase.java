package com.zynap.talentstudio;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.arenas.IArenaMenuHandler;
import com.zynap.talentstudio.arenas.MockArenaMenuHandlerImpl;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.permits.IPermit;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

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

    public Errors getErrors(DataBinder binder) {
        return binder.getBindingResult();
    }

    protected final Log logger = LogFactory.getLog(getClass());
}
