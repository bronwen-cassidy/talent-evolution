/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.objectives;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Mar-2007 16:03:30
 */
public class ObjectiveUtils {

    public static ObjectiveSet findCurrentObjectiveSet(Collection objectiveSets) {
        ObjectiveSet result;
        if (objectiveSets == null || objectiveSets.isEmpty()) result = null;
        result = (ObjectiveSet) CollectionUtils.find(objectiveSets, new Predicate() {
            public boolean evaluate(Object object) {
                ObjectiveSet objectiveSet = (ObjectiveSet) object;
                return (!objectiveSet.isArchived());
            }
        });
        return result;
    }
}
