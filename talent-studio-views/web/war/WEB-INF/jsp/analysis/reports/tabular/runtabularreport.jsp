<%@ include file="../../../includes/include.jsp" %>

<zynap:evalBack>
    <zynap:actionbox>
        <zynap:actionEntry>
            <fmt:message key="back" var="buttonLabel" />
            <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" buttonType="button" method="get"/>
        </zynap:actionEntry>
    </zynap:actionbox>
</zynap:evalBack>

<c:set var="rptName"><c:out value="${command.report.label}"/></c:set>
<fmt:message key="report.runoptions" var="runoptionsLabel" />
<fmt:message key="report.results" var="resultsLabel" />

<zynap:tab defaultTab="${command.activeTab}" id="report_info" url="javascript" tabParamName="activeTab">

    <zynap:tabName value="${runoptionsLabel}" name="runoptions"  />

    <c:if test="${command.resultsDisplayable}">
        <zynap:tabName value="${resultsLabel}" name="results"  />
    </c:if>

    <c:set var="selectedColumn" value="${command.selectedColumn}"/>
    <div id="runoptions_span" style="display:<c:choose><c:when test="${command.activeTab == 'runoptions'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${rptName}" id="runOptions">
            <form name="runreport" method="post" action="runtabularreport.htm">
                <table class="infotable" id="reportrun" cellspacing="0">
                    <%@ include file="runoptions.jsp" %>
                </table>
            </form>
        </zynap:infobox>
    </div>
      <%@ include file="../loading.jsp" %>
    <c:if test="${command.resultsDisplayable}">
        <div id="results_span" style="display:<c:choose><c:when test="${command.activeTab == 'results'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
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
    <%@include file="../common/legendcolourpopup.jsp" %>
</c:if>

<%@include file="exportform.jsp"%>
<%@include file="exportreportpdfform.jsp"%>