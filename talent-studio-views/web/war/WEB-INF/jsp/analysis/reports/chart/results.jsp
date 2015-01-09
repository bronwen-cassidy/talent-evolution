<%@ page import="ReportConstants" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.ChartConstants" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.producers.PieChartProducer" %>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>


<c:set var="baseUrl" value="runtabularreport.htm"/>
<zynap:artefactLink var="ddUrl" url="${baseUrl}" tabName="activeTab" activeTab="results">
    <zynap:param name="<%= ParameterConstants.REPORT_ID %>" value="${command.drillDownReportId}"/>
    <zynap:param name="<%= ReportConstants.ORIGINAL_POPULATION_ID %>" value="${command.populationIdsString}"/>
</zynap:artefactLink>

<%
    PieChartProducer chartProducer = (PieChartProducer) request.getAttribute(ChartConstants.PRODUCER_PARAM);
    final int width = chartProducer.getPreferredWidth();
    Object baseUrl = request.getAttribute("ddUrl");
    if (baseUrl != null) {
        chartProducer.setBaseUrl(baseUrl.toString());
    }

%>

<c:set var="chartView" value="${producer}"/>
<c:set var="labelProcessor" value="${producer}"/>

<div class="chart" id="chrtrpts">
    <cewolf:chart id="pieChart" type="pie3D" showlegend="false">
        <cewolf:gradientpaint>
            <cewolf:point x="0" y="0" color="#FFFFFF" />
            <cewolf:point x="300" y="0" color="#DDDDFF" />
        </cewolf:gradientpaint>
        <cewolf:data>
            <cewolf:producer id="chartView"/>
        </cewolf:data>
        <cewolf:colorpaint color="#fafafa"/>
        <cewolf:chartpostprocessor id="labelProcessor"/>
    </cewolf:chart>

    <cewolf:img chartid="pieChart" renderer="/cewolf" width="800" height="500" alt="Pie chart">
        <cewolf:map linkgeneratorid="chartView" tooltipgeneratorid="chartView"/>
    </cewolf:img>

    <div class="legend">
        <cewolf:legend id="pieChart" renderer="/cewolf" width="<%=width%>" height="150"/>
    </div>
</div>