package com.zynap.talentstudio.web.security.domain;

import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import org.springframework.validation.BindException;

/**
 * User: amark
 * Date: 16-Mar-2005
 * Time: 16:38:23
 */
public abstract class AbstractDomainControllerTest extends ZynapMockControllerTest {

    public void setUp() throws Exception {
        super.setUp();

        securityManager = (ISecurityManager) applicationContext.getBean("securityManager");
        userService = (IUserService) applicationContext.getBean("userService");
    }

    protected BindException getErrors(BaseDomainController domainController, SecurityDomainWrapperBean securityDomainWrapperBean) {
        return new BindException(securityDomainWrapperBean, domainController.getCommandName());
    }

    protected ISecurityManager securityManager;
    protected IUserService userService;
}
