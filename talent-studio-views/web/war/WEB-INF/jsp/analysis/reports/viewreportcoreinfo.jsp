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
<tr>
    <td class="infolabel"><fmt:message key="preferred.population"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <c:out value="${command.report.defaultPopulation.label}"/>
    </td>
</tr>

