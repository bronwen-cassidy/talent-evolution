<%@ page import="com.zynap.talentstudio.dashboard.DashboardItem" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.data.FilledSeriesChartReport" %>
<%@ page import="com.zynap.talentstudio.web.organisation.Series" %>
<%@ page import="java.util.List" %>
<%@ include file="../../includes/include.jsp" %>
<script src="<c:url value="/js/plotly-latest.min.js"/>" type="text/javascript"></script>

<fieldset id="itemRowId-<c:out value="${dashboardItem.id}"/>">
    <legend><c:out value="${dashboardItem.label}"/></legend>
    <button id="deleteme-<c:out value="${dashboardItem.id}"/>" class="remove-dashboard-item" value="X">X</button>
    
    <div id="chart_<c:out value="${dashboardItem.id}"/>" class="col-10 scroll-x centered">

    </div>

</fieldset>

<script type="text/javascript">

    $(function() {
        var data = [];
        <%
            DashboardItem item = (DashboardItem) pageContext.getAttribute("dashboardItem");
            FilledSeriesChartReport filledReport = (FilledSeriesChartReport) pageContext.getAttribute("filledReport");
            final List<Series> seriesChartReportAnswers = filledReport.getSeriesChartReportAnswers();
            int index = 0;
            for (Series s : seriesChartReportAnswers) {
                index++;   
        %>
        
        var trace<%=index%> = {
            
            x: [ <%= s.getXAnswers() %> ],
            y: [<%= s.getYAnswers() %>],
            <% if(s.getDisplayAs().equals(Series.SCATTER)) {%>
            mode: 'lines+markers',
            <% } %>
            type: '<%= s.getDisplayAs() %>',
            name: '<%= s.getSeriesName() %>'
        };
        
        data.push(trace<%=index%>);
    <%
        }
    %>
        var layout = {
            xaxis: {
                title: '<%= filledReport.getXAxisLabel() %>'
                , autorange: false
                , categoryorder: 'array'
                , categoryarray: [<%= filledReport.getxAxisRange() %>]
            },
            title: '<%= item.getLabel() %>'
            ,showlegend: true
            ,autosize: true
        };
        Plotly.newPlot('chart_<c:out value="${dashboardItem.id}"/>', data, layout);
    });
    
    $(function() {
        $('.remove-dashboard-item').unbind('click');
        
        $('.remove-dashboard-item').on('click', function() {
            var elemId = $(this).attr("id");
            var itemId = elemId.substring(elemId.indexOf('-') + 1, elemId.length);
            
            $("#itemRowId-" + itemId).hide();
            
            $.get('dashboards/removemydashboard.htm?ts=' + new Date().getTime(), {iId: itemId});
        });
        
    });

</script>