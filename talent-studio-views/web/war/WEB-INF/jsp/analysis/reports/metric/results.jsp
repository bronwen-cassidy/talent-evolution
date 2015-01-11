<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.RunReportController"%>
<%@ page import="com.zynap.talentstudio.analysis.reports.ReportConstants"%>

<%-- include to display results of metric reports --%>

<c:set var="baseUrl" value="runtabularreport.htm"/>

<c:if test="${command.report.drillDownReport.reportType == 'CROSSTAB'}">
    <c:set var="baseUrl" value="runcrosstabreport.htm"/>
</c:if>
<c:if test="${command.report.drillDownReport.reportType == 'METRIC'}">
    <c:set var="baseUrl" value="runmetricreport.htm"/>
</c:if>

<zynap:artefactLink var="ddUrl" url="${baseUrl}" tabName="activeTab" activeTab="results" >
    <zynap:param name="<%= ParameterConstants.REPORT_ID %>" value="${command.drillDownReportId}"/>
    <zynap:param name="<%= ReportConstants.ORIGINAL_POPULATION_ID %>" value="${command.populationIdsString}"/>
</zynap:artefactLink>

<c:if test="${command.drillDownReportId == null}">
    <c:set var="ddUrl" value=""/>
</c:if>

<c:set var="jasperPrint" value="${command.filledReport.jasperPrint}"/>
<zynap:jasperTabularReportTag jasperPrint="${jasperPrint}" viewSubjectUrl="" viewPositionUrl="" viewQuestionnaireUrl="" drillDownUrl="${ddUrl}" report="${command.report}" />


<zynap:form method="post" name="<%=ReportConstants.METRIC_COL_HEADER_FORM%>">
    <input type="hidden" name="_target2" value="2"/>
    <input id="<%=ReportConstants.METRIC_COL_ID%>" type="hidden" name="<%=ReportConstants.COL_ID_PARAM%>" value=""/>
    <input id="actmettab" type="hidden" name="activeTab" value="charts"/>
</zynap:form>

 <zynap:form method="post" name="<%=ReportConstants.METRIC_BAR_SELECT_FORM%>">
    <input type="hidden" name="_target3" value="3"/>
    <input id="actmettab" type="hidden" name="activeTab" value="charts"/>
</zynap:form>