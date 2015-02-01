<%-- include used to select format for cross tab reports results - number or percentage --%>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper"%>
<tr>
    <td class="infolabel"><fmt:message key="report.display.as"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <spring:bind path="command.resultFormat">
            <select name="<c:out value="${status.expression}"/>">
                <option value="<%= RunCrossTabReportWrapper.DISCREET_VALUE %>" <c:if test="${command.resultFormat == 0}">selected</c:if>><fmt:message key="crosstab.format.discreet"/></option>
                <option value="<%= RunCrossTabReportWrapper.PERCENTAGE_VALUE %>" <c:if test="${command.resultFormat == 1}">selected</c:if>><fmt:message key="crosstab.format.percentage"/></option>

                <%-- if report has display report include option to select it --%>
                <c:if test="${command.hasDisplayReport}">
                    <option value="<%= RunCrossTabReportWrapper.ARTEFACTS_VALUE %>" <c:if test="${command.resultFormat == 2}">selected</c:if>><fmt:message key="crosstab.format.artefacts"/></option>
                </c:if>
            </select>
        </spring:bind>
    </td>
</tr>