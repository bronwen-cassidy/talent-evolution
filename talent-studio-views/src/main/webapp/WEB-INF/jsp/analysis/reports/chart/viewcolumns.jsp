<tr>
    <td class="heading" colspan="2"><c:out value="${columnHeader}"/></td>
</tr>
<tr>
    <td class="infodata" colspan="2">
        <table cellpadding="0" cellspacing="0" class="infotable">
            <tr>
                <th width="33%"><fmt:message key="column.label"/></th>
                <th width="66%"><fmt:message key="attributes"/></th>
            </tr>
            <c:forEach var="column" items="${columns}" varStatus="indexer">
                <tr>                    
                    <td class="infodata"><c:out value="${column.label}"/></td>
                    <td class="infodata"><c:out value="${column.attributeLabel}"/></td>
                </tr>
            </c:forEach>
        </table>
    </td>
</tr>
<tr>
    <td class="heading" colspan="2"><fmt:message key="chart.columns"/></td>
</tr>
<tr>
    <td class="infodata" colspan="2">
        <table id="reportcolumnvalues" class="infotable" cellspacing="0">
            <tr>
                <th width="25%"><fmt:message key="column.label"/></th>
                <th width="25%"><fmt:message key="column.value"/></th>
                <%--<th width="25%"><fmt:message key="column.expected.value"/></th>--%>
                <th width="25%"><fmt:message key="column.colour"/></th>
            </tr>
            <c:forEach var="col" items="${command.reportColumns}">
                <tr>
                    <td class="infodata" width="25%"><c:out value="${col.label}"/></td>
                    <td class="infodata" width="25%">
                        <c:choose>
                            <c:when test="${col.value == '_NULL_'}"><strong><i><fmt:message key="chart.null.answer"/></i></strong></c:when>
                            <c:otherwise><c:out value="${col.value}"/></c:otherwise>
                        </c:choose>
                    </td>
                    <%--<td class="infodata" width="25%"><c:out value="${col.expectedValue}"/></td>--%>
                    <td class="infodata" width="25%"><span style="background-color:<c:out value="#"/><c:out value="${col.displayColour}"/>;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
                </tr>
            </c:forEach>
        </table>
    </td>
</tr>