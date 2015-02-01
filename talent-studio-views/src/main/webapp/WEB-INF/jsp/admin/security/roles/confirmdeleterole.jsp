<%@ include file="../../../includes/include.jsp"%>

<zynap:infobox title="${title}" >
	<div class="infomessage">
		<fmt:message key="role.delete.confirm"/>&nbsp;<c:out value="${model.label}"/>?
	</div>

	<zynap:form method="post" action="/admin/viewrole.htm" name="_cancel">
		<input type="hidden" name="<%=ParameterConstants.ROLE_ID%>" value="<c:out value="${model.role_id}"/>"/>
		<input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
	</zynap:form>

	<zynap:form method="post" action="/admin/deleterole.htm" name="_confirm">
		<input type="hidden" name="<%=ParameterConstants.ROLE_ID%>" value="<c:out value="${model.role_id}"/>"/>
		<input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
		<input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="_confirm" onclick="javascript:document.forms._confirm.submit();"/>
	</zynap:form>
</zynap:infobox>