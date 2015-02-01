<c:set var="columns" value="${command.columns}"/>

<c:set var="hasColumns" value="${columns != null && !empty columns}"/>
<c:if test="${hasColumns}">
    <tr>
        <td class="infolabel"><fmt:message key="report.metrics"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <table id="reportcolumninfo" class="infotable" cellspacing="0">
                <tr>
                    <th class="sorted"><fmt:message key="metric.label"/></th>
                </tr>
                <c:forEach var="column" items="${columns}" varStatus="indexer">
                    <tr>
                        <td class="infodata"><c:out value="${column.label}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>    
    <c:if test="${command.groupingColumn != null}">
        <tr>
            <td class="infolabel"><fmt:message key="group.by.attribute"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.groupingColumn.attributeLabel}"/></td>
        </tr>
    </c:if>
    <tr>
        <td class="infolabel"><fmt:message key="group.column"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <c:if test="${command.groupingColumn != null}">
                <c:out value="${command.groupingColumn.label}"/>
            </c:if>
        </td>
    </tr>
</c:if>
