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
<fmt:message key="report.charts" var="chartLabel" />

<zynap:tab defaultTab="${command.activeTab}" id="report_info" url="javascript" tabParamName="activeTab">

    <zynap:tabName value="${runoptionsLabel}" name="runoptions"  />

    <c:if test="${command.resultsDisplayable}">
        <zynap:tabName value="${resultsLabel}" name="results"  />
    </c:if>

    <c:if test="${producer != null || optionsDisplayable}">
        <zynap:tabName value="${chartLabel}" name="charts"/>
    </c:if>

    <div id="runoptions_span" style="display:<c:choose><c:when test="${command.activeTab == 'runoptions'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${rptName}" id="runOptions">
            <form name="runreport" method="post" action="runmetricreport.htm">
                <input id="actCTbElId" type="hidden" name="activeTab" value="results"/>
                <table class="infotable" id="reportrun" cellspacing="0">
                    <%@include file="runoptions.jsp"%>
                </table>
            </form>
        </zynap:infobox>
    </div>
    <%@ include file="../loading.jsp" %>
    <c:if test="${command.resultsDisplayable}">
        <div id="results_span" style="display:<c:choose><c:when test="${command.activeTab == 'results'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <zynap:infobox title="${rptName}" id="results">
                <%@ include file="results.jsp" %>
            </zynap:infobox>
        </div>
    </c:if>

    <c:if test="${command.resultsDisplayable}">
        <div id="charts_span" style="display:<c:choose><c:when test="${command.activeTab == 'charts'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <zynap:infobox title="${rptName}" id="chartbox">
                <c:if test="${optionsDisplayable}">
                    <%@ include file="chartoptions.jsp" %>
                </c:if>
                <c:if test="${producer != null}">
                    <%@ include file="../common/charts.jsp" %>
                </c:if>
            </zynap:infobox>
        </div>
    </c:if>

</zynap:tab>
