
<%-- select the default population to use with this report may not be null --%>
<tr>
	<td class="infolabel"><fmt:message key="preferred.population"/>&nbsp;:&nbsp;*</td>
    <spring:bind path="command.populationId">
    <td class="infodata">
        <select name="<c:out value="${status.expression}"/>">
            <option value="" <c:if test="${command.populationId == null}">selected</c:if>><fmt:message key="please.select"/></option>
            <c:forEach var="prefPop" items="${command.populations}">
                <option value="<c:out value="${prefPop.id}"/>" <c:if test="${command.populationId == prefPop.id}">selected</c:if>><c:out value="${prefPop.label}"/></option>
            </c:forEach>
        </select>
        <%@include file="../../includes/error_message.jsp" %>
    </td>
    </spring:bind>
</tr>