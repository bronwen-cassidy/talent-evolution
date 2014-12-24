/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Apr-2008 09:23:34
 */
public class MaxNumCellsExceededException extends TalentStudioException {

    public MaxNumCellsExceededException(String message, String key) {
        super(message, key);
    }
}
