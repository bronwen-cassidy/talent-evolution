<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}" id="addDef">

    <zynap:form encType="multipart/form-data" method="post" name="_upload">
		<table class="infotable" cellspacing="0">

            <c:if test="${command.cloneable}">
                <tr>
                    <td class="infonarrative" colspan="2">
                        <fmt:message key="select.questionnaire.upload.option"/>
                    </td>
                </tr>
                <spring:bind path="command.selectedDefinitionId">
                    <tr>
                        <td class="infolabel"><fmt:message key="questionnaire.definition.clone"/>&nbsp;:&nbsp;*</td>
                        <td class="infodata">
                            <select name="<c:out value="${status.expression}"/>">
                                <option <c:if test="${command.selectedDefinitionId == null}">selected</c:if>></option>
                                <c:forEach var="def" items="${command.questionnaireDefinitions}">
                                    <option value="<c:out value="${def.id}"/>"><c:out value="${def.label}"/></option>
                                </c:forEach>
                            </select>
                            <%@include file="../../includes/error_message.jsp" %>
                        </td>
                    </tr>
                </spring:bind>
            </c:if>
            <spring:bind path="command.definitionBytes">
                <tr>
                    <td class="infolabel"><fmt:message key="questionnaire.definition.upload"/>&nbsp;:&nbsp;*</td>
                    <td class="infodata">
                        <input type="file" class="input_file" name="<c:out value="${status.expression}"/>"/>
                        <%@include file="../../includes/error_message.jsp" %>
                    </td>
                </tr>
            </spring:bind>
            <spring:bind path="command.label">
                <tr>
                    <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>&nbsp;<fmt:message key="replaces.qd.label"/>
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
                <td class="infobutton"></td>
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
