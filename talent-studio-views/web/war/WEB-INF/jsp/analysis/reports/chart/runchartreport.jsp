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

    <div id="runoptions_span" style="display:<c:choose><c:when test="${command.activeTab == 'runoptions'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${rptName}" id="runOptions">
            <form name="runreport" method="post" action="runchartreport.htm">
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
                <c:set var="targetPage" value="0" scope="request"/>
                <c:set var="producer" value="${command.producer}" scope="request"/>
                <c:set var="isPieChart" value="${command.pieChart}" scope="request"/>
                <c:if test="${isPieChart}"><%@ include file="results.jsp" %></c:if>
                <c:if test="${!isPieChart}"><%@ include file="barchartresults.jsp" %></c:if>                
            </zynap:infobox>
        </div>
    </c:if>

</zynap:tab>