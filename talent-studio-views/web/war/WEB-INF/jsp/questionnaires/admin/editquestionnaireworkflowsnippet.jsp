<tr>
    <td class="infolabel"><fmt:message key="generic.label"/>&nbsp;:&nbsp;*</td>
    <td class="infodata">
        <spring:bind path="command.label">
            <input type="text" class="input_text" name="<c:out value="${status.expression}"/>"
                   value="<c:out value="${status.value}"/>"
                    <c:if test="${published}">disabled="true"</c:if>
                    />
            <%@ include file="../../includes/error_message.jsp" %>
        </spring:bind>
    </td>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <spring:bind path="command.description">
            <textarea rows="4" cols="40" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
            <%@ include file="../../includes/error_message.jsp" %>
        </spring:bind>
    </td>
</tr>
<tr>
    <td class="infolabel" colspan="2"><fmt:message key="questionnaire.group.instructions"/></td>
</tr>
<tr>
    <td colspan="2">
        <table class="infotable" cellpadding="0" cellspacing="0">
            <tr>
                <td class="infodata">
                    <input type="button" class="inlinebutton" name="clearBtn" value="<fmt:message key="clear"/>"                           
                           onclick="clearElements(['newGroupFieldId', 'groupSelectElemId']); disableElement(['newGroupFieldId']); deselectElements(['radioOpt1', 'radioOpt2']);"/>
                </td>
                <td class="infodata">
                    <input type="radio" id="radioOpt1" class="input_radio" name="groupLabelOption" <c:if test="${command.groupId != null}">checked="true"</c:if> onclick="disableElement(['newGroupFieldId']);"/>
                    <spring:bind path="command.groupId">
                        <select name="<c:out value="${status.expression}"/>" id="groupSelectElemId" onchange="clearElements(['newGroupFieldId']); disableElement(['newGroupFieldId']); selectElements(['radioOpt1']);">
                            <option value="">&nbsp;</option>
                            <c:forEach var="group" items="${command.groups}">
                                <option value="<c:out value="${group.id}"/>" <c:if test="${command.groupId == group.id}">selected="true" </c:if>><c:out value="${group.label}"/></option>
                            </c:forEach>
                        </select>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
                <td class="infodata">
                    <input type="radio" id="radioOpt2" class="input_radio" name="groupLabelOption" onclick="enableElement(['newGroupFieldId']); clearElements(['groupSelectElemId']);"/>
                    <spring:bind path="command.groupLabel">                        
                        <input id="newGroupFieldId" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"
                        <c:if test="${command.groupId != null}">disabled="true"</c:if>/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
        </table>
    </td>
</tr>