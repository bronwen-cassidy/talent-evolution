<%-- include used for adding user to subject or editing subject user details --%>

<%@ include file="../../includes/include.jsp" %>

<tr>
    <td class="infoheading" colspan="2">
        <fmt:message key="login.details"/>
    </td>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="admin.add.user.username"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.userWrapper.loginInfo.username">
        <td class="infodata"><input type="text" maxlength="200" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>

<c:if test="${add}">
    <tr>
        <td class="infolabel"><fmt:message key="admin.add.user.password"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.userWrapper.loginInfo.password">
            <td class="infodata"><input type="password" maxlength="50" class="input_password" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                <%@include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>
    <!-- repeat the password -->
    <tr>
        <td class="infolabel"><fmt:message key="repeat.new.password"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.userWrapper.loginInfo.repeatedPassword">
            <td class="infodata">
                <input type="password" maxlength="50" class="input_password" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                <%@include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>
</c:if>

<!-- is Active -->
<tr>
    <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.userWrapper.active">
        <td class="infodata"><input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.userWrapper.active}">checked="checked"</c:if>/>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>
<%-- home page group --%>
<tr>
    <td class="infolabel"><fmt:message key="assign.home.page.group"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <spring:bind path="command.userWrapper.groupId">
            <select name="<c:out value="${status.expression}"/>">
                <option value="" <c:if test="${command.userWrapper.groupId == null}">selected="true"</c:if>>&nbsp;</option>
                <c:forEach var="group" items="${command.userWrapper.groups}">
                    <option value="<c:out value="${group.id}"/>" <c:if test="${group.id == command.userWrapper.groupId}">selected="true" </c:if>><c:out value="${group.label}"/></option>
                </c:forEach>
            </select>
            <%@include file="../../includes/error_message.jsp" %>
        </spring:bind>
    </td>
</tr>
<!-- assign roles -->
<tr>
    <td class="infoheading" colspan="2">
        <fmt:message key="admin.user.roles"/>
    </td>
</tr>
<tr>
    <td class="infolabel">
        <fmt:message key="access.role"/>&nbsp;:&nbsp;
    </td>
    <td class="infodata">
        <c:import url="roles/assignroles.jsp"/>
    </td>
</tr>