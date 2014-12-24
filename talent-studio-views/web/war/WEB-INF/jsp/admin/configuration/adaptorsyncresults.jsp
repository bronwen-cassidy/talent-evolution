<%@ include file="../../includes/include.jsp" %>

<c:choose>
    <c:when test="${model.noResults}">
        <c:choose>
            <c:when test="${model.errorMsg}">
                <div class="error">
                    <c:out value="${model.errorMsg}"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="infomessage">
                    <fmt:message key="sync.up.to.date"/>
                </div>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <div>
            <fmt:message key="modified.info"/>:&nbsp;  <br/>
            <c:forEach var="s" items="${model.modified}" varStatus="indexer">
                <c:if test="${indexer.index > 0}">
                    ,&nbsp;
                </c:if>
                <c:out value="${s.label}"/>
            </c:forEach>
            <br/>
            <fmt:message key="added.info"/>:&nbsp; <br/>
            <c:forEach var="s" items="${model.added}" varStatus="indexer">
                <c:if test="${indexer.index > 0}">
                    ,&nbsp;
                </c:if>
                <c:out value="${s.label}"/>
            </c:forEach>
            <br/>
            <c:if test="${not empty model.pending}">
                <fmt:message key="pending.info"/>:&nbsp; <br/>
                <c:forEach var="s" items="${model.pending}" varStatus="indexer">
                    <c:if test="${indexer.index > 0}">
                        ,&nbsp;
                    </c:if>
                    <c:out value="${s.label}"/>
                </c:forEach>
                <br/>
            </c:if>
            <fmt:message key="errored.info"/>:&nbsp; <br/>
            <c:forEach var="s" items="${model.errored}" varStatus="indexer">
                <c:if test="${indexer.index > 0}">
                    ,&nbsp;
                </c:if>
                <c:out value="${s.label}"/>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
