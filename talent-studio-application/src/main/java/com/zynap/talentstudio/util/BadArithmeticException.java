/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.util;

/**
 * Class or Interface description.
 *
 * @author acalderwood
 * @version 0.1
 * @since 08-Dec-2006 09:29:24
 */

public class BadArithmeticException extends Exception {

    public BadArithmeticException(String msg) {
        super("The arithmetic operation could not be performed: " + msg);        
    }
}
