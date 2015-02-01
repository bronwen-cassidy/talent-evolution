<c:choose>
    <c:when test="${attrType == 'STRUCT' || attrType == 'MULTISELECT' || attrType == 'SELECT' || attrType == 'RADIO'}">
        <c:forEach var="vals" items="${criteria.activeLookupValues}">
            <c:if test="${criteria.refValue == vals.id}">
                <c:out value="${vals.label}"/>
            </c:if>
        </c:forEach>
    </c:when>
    <c:when test="${attrType == 'LASTUPDATEDBY'}">
        <c:out value="${criteria.nodeLabel}"/>
    </c:when>
    <c:otherwise>
        <c:out value="${criteria.displayValue}"/>
    </c:otherwise>
</c:choose>