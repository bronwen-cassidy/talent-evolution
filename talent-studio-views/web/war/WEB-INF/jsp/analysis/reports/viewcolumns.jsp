<c:set var="hasColumns" value="${columns != null && !empty columns}"/>
<c:if test="${hasColumns}">
    <tr>
        <td class="infolabel"><c:out value="${columnHeader}"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <table id="reportcolumninfo" class="infotable" cellspacing="0">
                <tr>
                    <th class="sorted" width="25%"><fmt:message key="attribute"/></th>
                    <th class="sorted"><fmt:message key="column.label"/></th>
                    <c:if test="${showLegend}">
                        <th class="sorted"><fmt:message key="color.legend"/></th>
                    </c:if>
                    <c:if test="${showGroups}">
                        <th class="sorted"><fmt:message key="column.group"/></th>
                    </c:if>
                </tr>
                <c:forEach var="column" items="${columns}" varStatus="indexer">
                    <tr>
                        <td class="infodata" width="25%"><c:out value="${column.attributeLabel}"/></td>
                        <td class="infodata"><c:out value="${column.label}"/></td>
                        <c:if test="${showLegend}">
                            <td class="infodata">
                                <c:choose>
                                    <c:when test="${column.useColours}">
                                        <a id="amr_<c:out value="${column.id}"/>" href="#" onclick="javascript:submitLegendForm('amr_', '<c:out value="${column.id}"/>', 'actTbElId', 'details');"><img src="<c:url value="/images/checked.gif"/>" alt="is coloured cell"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="<c:url value="/images/unchecked.gif"/>" alt="no legend"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${showGroups}">
                                <td class="infodata">
                                    <c:choose>
                                        <c:when test="${column.grouped}">
                                            <img src="<c:url value="/images/checked.gif"/>" alt="column is grouped"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="<c:url value="/images/unchecked.gif"/>" alt="no grouping"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
</c:if>