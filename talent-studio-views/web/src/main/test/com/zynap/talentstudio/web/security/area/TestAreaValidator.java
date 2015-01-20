package com.zynap.talentstudio.web.security.area;

/**
 * User: amark
 * Date: 20-Mar-2005
 * Time: 11:38:32
 */

import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;

import junit.framework.TestCase;
import org.springframework.validation.BindException;

import java.util.HashSet;

public class TestAreaValidator extends TestCase {

    protected void setUp() throws Exception {

        areaValidator = new AreaValidator();
    }

    public void testSupports() throws Exception {
       assertTrue(areaValidator.supports(AreaWrapperBean.class));
    }

    public void testValidateCoreValues() throws Exception {

        AreaWrapperBean areaWrapperBean = new AreaWrapperBean(new Area(), new HashSet<AreaElement>());

        final BindException errors = new BindException(areaWrapperBean, "command");
        areaValidator.validateCoreValues(areaWrapperBean, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("label"));
    }

    AreaValidator areaValidator;
}