<%@ include file="../../includes/include.jsp" %>

<fmt:message key="add.lookupvalue" var="msg"/>
<zynap:infobox title="${msg}">

    <div class="infomessage">
        <fmt:message key="add.lookupvalue.for">
            <fmt:param value="${command.lookupType.label}"/>
        </fmt:message>
    </div>

    <zynap:form method="post" name="_add" encType="multipart/form-data">
        <table class="infotable" cellspacing="0">
            <!-- Short Description  -->
            <tr>
                <td class="infolabel"><fmt:message key="lookupvalue.label"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata"><input type="text" maxlength="100" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <!-- Description  -->
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.description">
                    <td class="infodata"><textarea name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textarea>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <!-- Sort order -->
            <tr>
                <td class="infolabel"><fmt:message key="sort.order"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.sortOrder">
                    <td class="infodata">
                        <input type="text" maxlength="7" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <!-- Active -->
            <tr>
                <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.active">
                    <td class="infodata"><input type="checkbox" class="input_checkbox" name="active" <c:if test="${command.active}">checked</c:if>/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
			        <input class="inlinebutton" type="button" value="<fmt:message key="cancel"/>" name="_cancel" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_add" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel" action="/admin/listlookupvalues.htm">
    <input type="hidden" name="_cancel" value="_cancel"/>
    <input type="hidden" name="typeId" value="<c:out value="${command.typeId}"/>"/>
</zynap:form>

