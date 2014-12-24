<%@ include file="../../includes/include.jsp" %>
<tr>
    <td class="infoheading" colspan="2">
        <fmt:message key="security.permissions"/>
    </td>
</tr>
<tr>
    <td class="infolabel">
        <fmt:message key="access.role"/>&nbsp;:&nbsp;

    </td>
    <td class="infodata">
        <c:choose>
            <c:when test="${!rolesEmpty}">
                <c:set var="notSuperUser" value="${!model.superUser}"/>
                <c:forEach items="${userRoles}" var="role" varStatus="status" >
                    <%-- TS-551: Do not display home role as this role cannot be edited and by default is added to every user --%>
                    <c:set var="shouldHideAdminRole" value="${notSuperUser && role.adminRole}"/>
                    <c:if test= "${!role.homeRole && !shouldHideAdminRole}">
                        <c:out value="${role.label}"/><br/>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <fmt:message key="access.no.roles"/>
            </c:otherwise>
        </c:choose>
    </td>
</tr>