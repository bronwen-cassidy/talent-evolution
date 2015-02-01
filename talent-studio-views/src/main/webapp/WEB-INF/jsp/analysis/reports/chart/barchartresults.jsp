<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.producers.BarChartProducer" %>
<%@ include file="../../../includes/include.jsp" %>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>


<c:set var="baseUrl" value="runtabularreport.htm"/>
<zynap:artefactLink var="ddUrl" url="${baseUrl}" tabName="activeTab" activeTab="results">
    <zynap:param name="<%= ParameterConstants.REPORT_ID %>" value="${command.drillDownReportId}"/>
    <zynap:param name="<%= ReportConstants.ORIGINAL_POPULATION_ID %>" value="${command.populationIdsString}"/>
</zynap:artefactLink>

<fmt:message key="chart.report.answers" var="xLabel" scope="page"/>
<fmt:message key="chart.report.count" var="yLabel" scope="page"/>

<%
    BarChartProducer producer = (BarChartProducer) request.getAttribute(ChartConstants.PRODUCER_PARAM);
    Object baseUrl = request.getAttribute("ddUrl");
    if (baseUrl != null) {
        producer.setBaseUrl(baseUrl.toString());
    }
    final String xAxisLabel = (String) pageContext.getAttribute("xLabel");
    final String yAxisLabel = (String) pageContext.getAttribute("yLabel");
%>

<c:set var="chartView" value="${producer}"/>
<c:set var="colourProcessor" value="${producer}"/>

<div class="chart" id="chrtrpts">
    <cewolf:chart id="barChart" type="verticalBar3D" showlegend="false" xaxislabel="<%= xAxisLabel %>" yaxislabel="<%=yAxisLabel%>" antialias="true">
        <cewolf:data>
            <cewolf:producer id="chartView"/>
        </cewolf:data>
        <cewolf:colorpaint color="#fafafa00"/>
        <cewolf:chartpostprocessor id="colourProcessor"/>        
    </cewolf:chart>

    <cewolf:img chartid="barChart" renderer="/cewolf" width="800" height="500" alt="Bar chart">
        <cewolf:map linkgeneratorid="chartView" tooltipgeneratorid="chartView"/>
    </cewolf:img>

    <div class="legend">
        <cewolf:legend id="barChart" renderer="/cewolf" width="800" height="150"/>
    </div>
</div>