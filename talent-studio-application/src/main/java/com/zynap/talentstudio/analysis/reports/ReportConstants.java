/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Feb-2006 14:32:45
 */
public interface ReportConstants {

    String METRIC_COL_ID = "metColIdx";
    String METRIC_COL_HEADER_FORM = "colChartFrm";
    String COL_ID_PARAM = "colId";
    String METRIC_BAR_SELECT_FORM = "barSelectFrm";
    String ORIGINAL_POPULATION_ID = "originalPopulationId";
    String VERTICAL_ATTR = "verticalAttr";
    String VERTICAL_ATTR_VALUE = "verticalAttrValue";
    String VERTICAL_ATTR_LABEL = "verticalAttrCriteria";
    
    String CHART_ATTR_VALUE = "chartValue";
    String CHART_ATTR_IDS = "chartAttrIds";
    String CHART_VALUE_IDS = "chartValueIds";
    String CHART_DATA_ID ="dataId";
    String CHART_REPORT_PARAM ="isChartDrillDown";

    String EXPORTED_REPORT_ID = "report_id";
    String SORT_ORDER = "sort_order";
    String ORDER_BY = "order_by";

    /**
     * Constants for exports.
     */
    String COMMA = ",";
    String SEMI_COLON = ";";
    String TAB = "tab";
    String COLON = ":";

    /**
     * Values used for common groupings, blank values, etc.
     */
    String NA = "N/A";
    String TOTAL = "Total";
    String BLANK_VALUE = "-";
    String NO_VALUE = "#";
    String NaN = "~";

    /**
     * Constants for parameters used for drill down links.
     */
    String HORIZONTAL_ATTR = "horizontalAttr";
    String HORIZONTAL_ATTR_VALUE = "horizontalAttrValue";
    String HORIZONTAL_ATTR_LABEL = "horizontalAttrCriteria";

    /**
     * Prefix used to indicate id attribute.
     */
    String ID_ATTR_PREFIX = "id_";

    /**
     * Prefix used to indicate metric attribute.
     */
    String METRIC_ATTR_PREFIX = "met_";

    /**
     * Names of parameters passed into exporters.
     */
    String REPORT_PARAM = "report";
    String HAS_ACCESS_PARAM = "hasAccess";
    String _DS_FACTORY_PARAM = "_DS_FACTORY_PARAM";
    String _USER_PARAM = "user";

    /**
     * Name of parameter used when filling report to specify number of decimal places to format results to.
     */
    String DECIMAL_PLACES_PARAM = "_decimalplaces";

    /* the maximum number of results per page for the tabular reports */
    int RESULTS_PER_PAGE_NUM = 25;
    final String EMPTY_RESULTS = "There are no results for this report.";
    final String LOGO_FILE="/images/logo.gif";
    final String COL_SEPARATOR = "^";
    final String ROW_SEPARATOR = "~";
    final String WORKFLOW_NAME = "WORFLOW_LABEL";
    final String WORKFLOW_ID_NAME = "workflowId";
}
