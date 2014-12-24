<%@ include file="../../includes/include.jsp" %>

<fmt:message key="edit.dynamicAttribute" var="msg"/>
<zynap:infobox title="${msg}">

    <spring:bind path="command">
        <%@include file="../../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form name="edit" method="post" encType="multipart/form-data">
        <table id="attribs" class="infotable" cellspacing="0">

            <c:set var="readOnly" value="true" scope="request"/>
            <c:import url="../admin/dynamicattributes/DAform.jsp"/>

            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="edit" value="<fmt:message key="save"/>"/>
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
</zynap:form>
