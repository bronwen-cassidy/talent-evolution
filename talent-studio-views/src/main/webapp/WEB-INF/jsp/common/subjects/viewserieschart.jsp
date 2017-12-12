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
        
        var trace1 = {
            x: ['aaaa', 'bbbbb', 'ccccc', 'dddddd'],
            y: ['first', 'second', 'third', 'fourth'],
            mode: 'lines',
            type: 'scatter',
            showlegend: true
        };

        var data = [trace1];
        
        var layout = {
            legend: {"orientation": "h"},
            title:'Full cusum LLR for 2 Odds'
        };
        
        Plotly.newPlot('chart_<c:out value="${dashboardItem.id}"/>', data, layout);
    });
    
</script> 