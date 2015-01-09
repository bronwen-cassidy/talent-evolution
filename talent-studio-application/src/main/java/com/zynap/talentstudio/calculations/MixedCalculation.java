/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.CalculationResult;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 11-May-2009 11:37:52
 */
public class MixedCalculation extends Calculation {

    public CalculationResult execute(Node node) {
        return null;
    }

    public String getType() {
        return MIXED_TYPE;
    }

    public static final String MIXED_TYPE = "MIXED";
}
