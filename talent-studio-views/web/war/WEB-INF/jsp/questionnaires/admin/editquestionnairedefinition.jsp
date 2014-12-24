<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}" id="editDef">

    <zynap:form method="post" name="_upload">
        <table class="infotable" id="qdefedit">

            <spring:bind path="command.label">
                <tr>
                    <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;*</td>
                    <td class="infodata"><input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../includes/error_message.jsp" %>
                    </td>
                </tr>
            </spring:bind>
            <spring:bind path="command.description">
                <tr>
                    <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <textarea rows="4" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                        <%@include file="../../includes/error_message.jsp" %>
                    </td>
                </tr>
            </spring:bind>

            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input type="button" class="inlinebutton" name="cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input type="submit" class="inlinebutton" name="upload" value="<fmt:message key="save"/>"/>
                </td>
            </tr>

		</table>
	</zynap:form>

</zynap:infobox>

<zynap:form method="post" name="_cancel">
	<input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="<%=ParameterConstants.CANCEL_PARAMETER%>"/>
</zynap:form>