<%-- determine the correct label --%>
<fmt:message key="please.select" var="label"/>
<c:if test="${command.attributeSet}">
    <c:set var="label" value="${command.attributeLabel}"/>
</c:if>

<span style="white-space: nowrap;"><input id="<c:out value="${status.expression}"/>_label" type="text" class="input_text"
    value="<c:out value="${label}"/>"
        name="<c:out value="${status.expression}_label"/>"
        readonly="true"
/><input type="button"
        class="partnerbutton"
        value="..." id="navOUPopup" <c:if test="${!command.pickerEnabled}">disabled</c:if>
        onclick="<c:out value="${btnAction}"/>"/></span>
<input id="<c:out value="${status.expression}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />

<%@include file="../../includes/error_message.jsp" %>
