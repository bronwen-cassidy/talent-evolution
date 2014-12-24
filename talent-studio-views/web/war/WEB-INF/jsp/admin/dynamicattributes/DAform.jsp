<%@ include file="../../includes/include.jsp" %>
<%-- Label --%>
<tr>
    <td class="infolabel"><fmt:message key="da.general.label"/>&nbsp;:&nbsp;*</td>
    <spring:bind path="command.label">
        <td class="infodata">
            <input type="text" maxlength="100" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <%@ include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>

<%-- description --%>
<tr>
    <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;*</td>
    <spring:bind path="command.description">
        <td class="infodata">
            <textarea name="<c:out value="${status.expression}"/>" rows="4" cols="25"><c:out value="${status.value}"/></textArea>
            <%@ include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>

<%-- Type. Read-only --%>
<tr>
    <td class="infolabel"><fmt:message key="da.general.type"/>&nbsp;:&nbsp;</td>
    <td class="infodata"><fmt:message key="${command.type}"/></td>
</tr>

<%-- Active Flag --%>
<tr>
    <td class="infolabel"><fmt:message key="da.general.active"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.active">
        <td class="infodata">
            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.active}">checked="checked"</c:if>/>
            <%@ include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>

<c:if test="${!command.calculated}">
    <%-- Mandatory --%>
    <tr>
        <td class="infolabel"><fmt:message key="da.general.mandatory"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.mandatory">
            <td class="infodata">
                <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.mandatory}">checked="checked"</c:if>/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>

    <%-- Searchable --%>
    <tr>
        <td class="infolabel"><fmt:message key="da.general.searchable"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.searchable">
            <td class="infodata">
                <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.type == 'IMG'}">disabled="disabled"</c:if> <c:if test="${command.searchable}">checked="checked"</c:if>/>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>
</c:if>

<c:if test="${command.type == 'TEXT'}">
    <%@ include file="textDASnippet.jsp" %>
</c:if>
<c:if test="${command.type == 'TEXTAREA'}">
    <%@ include file="textareaDASnippet.jsp" %>
</c:if>
<c:if test="${command.type == 'NUMBER'}">
    <%@ include file="numberDASnippet.jsp" %>
</c:if>
<c:if test="${command.type == 'DOUBLE'}">
    <%@ include file="doubleDASnippet.jsp" %>
</c:if>
<c:if test="${command.type == 'TIME'}">
    <%@ include file="timeDASnippet.jsp" %>
</c:if>
<c:if test="${command.type == 'STRUCT' || command.type == 'MULTISELECT'}">

    <%-- If readOnly is true show name of selection type - editing not allowed --%>
    <c:choose>
        <c:when test="${readOnly}">
            <tr>
                <td class="infolabel"><fmt:message key="da.struct.refersto"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><c:out value="${command.refersToTypeLabel}"/></td>
            </tr>
        </c:when>
        <c:otherwise>
            <%@ include file="structDASnippet.jsp" %>
        </c:otherwise>
    </c:choose>

</c:if>
<c:if test="${command.type == 'DATE'}">
    <%@ include file="dateDASnippet.jsp" %>
</c:if>
<c:if test="${command.type == 'DATETIME'}">
    <%@ include file="dateTimeDASnippet.jsp" %>
</c:if>
