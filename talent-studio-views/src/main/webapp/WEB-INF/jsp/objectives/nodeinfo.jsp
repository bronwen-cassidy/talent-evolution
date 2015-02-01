<table class="infotable" cellpadding="0" cellspacing="0">
    <tr>
        <td class="infolabel"><fmt:message key="generic.name"/></td>
        <td class="infodata"><c:out value="${subject.label}"/> </td>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="organisation.unit"/></td>
        <td class="infodata">
            <c:forEach var="organisationUnit" items="${organisationUnits}">
                <c:out value="${organisationUnit.label}"/><br/>
            </c:forEach>
        </td>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="job.title"/></td>
        <td class="infodata">
            <c:forEach var="position" items="${positions}">
                <c:out value="${position.label}"/>
            </c:forEach>
        </td>
    </tr>
</table>