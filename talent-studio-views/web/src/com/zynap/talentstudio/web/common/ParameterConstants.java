package com.zynap.talentstudio.web.common;

/**
 * Interface that holds constants for names of request parameters.
 * that are shared between presentation tier components (Controllers, Tag libraries, etc.)
 *
 * User: amark
 * Date: 08-Feb-2005
 * Time: 10:07:22
 */
public interface ParameterConstants {

    String ARENA_ID = "a";

    String CONFIRM_PARAMETER = "confirm";

    String CANCEL_PARAMETER = "_cancel";

    String NODE_ID_PARAM = "command.node.id";

    // todo remove SUBJECT_ID_PARAM at some point - identical to NODE_ID_PARAM
    String SUBJECT_ID_PARAM = NODE_ID_PARAM;

    String ORG_UNIT_ID_PARAM = "id";

    String LOOKUP_ID = "pk";

    String LOOKUP_TYPE_ID = "typeId";

    String ITEM_ID = "i_id";

    String ARTEFACT_ID = "node_id";

    String MENU_PARAM = "menu_p";

    String ATTR_ID = "attrId";

    String ARTEFACT_TYPE = "artefact";

    String USER_ID = "node_id";

    String ROLE_ID = "role_id";

    String ACTIVE = "active";

    String TAB = "tab";

    String CONTROLLER_NAME = "controllerName";

    String TITLE = "title";

    String POPULATION_ID = "id";

    String REPORT_ID = "id";

    String DOMAIN_ID = "domainId";

    String AREA_ID = "areaId";

    String MANDATORY = "mandatory";

    String SEARCHABLE = "searchable";

    String DISABLE_COMMAND_DELETION = "_disableDelete";

    String UPDATE_COMMAND = "updateCommand";

    String LEAVE_COMMAND = "leaveCommand";

    String SEARCH_STARTED_PARAM = "search_initiated";

    String NAVIGATOR_COMMAND = "navigator";

    String NAVIGATOR_OU_ID = NAVIGATOR_COMMAND + ".organisationUnitId";

    String NAVIGATOR_NOT_SUBMIT = NAVIGATOR_COMMAND + ".notSubmit";

    String NAVIGATOR_OU_LABEL = NAVIGATOR_COMMAND + ".organisationUnitLabel";

    String MENUS_ATTR_KEY = "menus";

    String SAVE_COMMAND = "_saveCommand_";

    String ALLOW_DELETE = "allowDelete";

    String PREFIX_COMMAND_PARAMETER = "_parameter_save_command_.";

    String ARENA_ID_PARAM = "arena_p";

    String METRIC_ID = "metric_id";

    String DISPLAY_ITEM_ID_PARAM = "dc_item";

    String QUESTIONNAIRE_DEF_ID = "qd_id";

    String QUESTIONNAIRE_ID = "q_id";

    String ACTIVE_TAB = "activeTab";

    String CONFIG_ID_PARAM = "config_id_";

    String HAS_RESULTS = "hasResults";

    String FORM_SUBMISSION_PARAM = "_formSubmission";

    String PAGE_NUMBER_PARAM = "_pageNum";

    final String ARENAS_MENU_PARAM = "arenaz";    
    final String CURRENT_ARENA_ID_PARAM = "currentArenaId";
    final String USER_PRINCIPAL_PARAM = "userPrincipal";
    final String ARENA = "arena";
    final String GROUP_ID = "groupId";
    final String ACTIVE_SEARCH_TAB = "activeSearchTab";
    final String QUESTIONNAIRE_WF_ID = "workflowId";
    String DASHBOARD_ID  = "dashboardId";
    String POPULATION_PERSON_ID = "populationPersonId";
    String MOCK_USER_ID_PARAM = "mockUserId";
    String PERSONAL_DRILL_DOWN = "prsnlDD";
}
