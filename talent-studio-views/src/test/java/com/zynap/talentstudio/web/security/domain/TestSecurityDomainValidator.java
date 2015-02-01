package com.zynap.talentstudio.web.security.domain;

/**
 * User: amark
 * Date: 19-Mar-2005
 * Time: 12:34:15
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.security.SecurityDomain;

import org.springframework.validation.BindException;

public class TestSecurityDomainValidator extends TestCase {

    protected void setUp() throws Exception {

        securityDomainValidator = new SecurityDomainValidator();
    }

    public void testValidateArea() throws Exception {

        SecurityDomainWrapperBean securityDomainWrapperBean = new SecurityDomainWrapperBean(new SecurityDomain());

        final BindException errors = new BindException(securityDomainWrapperBean, "command");
        securityDomainValidator.validateArea(securityDomainWrapperBean, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("areaId"));
    }

    public void testValidateCoreValues() throws Exception {

        SecurityDomainWrapperBean securityDomainWrapperBean = new SecurityDomainWrapperBean(new SecurityDomain());

        final BindException errors = new BindException(securityDomainWrapperBean, "command");
        securityDomainValidator.validateCoreValues(securityDomainWrapperBean, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("label"));
    }

    public void testSupports() throws Exception {
        assertTrue(securityDomainValidator.supports(SecurityDomainWrapperBean.class));
    }

    SecurityDomainValidator securityDomainValidator;
}