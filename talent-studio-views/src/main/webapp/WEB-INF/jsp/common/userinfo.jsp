<%@ include file="../includes/include.jsp" %>

<div class="row userinfodata full-width">
    <div class="medium-3 column header-right"></div>
    <div class="medium-6 column">
        <c:if test="${userPrincipal != null}">
            <span class="userinfo" id="userinfo-id-1">
                <fmt:message key="logged.in"/>
                    <c:out value="${userPrincipal.firstName}"/>
                    <c:out value="${userPrincipal.lastName}"/>
            </span>
        </c:if>
    </div>
    <div class="medium-3 column">
        <c:if test="${userPrincipal != null}">
            <span class="infoctrl noprint">
                <a class="logout" href="#" onclick="confirmAction('<c:url value="/logout.htm"/>', '<spring:message code="confirm.cancel.logout" javaScriptEscape="true"/>');"><fmt:message key="logout"/></a>
            </span>
        </c:if>
    </div>
</div>