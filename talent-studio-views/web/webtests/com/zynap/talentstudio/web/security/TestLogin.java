package com.zynap.talentstudio.web.security;

import com.zynap.talentstudio.web.ZynapWebTestCase;

/**
 * User: amark
 * Date: 03-Jun-2005
 * Time: 13:48:23
 */

public class TestLogin extends ZynapWebTestCase {

    /**
     * Check that a logged in user cannot access the login page directly.
     * @throws Exception
     */
    public void testAccess() throws Exception {

        beginAt(getLoginPage());

        // should be redirected to home page
        assertAppTitleEquals("Home");
    }
}
