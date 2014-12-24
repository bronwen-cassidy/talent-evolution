<c:choose>
    <c:when test="${!rolesEmpty}">
        <c:forEach items="${userRoles}" var="role" varStatus="status" >
            <c:out value="${role.label}"/><br/>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <fmt:message key="access.no.roles"/>
    </c:otherwise>
</c:choose>
