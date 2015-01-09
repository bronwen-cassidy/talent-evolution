<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.RunCrossTabController"%>
<%@ page import="ReportConstants"%>

 <%-- include to display results of cross tab reports --%>

<zynap:artefactLink var="viewSubjectUrl" url="viewsubject.htm" tabName="activeTab" activeTab="results"/>
<zynap:artefactLink var="viewPositionUrl" url="viewposition.htm" tabName="activeTab" activeTab="results"/>
<zynap:artefactLink var="viewQuestionnaireUrl" url="viewsubjectquestionnaire.htm" tabName="activeTab" activeTab="results"/>

<c:choose>
    <c:when test="${command.overflow != null}">
        <div class="error">
            <fmt:message key="report.display.overflow"/>
       </div>
    </c:when>

    <c:when test="${not command.hasResults}">
        <div class="infomessage">
            <fmt:message key="no.results"/>
       </div>
    </c:when>

    <c:otherwise>
        <c:set var="baseUrl" value="runtabularreport.htm"/>
        <c:if test="${command.report.drillDownReport.reportType == 'CROSSTAB'}">
            <c:set var="baseUrl" value="runcrosstabreport.htm"/>
        </c:if>
        <c:if test="${command.report.drillDownReport.reportType == 'METRIC'}">
            <c:set var="baseUrl" value="runmetricreport.htm"/>
        </c:if>

        <c:choose>
            <c:when test="${command.artefactView}">

                <c:if test="${command.drillDownReportSet}">
                    <zynap:artefactLink var="drillDownURL" url="${baseUrl}" tabName="activeTab" activeTab="results" >
                        <zynap:param name="<%= ParameterConstants.REPORT_ID %>" value="${command.drillDownReportId}"></zynap:param>
                        <zynap:param name="<%= ReportConstants.ORIGINAL_POPULATION_ID %>" value="${command.populationIdsString}"></zynap:param>
                        <zynap:param name="<%= ReportConstants.VERTICAL_ATTR %>" value="${command.verticalColumnAttribute}"></zynap:param>
                        <zynap:param name="<%= ReportConstants.HORIZONTAL_ATTR %>" value="${command.horizontalColumnAttribute}"></zynap:param>
                     </zynap:artefactLink>
                </c:if>

                <fmt:message var="drillDownAltText" key="crosstab.drilldown.alttext"/>

                <zynap:jasperTabularReportTag jasperPrint="${command.filledReport.jasperPrint}" viewSubjectUrl="${viewSubjectUrl}"
                                                viewPositionUrl="${viewPositionUrl}" viewQuestionnaireUrl="${viewQuestionnaireUrl}" report="${command.report}"
                                                drillDownUrl="${drillDownURL}" drillDownAltText="${drillDownAltText}" forCrossTab="true"
                                                columnHeadings="${command.horizontalHeadings}" rowHeadings="${command.rowHeadings}"/>
            </c:when>
            <c:otherwise>
                <table class="crosstabtable" cellspacing="0">
                    <tr>
                        <td class="crosstabaxisheader">
                            <table width="100%" height="100%">
                                <tr>
                                    <td class="crosstabcolumntitle">
                                        <a href="#" onclick="document.forms.runBarChartHForm.submit();"><c:out value="${command.horizontalHeader}"/></a>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="crosstabrowtitle">
                                         <a href="#" onclick="document.forms.runBarChartVForm.submit();"><c:out value="${command.verticalHeader}"/></a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <c:forEach var="colHeader" items="${command.horizontalHeadings}" varStatus="colIndex">
                            <td class="crosstabcolumnheader">
                                <a href="#" onclick="submitChartFormValues('runColForm', 'ct_colId2', '<c:out value="${command.horizontalHeader}"/>', 'ct_colIndex', '<c:out value="${colIndex.index}"/>');">
                                        <c:out value="${colHeader.label}"/>
                                </a>
                            </td>
                        </c:forEach>
                    </tr>

                    <c:forEach var="row" items="${command.rows}" varStatus="rowIndex">
                        <tr>
                            <td class="crosstabrowheader">
                                <a href="#" onclick="submitChartFormValues('runRowForm', 'ct_rowId2', '<c:out value="${command.verticalHeader}"/>', 'ct_rowIndex', '<c:out value="${rowIndex.index}"/>');"><c:out value="${row.heading.label}"/></a>
                            </td>
                            <c:forEach var="cell" items="${row.cells}" varStatus="cellcount">
                                <td class="crosstabdata">
                                    <div class="crosstabcelllabel">
                                        <c:out value="${cell.label}"/>
                                    </div>
                                    <div class="crosstabcelldata">
                                        <%-- add percent sign to value if in percent mode and has value --%>
                                        <c:set var="value" value="${cell.value}"/>
                                        <c:if test="${command.percent && cell.valueSet}">
                                            <c:set var="value" value="${cell.value} %"/>
                                        </c:if>

                                        <c:choose>
                                        <%-- only add link if drill down set and cell has proper value --%>
                                            <c:when test="${command.drillDownReportSet && cell.valueSet}">
                                                <zynap:artefactLink var="drilldown" url="${baseUrl}" tabName="activeTab" activeTab="results" >
                                                    <zynap:param name="<%= ParameterConstants.REPORT_ID %>" value="${command.drillDownReportId}"></zynap:param>
                                                    <zynap:param name="<%= ReportConstants.ORIGINAL_POPULATION_ID %>" value="${command.populationIdsString}"></zynap:param>                                                                                                        
                                                    <zynap:param name="<%= ReportConstants.VERTICAL_ATTR %>" value="${command.verticalColumnAttribute}"></zynap:param>
                                                    <zynap:param name="<%= ReportConstants.VERTICAL_ATTR_VALUE %>" value="${row.heading.id}"></zynap:param>
                                                    <zynap:param name="<%= ReportConstants.VERTICAL_ATTR_LABEL %>" value="${command.verticalHeader}  =  ${row.heading.label}"></zynap:param>

                                                    <zynap:param name="<%= ReportConstants.HORIZONTAL_ATTR %>" value="${command.horizontalColumnAttribute}"></zynap:param>
                                                    <zynap:param name="<%= ReportConstants.HORIZONTAL_ATTR_VALUE %>" value="${command.horizontalHeadings[cellcount.index].id}"></zynap:param>
                                                    <zynap:param name="<%= ReportConstants.HORIZONTAL_ATTR_LABEL %>" value="${command.horizontalHeader} = ${command.horizontalHeadings[cellcount.index].label}"></zynap:param>
                                                </zynap:artefactLink>
                                                <a href="<c:out value="${drilldown}"/>" >
                                                    <c:out value="${value}"/>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${value}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<zynap:form method="post" name="runColForm">
    <input type="hidden" name="_target2" value="2"/>
    <input id="ct_colId2" type="hidden" name="<%= RunCrossTabController.CROSS_TAB_COLUMN_ID %>"  value=""/>
    <input id="ct_colIndex" type="hidden" name="<%= RunCrossTabController.COLUMN_INDEX %>"  value="-1"/>
</zynap:form>

<zynap:form method="post" name="runRowForm">
    <input type="hidden" name="_target2" value="2"/>
    <input id="ct_rowId2" type="hidden" name="<%= RunCrossTabController.CROSS_TAB_ROW_ID %>"  value=""/>
    <input id="ct_rowIndex" type="hidden" name="<%= RunCrossTabController.ROW_INDEX %>"  value="-1"/>
</zynap:form>

<zynap:form method="post" name="runBarChartHForm">
    <input type="hidden" name="_target4" value="4"/>
    <input type="hidden" name="axis" value="horizontal"/>
</zynap:form>

<zynap:form method="post" name="runBarChartVForm">
    <input type="hidden" name="_target4" value="4"/>
    <input type="hidden" name="axis" value="vertical"/>
</zynap:form>
