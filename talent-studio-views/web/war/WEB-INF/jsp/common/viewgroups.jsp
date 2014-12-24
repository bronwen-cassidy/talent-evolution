<c:choose>
    <c:when test="${!groupsEmpty}">
        <c:forEach items="${groups}" var="group" varStatus="status" >
            <c:out value="${group.label}"/>&nbsp;&nbsp;&nbsp;
            <c:if test="${status.index % 3 == 0}"><br/></c:if>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <fmt:message key="no.assigned.groups"/>
    </c:otherwise>
</c:choose>

