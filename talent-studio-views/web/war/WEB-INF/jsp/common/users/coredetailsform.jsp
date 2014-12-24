<tr>
    <td class="infolabel"><fmt:message key="admin.add.user.title"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.title">
        <td class="infodata">
            <select name="<c:out value="${status.expression}"/>">
                <option value="" <c:if test="${command.title == null}">selected</c:if>><fmt:message key="please.select"/></option>
                <c:forEach var="tle" items="${titles}">
                    <option value="<c:out value="${tle.label}"/>"
                        <c:if test="${command.title == tle.label}">selected</c:if>><c:out value="${tle.label}"/>
                    </option>
                </c:forEach>
            </select>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>

<tr>
    <td class="infolabel"><fmt:message key="admin.add.user.firstname"/>&nbsp;:&nbsp;*</td>
    <spring:bind path="command.firstName">
        <td class="infodata"><input type="text" maxlength="150" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>

<tr>
    <td class="infolabel"><fmt:message key="admin.add.user.lastname"/>&nbsp;:&nbsp;*</td>
    <spring:bind path="command.secondName">
        <td class="infodata"><input type="text" maxlength="150" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>

<tr>
    <td class="infolabel"><fmt:message key="admin.user.prefname"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.prefGivenName">
        <td class="infodata"><input type="text" maxlength="150" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>

<tr>
    <td class="infolabel"><fmt:message key="contact.telephone"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.contactTelephone">
    <td class="infodata"><input type="text" maxlength="20" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>
    </td>
    </spring:bind>
</tr>

<tr>
    <td class="infolabel"><fmt:message key="contact.email"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.contactEmail">
    <td class="infodata"><input type="text" maxlength="200" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>
    </td>
    </spring:bind>
</tr>
