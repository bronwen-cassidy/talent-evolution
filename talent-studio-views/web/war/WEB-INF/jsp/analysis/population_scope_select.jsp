<%@ page import="AccessType"%>
<select name="<c:out value="${status.expression}"/>">
    <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
    <option value="<%=AccessType.PUBLIC_ACCESS%>" <c:if test="${status.value == 'Public'}"> selected</c:if>><fmt:message key="scope.Public"/></option>
    <option value="<%=AccessType.PRIVATE_ACCESS%>" <c:if test="${status.value == 'Private'}"> selected</c:if>><fmt:message key="scope.Private"/></option>
</select>