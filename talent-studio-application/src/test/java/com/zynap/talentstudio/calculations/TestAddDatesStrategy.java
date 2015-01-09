/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 24-Jul-2007 16:20:12
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.calculations.AddDatesStrategy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestAddDatesStrategy extends TestCase {

    public void testExecute() throws Exception {
        AddDatesStrategy addDatesStrategy = new AddDatesStrategy();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = dateFormat.parse("24-07-2007");
        Date date2 = dateFormat.parse("24-07-2007");
        Object actual = addDatesStrategy.execute(date1, date2, 0);
        assertTrue(actual instanceof Date);
        String answer = DateFormat.getDateInstance(2, Locale.ENGLISH).format(actual);
        assertEquals("Mar 17, 4015", answer);
    }
}