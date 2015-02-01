<%@ page import="com.zynap.talentstudio.analysis.reports.Report" %>
<%@ include file="../../../includes/include.jsp" %>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_back" method="get" action="listchartreports.htm">
            <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="document.forms._back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:evalButton userId="${command.report.userId}">
        <zynap:actionEntry>
            <zynap:form method="get" name="_edit" action="editspiderchartreport.htm">
                <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
                <input class="actionbutton" id="repEditBtn" type="button" value="<fmt:message key="edit"/>" onclick="document.forms._edit.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
        <zynap:actionEntry>
            <zynap:form method="get" name="_delete" action="deletechartreport.htm">
                <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
                <input class="actionbutton" type="button" value="<fmt:message key="delete"/>" onclick="document.forms._delete.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
    </zynap:evalButton>
</zynap:actionbox>

<c:set var="rptName"><c:out value="${command.report.label}"/></c:set>
<fmt:message key="chart.report.information" var="detailsLabel"/>
<fmt:message key="report.runoptions" var="runoptionsLabel"/>
<fmt:message key="report.results" var="resultsLabel"/>

<zynap:tab defaultTab="${command.activeTab}" id="report_info" url="javascript" tabParamName="activeTab">

    <zynap:tabName value="${detailsLabel}" name="details"/>
    <zynap:tabName value="${runoptionsLabel}" name="runoptions"/>

    <c:if test="${command.resultsDisplayable}">
        <zynap:tabName value="${resultsLabel}" name="results"/>
    </c:if>

    <div id="details_span" style="display:<c:choose><c:when test="${command.activeTab == 'details'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${detailsLabel}" id="details">

            <table class="infotable" id="reportinfo" cellspacing="0">
                <c:set var="columns" value="${command.columns}"/>
                <%@ include file="../viewreportcoreinfo.jsp" %>
                <%@ include file="viewspiderreportcoreinfo.jsp" %>
                <%@ include file="../viewreportarenainfo.jsp" %>                
                <%@ include file="viewspidercolumnattributes.jsp" %>
            </table>
        </zynap:infobox>
    </div>

    <div id="runoptions_span"
         style="display:<c:choose><c:when test="${command.activeTab == 'runoptions'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${runoptionsLabel}" id="runOptions">
            <form name="runreport" method="post" action="runviewspiderchartreport.htm">
                <table class="infotable" id="reportrun" cellspacing="0">
                    <%@ include file="runoptions.jsp" %>
                </table>
            </form>
        </zynap:infobox>
    </div>
    <%@ include file="../loading.jsp" %>

    <c:if test="${command.resultsDisplayable}">

        <div id="results_span"
             style="display:<c:choose><c:when test="${command.activeTab == 'results'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <zynap:infobox title="${rptName}" id="results">                
                <c:set var="targetPage" value="0" scope="request"/>
                <c:set var="spiderChartProducer" value="${command.chartProducer}"/>
                <c:set var="linkGenerator" value="${command.postProcessor}"/>
                <c:set var="toolTipGenerator" value="${command.postProcessor}"/>
                <c:set var="spiderPostProcessor" value="${command.postProcessor}"/>

                <cewolf:spiderchart id="spiderChart" type="spider" showlegend="true">
                    <cewolf:data>
                        <cewolf:producer id="spiderChartProducer"/>
                    </cewolf:data>
                    <cewolf:colorpaint color="#fafafa"/>
                    <cewolf:chartpostprocessor id="spiderPostProcessor"/>
                </cewolf:spiderchart>

                <cewolf:img chartid="spiderChart" renderer="/cewolf" width="500" height="400" alt="Spider chart">
                    <cewolf:map tooltipgeneratorid="toolTipGenerator"/>
                </cewolf:img>
                <%-- todo using img src= --%>
                <%--<img src="<c:url value="viewspiderchart.htm"><c:param name="id" value="${command.reportId}"/><c:param name="populationIds" value="${command.populationIdsString}"/></c:url>" alt="spider graph"/>--%>
            </zynap:infobox>
        </div>
    </c:if>

</zynap:tab>