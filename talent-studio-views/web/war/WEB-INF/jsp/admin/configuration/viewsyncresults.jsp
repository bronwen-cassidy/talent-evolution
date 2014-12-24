<%@ include file="../../includes/include.jsp" %>

<c:choose>
    <c:when test="${empty model.subjects}">
        <div class="infomessage">
            <fmt:message key="sync.up.to.date"/>
        </div>
    </c:when>
    <c:otherwise>
        <div>
            <c:forEach var="s" items="${model.subjects}" varStatus="indexer">
                <c:if test="${indexer.index > 0}">
                    ,&nbsp;
                </c:if>
                <c:out value="${s.label}"/>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
