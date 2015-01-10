package com.zynap.talentstudio.integration.common;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;

import java.util.HashSet;
import java.util.Set;

/**
 * User: amark
 * Date: 19-Oct-2005
 * Time: 13:55:10
 */
public final class IntegrationConstants {

    public static final String SYSTEM_ERROR_CODE = "100";
    public static final String NO_DATA_CODE = "200";
    public static final String INVALID_DATA_CODE = "300";
    public static final String OBJECT_NOT_FOUND_CODE = "400";

    public static final String ERRORS_NODE = "errors";
    public static final String ERROR_NODE = "error";

    public static final String COMMANDS_NODE = "commands";
    public static final String COMMAND_NODE = "command";

    public static final String RESULTS_NODE = "results";
    public static final String RESULT_NODE = "result";

    public static final String TARGET_NODE = IPopulationEngine.TARGET_ATTR;

    public static final String UPDATED_ID_ATTRIBUTE = "updatedId";
    public static final String GENERATED_ATTRIBUTE = "generated";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String CODE_ATTRIBUTE = "code";
    public static final String ACTION_ATTRIBUTE = "action";
    public static final String ID_ATTRIBUTE = "id";
    public static final String REF_ATTRIBUTE = "ref";

    public static final String SUBJECT_ASSOCIATION_FIELD = IPopulationEngine.SUBJECT_ASSOCIATION_ATTR;
    public static final String SOURCE_ASSOCIATION_FIELD = IPopulationEngine.SOURCE_ASSOCIATIONS_ATTR;

    public static final Set ASSOCIATIONS_ATTRIBUTE = new HashSet();
    static {
        ASSOCIATIONS_ATTRIBUTE.add(SOURCE_ASSOCIATION_FIELD);
        ASSOCIATIONS_ATTRIBUTE.add(SUBJECT_ASSOCIATION_FIELD);
    }
}
