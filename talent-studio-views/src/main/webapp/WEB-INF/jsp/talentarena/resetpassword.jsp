<%@ include file="../includes/include.jsp" %>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="<%=ParameterConstants.USER_ID%>" value="<c:out value="${command.userId}"/>"/>
    <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="_cancel"/>
</zynap:form>

<zynap:form name="resetpwdform" method="post">

    <input type="hidden" name="<%=ParameterConstants.USER_ID%>" value="<c:out value="${command.userId}"/>"/>

    <fmt:message key="resetpwd.title" var="msg"/>
    <zynap:infobox title="${msg}">

        <table class="infotable" cellspacing="0">
			<tr>
				<td class="infolabel"><fmt:message key="old.password"/>&nbsp;:&nbsp;*</td>
				<spring:bind path="command.oldPassword">
				<td class="infodata">
					<input type="password" maxlength="50" class="input_password" name="oldPassword" value="<c:out value="${status.value}"/>"/>
                    <%@ include file="../includes/error_message.jsp" %>
                </td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infolabel"><fmt:message key="new.password"/>&nbsp;:&nbsp;*</td>
				<spring:bind path="command.newPassword">
				<td class="infodata">
					<input type="password" maxlength="50" class="input_password" name="newPassword" value="<c:out value="${status.value}"/>"/>
                    <%@ include file="../includes/error_message.jsp" %>
                </td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infolabel"><fmt:message key="repeat.new.password"/>&nbsp;:&nbsp;*</td>
				<spring:bind path="command.newPasswordAgain">
				<td class="infodata">
					<input type="password" maxlength="50" class="input_password" name="newPasswordAgain" value="<c:out value="${status.value}"/>"/>
                    <%@ include file="../includes/error_message.jsp" %>
                </td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infobutton"></td>
				<td class="infobutton">
					<input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
					<input class="inlinebutton" type="submit" value="<fmt:message key="save"/>"/>
        		</td>
            </tr>
        </table>
    </zynap:infobox>
</zynap:form>
