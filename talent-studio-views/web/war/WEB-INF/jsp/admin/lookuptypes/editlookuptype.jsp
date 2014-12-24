<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="Edit Selection Type">
	<zynap:form method="post" name="edit" encType="multipart/form-data">
		<table class="infotable" cellspacing="0">
			<tr>
				<td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;*</td>
				<spring:bind path="command.label">
					<td class="infodata"><input type="text" maxlength="80" class="input_text" name="label" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
				</spring:bind>
			</tr>
			<tr>
				<td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;*</td>
				<spring:bind path="command.description">
				    <td class="infodata">
					<textarea tabindex="0" name="description" rows="4"><c:out value="${status.value}"/></textarea>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
				</spring:bind>
			</tr>
            <!-- Only user-defined lookup types can be disabled -->
            <c:choose>
                <c:when test="${command.userDefined}">
                    <tr>
                        <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                        <spring:bind path="command.active">
                        <td class="infodata">
                            <input type="checkbox" class="input_checkbox" name="active" <c:if test="${command.active}">checked</c:if>/>
                            <%@ include file="../../includes/error_message.jsp" %>
                        </td>
                        </spring:bind>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                            <input type="checkbox" class="input_checkbox" name="active" disabled <c:if test="${command.active}">checked</c:if>/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
			<tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
					<input class="inlinebutton" type="submit" name="edit" value="<fmt:message key="save"/>"/>
				</td>
			</tr>
		</table>
	</zynap:form>
</zynap:infobox>

<zynap:form method="get" name="_cancel" action="/admin/listlookupvalues.htm">
    <input type="hidden" name="typeId" value="<c:out value="${command.typeId}"/>"/>
</zynap:form>

