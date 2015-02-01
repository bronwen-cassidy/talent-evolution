<%@ include file="../../includes/include.jsp" %>

<fmt:message key="da.wizard.1" var="msg"/>
<zynap:infobox title="${msg}">

    <spring:bind path="command">
        <%@include file="../../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form name="add" method="post" encType="multipart/form-data">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table id="attribs" class="infotable" cellspacing="0">

            <c:set var="readOnly" value="false" scope="request"/>
            <c:import url="../admin/dynamicattributes/DAform.jsp"/>

            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('add', 'pgTarget', '0', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>

    </zynap:form>

</zynap:infobox>

<c:if test="${command.type == 'DATETIME' || command.type == 'DATE'}">
    <zynap:popup id="calendarPopup">
        <%@ include file="../../includes/calendar.jsp" %>
    </zynap:popup>
</c:if>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="true"/>
    <input type="hidden" name="<%=ParameterConstants.ARTEFACT_TYPE%>" value="<c:out value="${command.artefactType}"/>"/>
</zynap:form>
