<%@ include file="../../includes/include.jsp" %>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>

<fieldset>
    <legend><c:out value="${dashboardItem.label}"/></legend>
    <c:if test="${dashboardItem.description != null && dashboardItem != ''}">
        <div class="infomessage"><c:out value="${dashboardItem.description}"/></div>
    </c:if>
    <%
        int idNum3 = Integer.parseInt(String.valueOf(pageContext.getAttribute("chartIdNum")));
        String spiderChartId = "spiderChart" + idNum3;
    %>

    <c:set var="spiderChartProducer" value="${filledReport.producer}"/>
    <c:set var="linkGenerator" value="${filledReport.postProcessor}"/>
    <c:set var="toolTipGenerator" value="${filledReport.postProcessor}"/>
    <c:set var="spiderPostProcessor" value="${filledReport.postProcessor}"/>

    <cewolf:spiderchart id="<%=spiderChartId%>" type="spider" showlegend="true">
        <cewolf:data>
            <cewolf:producer id="spiderChartProducer"/>
        </cewolf:data>
        <cewolf:colorpaint color="#fafafa"/>
        <cewolf:chartpostprocessor id="spiderPostProcessor"/>
    </cewolf:spiderchart>

    <cewolf:img chartid="<%=spiderChartId%>" renderer="/cewolf" width="400" height="300" alt="Spider chart">
        <cewolf:map tooltipgeneratorid="toolTipGenerator"/>
    </cewolf:img>
</fieldset>