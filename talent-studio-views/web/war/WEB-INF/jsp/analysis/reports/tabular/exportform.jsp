<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ page import="com.zynap.talentstudio.analysis.reports.ReportConstants"%>
<zynap:form method="get" name="_csvexport" action="runexporttabularreport.htm">

    <!-- back -->
    <input id="active_tab1" type="hidden" name="<%= ParameterConstants.PREFIX_COMMAND_PARAMETER + ParameterConstants.ACTIVE_TAB %>" value="runoptions"/>
    <input id="save_1" type="hidden" name="<%= ParameterConstants.DISABLE_COMMAND_DELETION %>" value="<%=ParameterConstants.SAVE_COMMAND%>"/>

    <!-- export -->
    <input id="rep_id1" type="hidden" name="<%= ReportConstants.EXPORTED_REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
    <input id="pop_id1" type="hidden" name="<%= ParameterConstants.POPULATION_ID %>" value="<c:out value="${command.populationIdsString}"/>"/>
    <input id="so1" type="hidden" name="<%= ReportConstants.SORT_ORDER %>" value="<c:out value="${command.sortOrder}"/>"/>
    <input id="ob1" type="hidden" name="<%= ReportConstants.ORDER_BY %>" value="<c:out value="${command.orderBy}"/>"/>

    <c:if test="${command.lockDown}">
        <input id="cmdld1" type="hidden" name="<%= ParameterConstants.PERSONAL_DRILL_DOWN %>" value="<c:out value="${command.lockDown}"/>"/>                
    </c:if>

    <!-- drilldown info -->
    <c:if test="${command.drillDown}">
        <c:if test="${command.chartId != null}">
            <input id="ct7" type="hidden" name="<%= ReportConstants.CHART_REPORT_PARAM %>" value="Yes"/>
            <input id="ct8" type="hidden" name="<%= ReportConstants.CHART_DATA_ID %>" value="<c:out value="${command.chartId}"/>"/>
            <input id="ct9" type="hidden" name="<%= ReportConstants.CHART_ATTR_VALUE %>" value="<c:out value="${command.horizontalCriteriaValue}"/>"/>
        </c:if>
        <c:if test="${command.populationPersonId != null}">
            <input id="ct11" type="hidden" name="<%= ReportConstants.CHART_REPORT_PARAM %>" value="Yes"/>
            <input id="ct12" type="hidden" name="<%= ParameterConstants.POPULATION_PERSON_ID %>" value="<c:out value="${command.populationPersonId}"/>"/>
            <input id="ct13" type="hidden" name="<%= ReportConstants.CHART_ATTR_VALUE %>" value="<c:out value="${command.horizontalCriteriaValue}"/>"/>
        </c:if>
        <input id="origPopId12" type="hidden" name="<%= ReportConstants.ORIGINAL_POPULATION_ID %>" value="<c:out value="${command.populationIdsString}"/>"/>

        <input id="ha1" type="hidden" name="<%= ReportConstants.HORIZONTAL_ATTR %>" value="<c:out value="${command.horizontalCriteria}"/>"/>
        <input id="hzl1" type="hidden" name="<%= ReportConstants.HORIZONTAL_ATTR_LABEL %>" value="<c:out value="${command.horizontalCriteriaLabel}"/>"/>
        <input id="hzv1" type="hidden" name="<%= ReportConstants.HORIZONTAL_ATTR_VALUE %>" value="<c:out value="${command.horizontalCriteriaValue}"/>"/>
        
        <input id="va1" type="hidden" name="<%= ReportConstants.VERTICAL_ATTR %>" value="<c:out value="${command.verticalCriteria}"/>"/>
        <input id="vtl1" type="hidden" name="<%= ReportConstants.VERTICAL_ATTR_LABEL %>" value="<c:out value="${command.verticalCriteriaLabel}"/>"/>
        <input id="vtv1" type="hidden" name="<%= ReportConstants.VERTICAL_ATTR_VALUE %>" value="<c:out value="${command.verticalCriteriaValue}"/>"/>
    </c:if>

</zynap:form>