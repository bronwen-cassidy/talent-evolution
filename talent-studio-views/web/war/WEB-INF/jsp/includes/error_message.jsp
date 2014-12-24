<c:if test="${not empty status.errorMessage}">
    <div class="error">
        <c:out value="${status.errorMessage}"/>
    </div>
</c:if>