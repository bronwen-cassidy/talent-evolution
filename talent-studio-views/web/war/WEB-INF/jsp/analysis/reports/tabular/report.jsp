<%@ page import="com.zynap.talentstudio.web.analysis.reports.RunReportController" %>
<%@ include file="../../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_back" method="get" action="listreports.htm">
            <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>"
                   onclick="javascript:document.forms._back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:evalButton userId="${command.reportUserId}">
        <zynap:actionEntry>
            <zynap:form method="get" name="_edit" action="editstandardreport.htm">
                <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
                <input class="actionbutton" id="repEditBtn" type="button" value="<fmt:message key="edit"/>" onclick="javascript:document.forms._edit.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
        <zynap:actionEntry>
            <zynap:form method="get" name="_delete" action="deletereport.htm">
                <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
                <input class="actionbutton" type="button" value="<fmt:message key="delete"/>" onclick="javascript:document.forms._delete.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
    </zynap:evalButton>
</zynap:actionbox>

<c:set var="rptName"><c:out value="${command.report.label}"/></c:set>
<fmt:message key="standard.report.information" var="detailsLabel"/>
<fmt:message key="report.runoptions" var="runoptionsLabel"/>
<fmt:message key="report.results" var="resultsLabel"/>

<zynap:tab defaultTab="${command.activeTab}" id="report_info" url="javascript" tabParamName="activeTab">

    <zynap:tabName value="${detailsLabel}" name="details"/>
    <zynap:tabName value="${runoptionsLabel}" name="runoptions"/>

    <c:if test="${command.resultsDisplayable}">
        <zynap:tabName value="${resultsLabel}" name="results"/>
    </c:if>

    <c:set var="selectedColumn" value="${command.selectedColumn}"/>

    <div id="details_span"
         style="display:<c:choose><c:when test="${command.activeTab == 'details'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${detailsLabel}" id="details">

            <table class="infotable" id="reportinfo" cellspacing="0">
                <%@ include file="../viewreportcommon.jsp" %>
                <fmt:message var="columnHeader" key="report.columns"/>

                <c:set var="showLegend" value="true"/>
                <c:set var="showGroups" value="true"/>
                <c:set var="columns" value="${command.columns}"/>

                <%@ include file="../viewcolumns.jsp" %>
            </table>
        </zynap:infobox>
    </div>

    <div id="runoptions_span"
         style="display:<c:choose><c:when test="${command.activeTab == 'runoptions'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${runoptionsLabel}" id="runOptions">
            <form name="runreport" method="post" action="runviewstandardreport.htm">
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
                <c:set var="resultset" value="${command}"/>
                <c:set var="jasperPrint" value="${command.filledReport.jasperPrint}"/>
                <c:set var="targetPage" value="0" scope="request"/>
                <c:set var="lockDown" value="${command.lockDown}"/>
                <%@ include file="results.jsp" %>
            </zynap:infobox>
        </div>
    </c:if>

</zynap:tab>

<c:if test="${selectedColumn != null && showPopup}">
    <%@ include file="../common/legendcolourpopup.jsp" %>
</c:if>

<%-- form that holds report id - set by javascript in viewrunreportcommon.jsp --%>
<zynap:form method="get" name="_run" action="runviewstandardreport.htm">
    <input type="hidden" id="hidden_report" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
</zynap:form>

<%@ include file="exportform.jsp" %>
<%@ include file="exportreportpdfform.jsp" %>
