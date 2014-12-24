<%
    PieChartProducer chartProducer = (PieChartProducer) pageContext.getAttribute("filledReportProducer");
    Object baseUrl = request.getAttribute("ddUrl");
    if (baseUrl != null) chartProducer.setBaseUrl(baseUrl.toString());
    Object num3 = pageContext.getAttribute("chartIdNum");
    int idNum3 = Integer.parseInt(String.valueOf(num3));
    String chartId = "pieChart" + idNum3;
%>

<cewolf:chart id="<%=chartId%>" type="pie3D" showlegend="false">
    <cewolf:data>
        <cewolf:producer id="chartView"/>
    </cewolf:data>
    <cewolf:colorpaint color="#fafafa"/>
    <cewolf:chartpostprocessor id="colourProcessor"/>
</cewolf:chart>

<cewolf:img chartid="<%=chartId%>" renderer="/cewolf" width="400" height="250" alt="Pie chart">
    <cewolf:map linkgeneratorid="chartView" tooltipgeneratorid="chartView"/>
</cewolf:img>