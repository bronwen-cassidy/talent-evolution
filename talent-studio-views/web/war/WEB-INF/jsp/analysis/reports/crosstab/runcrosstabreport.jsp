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
        <c:if test="${producer != null}">
            <zynap:tabName value="${chartLabel}" name="charts"/>
        </c:if>
    </c:if>

    <div id="runoptions_span" style="display:<c:choose><c:when test="${command.activeTab == 'runoptions'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${rptName}" id="runOptions">
            <form name="runreport" method="post" action="runcrosstabreport.htm">
                <input id="actCTbElId" type="hidden" name="activeTab" value="results"/>
                <table class="infotable" id="reportrun" cellspacing="0">
                    <%@include file="runoptions.jsp"%>
                </table>
            </form>
        </zynap:infobox>
    </div>
    <%@ include file="../loading.jsp" %>
    <c:set var="verticalColumn" value="${command.report.verticalColumn}"/>
    <c:set var="horizontalColumn" value="${command.report.horizontalColumn}"/>

    <c:if test="${command.resultsDisplayable}">
        <div id="results_span" style="display:<c:choose><c:when test="${command.activeTab == 'results'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <zynap:infobox title="${rptName}" id="results">
                <%@ include file="results.jsp" %>
            </zynap:infobox>
            <c:if test="${command.hasResults}">
                <%@include file="summary.jsp"%>
            </c:if>
        </div>
    </c:if>

    <c:if test="${command.resultsDisplayable && producer != null}">
        <div id="charts_span" style="display:<c:choose><c:when test="${command.activeTab == 'charts'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <zynap:infobox title="${rptName}" id="chartbox">
                <c:if test="${producer.hasValues}">
                    <zynap:form name="genBarCt" method="post">
                        <table class="infotable" id="chrt">
                            <%@include file="../common/chartselect.jsp"%>
                        </table>
                    </zynap:form>
                </c:if>
               <%@ include file="../common/charts.jsp" %>
            </zynap:infobox>
        </div>
    </c:if>

</zynap:tab>

