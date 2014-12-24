 <%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_next" method="post">
        <table class="infotable" cellspacing="0">
			<tr>
				<td class="infolabel" width="10%"><fmt:message key="securitydomain.label"/>&nbsp;:&nbsp;*</td>
				<spring:bind path="command.label">
					<td class="infodata">
                        <input type="text" maxlength="100" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
						<%@include file="../../includes/error_message.jsp" %>
					</td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infolabel"><fmt:message key="securitydomain.comments"/>&nbsp;:&nbsp;</td>
				<spring:bind path="command.comments">
					<td class="infodata">
						<textarea name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textarea>
						<%@include file="../../includes/error_message.jsp" %>
					</td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infolabel"><fmt:message key="securitydomain.active"/>&nbsp;:&nbsp;</td>
				<spring:bind path="command.active">
					<td class="infodata">
                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.active}">checked</c:if>/>
						<%@include file="../../includes/error_message.jsp" %>
					</td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infolabel"><fmt:message key="securitydomain.exclusive"/>&nbsp;:&nbsp;</td>
				<spring:bind path="command.exclusive">
					<td class="infodata">
                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.exclusive}">checked</c:if>/>
						<%@include file="../../includes/error_message.jsp" %>
					</td>
				</spring:bind>
			</tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_target1" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>