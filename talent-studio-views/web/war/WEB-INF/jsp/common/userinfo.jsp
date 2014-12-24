<%@ include file="../includes/include.jsp" %>

<tr>
    <td class="userinfodata">
        <c:if test="${userPrincipal != null}">
            <span class="userinfo" id="userinfo-id-1">
                <fmt:message key="logged.in"/>
                    <c:out value="${userPrincipal.firstName}"/>
                    <c:out value="${userPrincipal.lastName}"/>
            </span>
        </c:if>
        <c:if test="${not sessionScope.permitsDone}">
            <div id="permitsDoneDV" class="loading" style="display:block;"><fmt:message key="permits.loading"/></div>
        </c:if>
        <c:if test="${userPrincipal != null}">
            <span class="infoctrl noprint">
                <a class="logout" href="#" onclick="confirmAction('<c:url value="/logout.htm"/>', '<spring:message code="confirm.cancel.logout" javaScriptEscape="true"/>');"><fmt:message key="logout"/></a>
            </span>
        </c:if>
    </td>
</tr>
