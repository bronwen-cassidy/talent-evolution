<%@ page import="com.zynap.talentstudio.questionnaires.Questionnaire" %>
<%@ page import="com.zynap.talentstudio.web.organisation.ChartPoint" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.data.FilledSeriesChartReport" %>
<%@ page import="com.zynap.talentstudio.dashboard.DashboardItem" %>
<%@ include file="../../includes/include.jsp" %>
<script src="<c:url value="/js/plotly-latest.min.js"/>" type="text/javascript"></script>

<fieldset>
    <legend><c:out value="${dashboardItem.label}"/></legend>
    <c:if test="${dashboardItem.description != null && dashboardItem != ''}">
        <div class="infomessage"><c:out value="${dashboardItem.description}"/></div>
    </c:if>

    <div id="chart_<c:out value="${dashboardItem.id}"/>">

    </div>

</fieldset>

<script type="text/javascript">

    $(function() {

        <%
            DashboardItem item = (DashboardItem) pageContext.getAttribute("dashboardItem");
            FilledSeriesChartReport filledReport = (FilledSeriesChartReport) pageContext.getAttribute("filledReport");
        %>

        var trace1 = {

            <% Map<Questionnaire, ChartPoint> data = filledReport.getSeriesChartReportAnswers(); %>
            x: [
                <%= filledReport.getxAxisRange() %>
            ].map(function(y) {
                return y.toString();
            }),
            y: [
                <%= filledReport.getyAxisRange() %>
            ].map(function(y) {
                return y.toString();
            }),
            mode: 'lines',
            type: 'scatter',
            name: '<%= item.getLabel() %>',
            showlegend: true
        };

        var data = [trace1];

        var layout = {
             xaxis: {
                 title: '<%= filledReport.getXAxisLabel()  %>'
                 , autorange: false
                 , range: [<%= filledReport.getxAxisRange() %>]
             },
             yaxis: {
                 title: '<%= filledReport.getYAxisLabel()  %>'
                 , autorange: false
                 , range: [<%= filledReport.getyAxisRange() %>]
             },
            legend: {"orientation": "h"}
        };

        Plotly.newPlot('chart_<c:out value="${dashboardItem.id}"/>', data, layout);
    });

</script>