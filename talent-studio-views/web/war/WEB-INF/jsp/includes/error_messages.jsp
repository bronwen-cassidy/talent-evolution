<c:if test="${!empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <c:out value="${error}"/><br/>
        </c:forEach>
    </div>
</c:if>