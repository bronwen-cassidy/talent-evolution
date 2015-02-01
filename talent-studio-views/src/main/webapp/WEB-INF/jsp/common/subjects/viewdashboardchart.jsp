<%@include file="../../includes/include.jsp"%>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>

    <cewolf:chart id="pieChart" type="pie" showlegend="false">
        <cewolf:data>
            <cewolf:producer id="chartView"/>
        </cewolf:data>
        <cewolf:chartpostprocessor id="colourProcessor"/>
        <cewolf:chartpostprocessor id="labelProcessor"/>
    </cewolf:chart>

    <cewolf:img chartid="pieChart" renderer="/cewolf" width="400" height="250" alt="Pie chart">
        <cewolf:map linkgeneratorid="chartView" tooltipgeneratorid="chartView"/>
    </cewolf:img>