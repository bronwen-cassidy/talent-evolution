
    <tr>
        <td class="infolabel"><fmt:message key="da.minlength"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.minSize">
            <td class="infodata">
                <input type="text" maxlength="20" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>

    <tr>
        <td class="infolabel"><fmt:message key="da.maxlength"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.maxSize">
            <td class="infodata">
                <input type="text" maxlength="20" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>    

