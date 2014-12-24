<tr>
    <td class="infolabel"><fmt:message key="report.label"/>&nbsp;:&nbsp;</td>
    <td class="infodata"><c:out value="${command.report.label}"/></td>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="report.type"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <fmt:message key="analysis.population.type.simple${command.report.defaultPopulation.type}"/>
    </td>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="report.description"/>&nbsp;:&nbsp;</td>
    <td class="infodata"><zynap:desc><c:out value="${command.report.description}"/></zynap:desc></td>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="analysis.scope"/>&nbsp;:&nbsp;</td>
    <td class="infodata"><fmt:message key="scope.${command.report.accessType}"/></td>
</tr>
<c:if test="${command.report.tabularReport || command.report.chartReport}">
    <tr>
        <td class="infolabel"><fmt:message key="display.last.item"/>&nbsp;:&nbsp;</td>
        <td class="infodata"><fmt:message key="${command.report.lastLineItem}"/></td>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="supports.personal"/>&nbsp;:&nbsp;</td>
        <td class="infodata"><fmt:message key="${command.report.personal}"/></td>
    </tr>
</c:if>
<c:if test="${command.report.chartReport}">
    <tr>
        <td class="infolabel"><fmt:message key="preferred.drilldown.report"/>&nbsp;:&nbsp;</td>
        <td class="infodata"><c:out value="${command.report.drillDownReport.label}"/></td>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="chart.type"/>&nbsp;:&nbsp;</td>
        <td class="infodata"><fmt:message key="${command.report.chartType}"/></td>
    </tr>
</c:if>

<tr>
    <td class="infolabel">
        <fmt:message key="assigned.arenas"/>&nbsp;:&nbsp;
    </td>
    <td class="infodata">
        <c:forEach var="reparena" items="${command.assignedArenas}">
            <div><c:out value="${reparena.label}"/></div>
        </c:forEach>
    </td>
</tr>
<tr>
    <td class="infolabel">
        <fmt:message key="assigned.groups"/>&nbsp;:&nbsp;
    </td>
    <td class="infodata">
        <c:forEach var="assignedGroup" items="${command.assignedGroups}">
            <div><c:out value="${assignedGroup.label}"/></div>
        </c:forEach>
    </td>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="preferred.population"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <c:out value="${command.report.defaultPopulation.label}"/>
    </td>
</tr>
