<%@ include file="../includes/include.jsp" %>
    <tr>
        <td class="userinfodata">
            <c:if test="${userPrincipal != null}">
			    <span class="userinfo" id="userinfo-id-1">
	            	<fmt:message key="logged.in"/>
						<c:out value="${userPrincipal.firstName}"/>
						<c:out value="${userPrincipal.lastName}" />
				</span>
            </c:if>
        </td>
    </tr>