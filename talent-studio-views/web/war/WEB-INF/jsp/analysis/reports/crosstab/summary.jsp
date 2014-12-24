<%@ page import="com.zynap.talentstudio.analysis.populations.IPopulationEngine" %>
<%@ page import="static com.zynap.talentstudio.web.common.ParameterConstants.*" %>
<%@ page import="static com.zynap.talentstudio.analysis.reports.ReportConstants.*" %>
<table class="summarytable" cellpadding="0" cellspacing="0">
    <tr>
        <td class="summarylabel">
            <fmt:message key="summary" var="summy"/>
            <zynap:infobox title="${summy}" id="ressummary">
                <!-- todo add the summary table here -->
                <table class="infotable" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="summarylabel"><fmt:message key="report.population"/></td>
                        <td class="summarydata"><c:out value="${command.population.label}"/></td>
                    </tr>
                    <tr>
                        <td class="summarylabel"><fmt:message key="missing"/> <c:out value="${command.horizontalHeader}"/></td>
                        <td class="summarydata">
                            <c:choose>
                                <c:when test="${command.drillDownReportSet}">
                                    <zynap:artefactLink var="hhdrilldown" url="${baseUrl}" tabName="activeTab" activeTab="results">
                                        <zynap:param name="<%= REPORT_ID %>" value="${command.drillDownReportId}"></zynap:param>
                                        <zynap:param name="<%= ORIGINAL_POPULATION_ID %>" value="${command.populationIdsString}"></zynap:param>

                                        <zynap:param name="<%= VERTICAL_ATTR %>" value="${command.verticalColumnAttribute}"></zynap:param>
                                        <zynap:param name="<%= VERTICAL_ATTR_VALUE %>" value="<%=IPopulationEngine._CROSS_TAB_TOTAL%>"></zynap:param>
                                        <zynap:param name="<%= VERTICAL_ATTR_LABEL %>" value="${command.verticalHeader} = Total"></zynap:param>

                                        <zynap:param name="<%= HORIZONTAL_ATTR %>" value="${command.horizontalColumnAttribute}"></zynap:param>
                                        <zynap:param name="<%= HORIZONTAL_ATTR_VALUE %>" value=""></zynap:param>
                                        <zynap:param name="<%= HORIZONTAL_ATTR_LABEL %>" value="${command.horizontalHeader} = N/A"></zynap:param>
                                    </zynap:artefactLink>
                                    <a href="<c:out value="${hhdrilldown}"/>" >
                                        <c:out value="${command.columnNaTotal}"/>
                                    </a>
                                </c:when>                            
                                <c:otherwise>
                                    <c:out value="${command.columnNaTotal}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td class="summarylabel"><fmt:message key="missing"/> <c:out value="${command.verticalHeader}"/></td>
                        <td class="summarydata">
                            <c:choose>
                                <c:when test="${command.drillDownReportSet}">
                                    <zynap:artefactLink var="vhdrilldown" url="${baseUrl}" tabName="activeTab" activeTab="results">
                                        <zynap:param name="<%= REPORT_ID %>" value="${command.drillDownReportId}"></zynap:param>
                                        <zynap:param name="<%= ORIGINAL_POPULATION_ID %>" value="${command.populationIdsString}"></zynap:param>

                                        <zynap:param name="<%= VERTICAL_ATTR %>" value="${command.verticalColumnAttribute}"></zynap:param>
                                        <zynap:param name="<%= VERTICAL_ATTR_VALUE %>" value=""></zynap:param>
                                        <zynap:param name="<%= VERTICAL_ATTR_LABEL %>" value="${command.verticalHeader} = N/A"></zynap:param>

                                        <zynap:param name="<%= HORIZONTAL_ATTR %>" value="${command.horizontalColumnAttribute}"></zynap:param>
                                        <zynap:param name="<%= HORIZONTAL_ATTR_VALUE %>" value="<%=IPopulationEngine._CROSS_TAB_TOTAL%>"></zynap:param>
                                        <zynap:param name="<%= HORIZONTAL_ATTR_LABEL %>" value="${command.horizontalHeader} = Total"></zynap:param>
                                    </zynap:artefactLink>
                                    <a href="<c:out value="${vhdrilldown}"/>" >
                                        <c:out value="${command.rowNaTotal}"/>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${command.rowNaTotal}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <fmt:message key="${command.population.type}s" var="popType"/>
                        <td class="summarylabel"><fmt:message key="objective.total"><fmt:param value="${popType}"/></fmt:message></td>
                        <td class="summarydata"><c:out value="${command.total}"/></td>
                    </tr>
                </table>
            </zynap:infobox>
        </td>
    </tr>
</table>