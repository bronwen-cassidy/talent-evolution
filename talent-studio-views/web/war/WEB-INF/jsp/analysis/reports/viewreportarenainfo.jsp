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


