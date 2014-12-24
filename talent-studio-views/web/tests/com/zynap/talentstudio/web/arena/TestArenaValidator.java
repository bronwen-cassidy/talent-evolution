package com.zynap.talentstudio.web.arena;

/**
 * User: amark
 * Date: 09-Jun-2005
 * Time: 18:21:59
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.ArenaDisplayConfigItem;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.web.common.ControllerConstants;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

public class TestArenaValidator extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {

        super.setUp();

        arenaValidator = new ArenaValidator();
        displayConfigService = (IDisplayConfigService) getBean("displayConfigService");
    }

    public void testValidate() throws Exception {

        final List all = displayConfigService.findAll();

        final Arena newArena = new Arena();
        newArena.setHideable(true);
        newArena.setLabel("label");
        newArena.setSessionTimeout(10);

        // add an ArenaDisplayConfigItem for each available DisplayConfig
        for (int i = 0; i < all.size(); i++) {
            final DisplayConfig displayConfig = (DisplayConfig) all.get(i);
            final DisplayConfigItem displayConfigItem = displayConfig.getDisplayConfigItems().get(0);
            newArena.addArenaDisplayConfigItem(new ArenaDisplayConfigItem(null, newArena, displayConfigItem));
        }

        ArenaWrapperBean arenaWrapperBean = new ArenaWrapperBean(newArena, all);

        Errors errors = new BindException(arenaWrapperBean, ControllerConstants.COMMAND_NAME);
        arenaValidator.validate(arenaWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testInvalidSessionTimeout() throws Exception {

        // set value to zero
        final Arena newArena = new Arena();
        newArena.setSessionTimeout(0);

        ArenaWrapperBean arenaWrapperBean = new ArenaWrapperBean(newArena, displayConfigService.findAll());

        Errors errors = new BindException(arenaWrapperBean, ControllerConstants.COMMAND_NAME);
        arenaValidator.validate(arenaWrapperBean, errors);
        final FieldError fieldError = errors.getFieldError("sessionTimeout");
        assertNotNull(fieldError);
        assertEquals("field.is.numeric", fieldError.getCode());
    }

    public void testBlankLabel() throws Exception {

        // set blank label
        final Arena newArena = new Arena();
        newArena.setLabel("");
        ArenaWrapperBean arenaWrapperBean = new ArenaWrapperBean(newArena, displayConfigService.findAll());

        Errors errors = new BindException(arenaWrapperBean, ControllerConstants.COMMAND_NAME);
        arenaValidator.validate(arenaWrapperBean, errors);
        final FieldError fieldError = errors.getFieldError("label");
        assertNotNull(fieldError);
        assertEquals("error.label.required", fieldError.getCode());
    }

    public void testValidateDeactivation() throws Exception {

        // Non-hideable arena cannot be inactive
        final Arena newArena = new Arena();
        newArena.setHideable(false);
        newArena.setActive(false);
        ArenaWrapperBean arenaWrapperBean = new ArenaWrapperBean(newArena, displayConfigService.findAll());

        Errors errors = new BindException(arenaWrapperBean, ControllerConstants.COMMAND_NAME);
        arenaValidator.validate(arenaWrapperBean, errors);
        final FieldError fieldError = errors.getFieldError("active");
        assertNotNull(fieldError);
        assertEquals("error.arena.required", fieldError.getCode());
    }

    public void testArenaDisplayConfigItems() throws Exception {

        // Hideable arena must have the correct number of arena display config items - one per display config
        final Arena newArena = new Arena();
        newArena.setHideable(true);

        newArena.addArenaDisplayConfigItem(new ArenaDisplayConfigItem());

        ArenaWrapperBean arenaWrapperBean = new ArenaWrapperBean(newArena, displayConfigService.findAll());
        Errors errors = new BindException(arenaWrapperBean, ControllerConstants.COMMAND_NAME);
        arenaValidator.validate(arenaWrapperBean, errors);
        final ObjectError fieldError = errors.getGlobalError();
        assertNotNull(fieldError);
        assertEquals("error.arena.displayconfigitems.required", fieldError.getCode());
    }

    public void testSupports() throws Exception {
        assertTrue(arenaValidator.supports(ArenaWrapperBean.class));
    }

    ArenaValidator arenaValidator;
    private IDisplayConfigService displayConfigService;
}