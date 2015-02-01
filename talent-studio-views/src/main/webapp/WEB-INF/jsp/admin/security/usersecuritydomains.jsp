<!-- security domains for user -->
<tr>
    <td class="infoheading" colspan="2">
        <fmt:message key="user.security.domains"/>
    </td>
</tr>

<tr>
    <td class="infolabel">
        <fmt:message key="security.domains"/>&nbsp;:&nbsp;
    </td>
    <td class="infodata">
        <c:choose>
            <c:when test="${! empty securityDomains}">
                <c:forEach items="${securityDomains}" var="securityDomain" varStatus="status">
                    <c:if test="${securityDomain.active}">
                        <c:out value="${securityDomain.label}"/><br/>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <fmt:message key="no.security.domains"/>
            </c:otherwise>
        </c:choose>
    </td>
</tr>