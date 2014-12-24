<%@ include file="includes/include.jsp" %>
<fmt:message key="changepwd.title" var="title" scope="request"/>
<script type="text/javascript">
<!--
function goBackToPrevUrl() {
	var frm = document.forms['changepwdform'];
	var url = frm.prevUrl.value;
	frm.action=url;
	frm.method='post';
	return true;
}
//-->
</script>

<zynap:form name="changepwdform" method="post" encType="multipart/form-data">

	<zynap:infobox title="${title}" id="nonav">
		<c:if test="${command.forceChange}">
			<div class="infomessage"><fmt:message key="force.changepwd.message"/></div>
		</c:if>

		<table class="infotable" cellspacing="0">
			<tr>
				<td class="infolabel"><fmt:message key="username"/>&nbsp;:&nbsp;</td>
				<td class="infodata">
					<c:out value="${command.username}"/>
				</td>
			</tr>
			<tr>
				<td class="infolabel"><fmt:message key="new.password"/>&nbsp;:&nbsp;</td>
				<spring:bind path="command.newPassword">
				<td class="infodata">
					<input type="password" maxlength="50" class="input_password" name="newPassword" value="<c:out value="${status.value}"/>"/>
                    <%@ include file="includes/error_message.jsp" %>
                </td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infolabel"><fmt:message key="repeat.new.password"/>&nbsp;:&nbsp;</td>
				<spring:bind path="command.newPasswordAgain">
				<td class="infodata">
					<input type="password" maxlength="50" class="input_password" name="newPasswordAgain" value="<c:out value="${status.value}"/>"/>
                    <%@ include file="includes/error_message.jsp" %>
                </td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infobutton"/>
				<td class="infobutton">
                    <input class="inlinebutton" type="button" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" id="_save" name="_save" value="<fmt:message key="save"/>"/>
				</td>
			</tr>
		</table>
	</zynap:infobox>
</zynap:form>

<zynap:form name="_cancel" method="post">
    <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="true"/>
</zynap:form>




