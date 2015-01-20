package com.zynap.talentstudio.web.common;

/**
 * Interface that holds constants for names of attributes that are shared between Controllers.
 *
 * User: amark
 * Date: 08-Feb-2005
 * Time: 10:05:24
 */
public interface ControllerConstants {


    final String APPRAISAL_REPORTS = "appraisalReports";
    final String REPORTS = "reports";
    final String NUM_APPRAISAL_REPORTS = "numAppraisalReports";
    final String ARENAS = "arenas";
    final String ORG_UNIT_TREE = "outree";
    final String TITLE = "pagetitle";
    final String TITLES = "titles";
    final String ARTEFACT = "artefact";
    final String ARTEFACT_TYPE = "artefactType";

    final String USERS = "users";

    final String USER_ID = "userId";

    final String USER_NAME = "userName";

    final String LABEL = "label";

    final String ERROR = "error";

    final String FORCE_PWD_CHANGE = "forcePwd";

    final String LOOKUPS = "lookups";

    final String DYNAMIC_ATTRIBUTE = "da";

    final String DYNAMIC_ATTRIBUTES = "das";

    final String POSITIONS = "positions";

    final String PORTFOLIO_ITEM = "item";

    final String POPULATIONS = "populations";

    final String PREV_URL = "prevUrl";

    final String CANCEL_URL = "cancelUrl";

    /**
     * Constants for common views.
     */
    final String CANCEL_VIEW = "cancelView";

    final String CONFIRM_VIEW = "confirmView";

    final String EDIT_VIEW = "editView";

    final String DELETE_VIEW = "deleteView";

    /**
     * Name of command in controllers.
     */
    final String COMMAND_NAME = "command";

    /**
     * Name of command object stored in session by Controllers.
     */
    final String COMMAND_OBJECT_NAME_SUFFIX = ".FORM." + COMMAND_NAME;

    /**
     * Name of model in controllers.
     */
    final String MODEL_NAME = "model";

    /**
     * Name of request parameter that indicates that Node has just been added.
     */
    final String NEW_NODE = "newNode";

    /**
     * Name of request parameter that indicates a Node has just been deleted - the value is expected to be the id of the deleted Node.
     */
    final String DELETED_NODE_ID = "deletedNodeId";

    /**
     * Name of request parameter that indicates that we are paging through search results.
     */
    final String PAGE_CHANGE = "pageChange";

    /**
     * The key passed to tell the displayConfigContentWrapper which tab to display by default
     */
    final String DISPLAY_CONFIG_KEY = "displayConfigKey";

    /**
     * Prefix used by display tag for its parameters - used by this controller to find the page number.
     */
    String DISPLAY_TAG_PREFIX = "d-";

    /**
     * Parameter that indicates a form submission - use with wizards to force controller to acknowledge the form submission.
     */
    String FORM_SUBMISSION = "_formSubmission";

    /**
     * Parameter for page numbers for wizards - used to override getTargetPage(...).
     */
    String PAGE_NUM = "_pageNum";

    /**
     * Parameter that specifies index of image extended attribute whose value is being cleared.
     */
    String DELETE_IMAGE_INDEX = "deleteImageIndex";

    /**
     * Prefix for spring wizard target parameters.
     */
    String TARGET_PARAM_PREFIX = "_target";
    final String SECURITY_DOMAINS = "securityDomains";

    final String QUESTIONNAIRES = "questionnaires";

    static final String USER_SECURITY_DOMAINS = "userSecurityDomains";
    String ACCEPT_POLICY_KEY = "acceptedPolicy";
    String SUPER_USER = "superUser";
}
