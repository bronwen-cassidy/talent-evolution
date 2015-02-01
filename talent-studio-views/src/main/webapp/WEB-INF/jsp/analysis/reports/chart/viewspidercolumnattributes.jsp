<tr>
    <td class="infodata" colspan="2">
        <table id="reportcolumnvalues" class="infotable" cellspacing="0">
            <thead>
                <tr>
                    <th><fmt:message key="column.label"/></th>
                    <th><fmt:message key="attribute"/></th>
                    <th><fmt:message key="column.expected.value"/></th>
                    <th><fmt:message key="column.group"/></th>
                    <th><fmt:message key="column.colour"/></th>
                </tr>
            </thead>
            <c:forEach var="col" items="${columns}">
                <tr>
                    <td class="infodata" width="25%"><c:out value="${col.label}"/></td>
                    <td class="infodata" width="25%"><c:out value="${col.attributeLabel}"/></td>
                    <td class="infodata" width="25%"><c:out value="${col.expectedValue}"/></td>
                    <td class="infodata" width="25%"><c:out value="${col.columnLabel}"/></td>
                    <td class="infodata" width="25%"><span style="background-color:<c:out value="#"/><c:out value="${col.displayColour}"/>;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
                </tr>
            </c:forEach>
        </table>
    </td>
</tr>