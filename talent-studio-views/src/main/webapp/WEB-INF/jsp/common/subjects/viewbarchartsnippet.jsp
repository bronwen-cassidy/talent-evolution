<%
    BarChartProducer chartProducer = (BarChartProducer) pageContext.getAttribute("filledReportProducer");
    Object baseUrl = request.getAttribute("ddUrl");
    if (baseUrl != null) chartProducer.setBaseUrl(baseUrl.toString());
    Object num = pageContext.getAttribute("chartIdNum");
    int idNum = Integer.parseInt(String.valueOf(num));
    String chartId = "barChart" + idNum;
%>

<cewolf:chart id="<%=chartId%>" type="verticalBar3D" showlegend="false" xaxislabel="" yaxislabel="" antialias="true">
    <cewolf:data>
        <cewolf:producer id="chartView"/>
    </cewolf:data>
    <cewolf:colorpaint color="#fafafa"/>
    <cewolf:chartpostprocessor id="colourProcessor"/>
</cewolf:chart>

<cewolf:img chartid="<%=chartId%>" renderer="/cewolf" width="400" height="250" alt="Bar chart">
    <cewolf:map linkgeneratorid="chartView" tooltipgeneratorid="chartView"/>
</cewolf:img>