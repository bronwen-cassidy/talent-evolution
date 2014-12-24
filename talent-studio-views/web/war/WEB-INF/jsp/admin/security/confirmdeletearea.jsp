<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <div class="infomessage">
        <fmt:message key="area.delete.confirm"/>&nbsp;<c:out value="${model.area.label}"/>?
    </div>

    <zynap:form method="post" action="/admin/viewarea.htm" name="_cancel">
        <input type="hidden" name="<%=ParameterConstants.AREA_ID%>" value="<c:out value="${model.area.id}"/>"/>
        <input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
    </zynap:form>

    <zynap:form method="post" action="/admin/deletearea.htm" name="_confirm">
        <input type="hidden" name="<%=ParameterConstants.AREA_ID%>" value="<c:out value="${model.area.id}"/>"/>
        <input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
        <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="_confirm" onclick="javascript:document.forms._confirm.submit();"/>
    </zynap:form>
</zynap:infobox>