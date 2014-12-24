<c:choose>
    <c:when test="${attrType == 'STRUCT' || attrType == 'SELECT' || attrType == 'RADIO'}">

        <c:forEach var="vals" items="${criteria.activeLookupValues}">
            <c:if test="${criteria.value == vals.id}">
                <c:out value="${vals.label}"/>
            </c:if>
        </c:forEach>
        
    </c:when>
    <c:when test="${attrType == 'LASTUPDATEDBY'}">

        <c:out value="${criteria.nodeLabel}"/>

    </c:when>
    <c:when test="${attrType == 'IMG'}">
        <img src="<c:url value="/image/daimageview.htm"><c:param name="i_id" value="${attrValueId}"/><c:param name="command.node.id" value="${nodeId}"/></c:url>" alt="<c:out value="${criteria.displayValue}"/>"/>
    </c:when>
    <c:when test="${attrType == 'MULTISELECT'}">

        <c:forEach var="vals" items="${criteria.activeLookupValues}">
            <c:forEach var="msVal" items="${attr.multiSelectValues}">
                <c:if test="${msVal.value == vals.id}">
                    <c:out value="${vals.label}"/><br/>
                </c:if>
            </c:forEach>
        </c:forEach>

    </c:when>
    <c:otherwise>
        <c:out value="${criteria.displayValue}"/>
    </c:otherwise>
</c:choose>