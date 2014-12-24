<%@ include file="../../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_back" method="get" action="listcrosstabreports.htm">
           <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="javascript:document.forms._back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:evalButton userId="${command.report.userId}">
        <zynap:actionEntry>
            <input class="actionbutton" type="button" value="<fmt:message key="edit"/>" onclick="javascript:document.forms._edit.submit();"/>
        </zynap:actionEntry>
        <zynap:actionEntry>
            <input class="actionbutton" type="button" value="<fmt:message key="delete"/>" onclick="javascript:document.forms._delete.submit();"/>
        </zynap:actionEntry>
    </zynap:evalButton>
</zynap:actionbox>

<c:set var="rptName"><c:out value="${command.report.label}"/></c:set>
<fmt:message key="crosstab.report.information" var="detailsLabel"/>
<fmt:message key="report.runoptions" var="runoptionsLabel" />
<fmt:message key="report.results" var="resultsLabel" />
<fmt:message key="report.charts" var="chartLabel" />

<zynap:tab defaultTab="${command.activeTab}" id="report_info" url="javascript" tabParamName="activeTab">

    <zynap:tabName value="${detailsLabel}" name="details"  />
    <zynap:tabName value="${runoptionsLabel}" name="runoptions"  />

    <c:if test="${command.resultsDisplayable}">
        <zynap:tabName value="${resultsLabel}" name="results"  />
        <c:if test="${producer != null}">
            <zynap:tabName value="${chartLabel}" name="charts"/>
        </c:if>
    </c:if>

    <div id="details_span" style="display:<c:choose><c:when test="${command.activeTab == 'details'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${detailsLabel}" id="details">

                <table class="infotable" id="crosstabinfo" cellspacing="0">
                    <%@ include file="../viewreportcommon.jsp" %>

                    <tr>
                        <td class="infolabel"><fmt:message key="preferred.metric"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                        <c:choose>
                            <c:when test="${command.report.defaultMetric == null}">
                                <fmt:message key="scalar.operator.count"/>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${command.report.defaultMetric.label}"/>
                            </c:otherwise>
                        </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td class="infolabel"><fmt:message key="preferred.drilldown.report"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                            <c:out value="${command.report.drillDownReport.label}"/>
                        </td>
                    </tr>

                    <tr>
                        <td class="infolabel"><fmt:message key="preferred.display.report"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                            <c:out value="${command.report.displayReport.label}"/>
                        </td>
                    </tr>

                    <tr>
                        <td class="infolabel"><fmt:message key="preferred.display.limit"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                            <c:out value="${command.report.displayLimit}"/>
                        </td>
                    </tr>

                    <c:set var="verticalColumn" value="${command.verticalColumn}"/>
                    <c:set var="horizontalColumn" value="${command.horizontalColumn}"/>
                    <%@ include file="columns.jsp" %>

                </table>

            </zynap:infobox>
    </div>

    <div id="runoptions_span" style="display:<c:choose><c:when test="${command.activeTab == 'runoptions'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:infobox title="${runoptionsLabel}" id="runOptions">
            <form name="runreport" method="post" action="runviewcrosstabreport.htm">
                <input id="actCTbElId" type="hidden" name="activeTab" value="results"/>
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
                <c:if test="${producer.barChart || producer.overlaidChart && producer.hasValues}"> 
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

<%-- form that holds report id - set by javascript in viewrunreportcommon.jsp --%>
<zynap:form method="get" name="_run" action="runviewcrosstabreport.htm">
    <input type="hidden" id="hidden_report" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
</zynap:form>

<zynap:form method="get" name="_edit" action="editcrosstabreport.htm">
    <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
</zynap:form>

<zynap:form method="get" name="_delete" action="deletecrosstabreport.htm">
    <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${command.reportId}"/>"/>
</zynap:form>
