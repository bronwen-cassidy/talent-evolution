<%@ include file="../../includes/include.jsp"%>

<zynap:infobox title="${title}" >

    <div class="infomessage">
        <fmt:message key="confirm.delete.chart.settings"/>&nbsp;<fmt:message key="confirm.message"/>
    </div>

    <zynap:form method="post" action="" name="_cancel">
        <input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
    </zynap:form>

    <zynap:form method="post" action="" name="_confirm">
        <input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
        <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="_confirm" onclick="javascript:document.forms._confirm.submit();"/>
    </zynap:form>

</zynap:infobox>
