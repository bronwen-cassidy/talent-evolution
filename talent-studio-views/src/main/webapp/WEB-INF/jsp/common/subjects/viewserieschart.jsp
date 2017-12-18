<%@ page import="com.zynap.talentstudio.dashboard.DashboardItem" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.data.FilledSeriesChartReport" %>
<%@ page import="com.zynap.talentstudio.web.organisation.Series" %>
<%@ page import="java.util.List" %>
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
            final List<Series> seriesChartReportAnswers = filledReport.getSeriesChartReportAnswers();
            int index = 0;
            for (Series s : seriesChartReportAnswers) {
                index++;   
        %>
        var data = [];
        
        var trace<%=index%> = {
            
            x: [ <%= s.getXAnswers() %> ],
            y: [<%= s.getYAnswers() %>],
            mode: 'lines+markers',
            type: 'scatter',
            name: '<%= s.getSeriesName() %>'
        };
        
        data.push(trace<%=index%>);
    <%
        }
    %>
        var layout = {
            xaxis: {
                autorange: false
                , categoryorder: 'array'
                , categoryarray: [<%= filledReport.getxAxisRange() %>]
            },
            title: '<%= filledReport.getXAxisLabel()  %>'
            ,showlegend: true
        };
        Plotly.newPlot('chart_<c:out value="${dashboardItem.id}"/>', data, layout);
    });

</script>