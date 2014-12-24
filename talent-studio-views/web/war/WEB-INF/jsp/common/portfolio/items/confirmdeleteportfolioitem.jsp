<%@ include file="../../../includes/include.jsp"%>

<zynap:infobox title="${title}" >
    <c:set var="portfolioItem" value="${model.item}"/>

    <div class="infomessage">
        <fmt:message key="confirm.delete.message"/>&nbsp;<c:out value="${portfolioItem.label}"/>?
        <fmt:message key="confirm.delete.item"/>
    </div>

	<%-- These urls are supplied in the model so this is correct --%>
    <zynap:form method="post" action="${model.cancelView}" name="_cancel">
        <input type="hidden" name="<%=ParameterConstants.ITEM_ID%>" value="<c:out value="${portfolioItem.id}"/>"/>
        <input type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM %>" value="<c:out value="${portfolioItem.node.id}"/>"/>
        <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.SAVE_COMMAND%>"/>        
        <input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
    </zynap:form>

    <zynap:form method="post" action="${model.confirmView}" name="confirm">
        <input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
        <input type="hidden" name="<%=ParameterConstants.ITEM_ID%>" value="<c:out value="${portfolioItem.id}"/>"/>
        <input type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM %>" value="<c:out value="${portfolioItem.node.id}"/>"/>
        <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="confirm" onclick="javascript:document.forms.confirm.submit();"/>
    </zynap:form>

</zynap:infobox>
