<%@ include file="../../includes/include.jsp"%>

<zynap:infobox title="${title}" >

	<div class="infomessage">
		<fmt:message key="securitydomain.delete.confirm"/>&nbsp;<c:out value="${model.securityDomain.label}"/>?
	</div>

	<zynap:form method="post" action="/admin/viewsecuritydomain.htm" name="_cancel">
		<input type="hidden" name="<%=ParameterConstants.DOMAIN_ID%>" value="<c:out value="${model.securityDomain.id}"/>"/>
		<input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
	</zynap:form>

	<zynap:form method="post" action="/admin/deletesecuritydomain.htm" name="_confirm">
		<input type="hidden" name="<%=ParameterConstants.DOMAIN_ID%>" value="<c:out value="${model.securityDomain.id}"/>"/>
		<input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
		<input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="_confirm" onclick="javascript:document.forms._confirm.submit();"/>
	</zynap:form>
</zynap:infobox>