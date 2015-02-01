<%@ include file="../includes/include.jsp"%>

<fmt:message key="corporate.goals" var="msg"/>

<zynap:form method="post" name="objsetfrmid" encType="multipart/form-data">
    <zynap:infobox id="objformId" title="${msg}">

        <input type="hidden" id="cancelId" name="" value=""/>
        <input type="hidden" id="deleteIdx" name="deleteIndex" value="-1"/>
        <input type="hidden" id="targetId" name="-1" value="-1"/>

        <table class="infotable" cellspacing="0" id="objsetid">
            <tr>
                <td class="infolabel"><fmt:message key="quarter.year"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.objectiveSet.label">
                    <td class="infodata">
                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../includes/error_message.jsp"%>
                    </td>
                </spring:bind>
            </tr>            
            <tr>
                <td class="infolabel"><fmt:message key="expiry.date"/>&nbsp;:</td>
                <spring:bind path="command.objectiveSet.expiryDate">
                    <td class="infodata">
                        <span style="white-space: nowrap;"><input id="min4x" class="input_date" name="dateName" type="text" value="<c:out value="${command.expiryDate}"/>" readonly="true"/><input type="button" id="minDateSelect" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'min4x', 'min5x');"/></span>
                        <input id="min5x" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../includes/error_message.jsp"%>
                    </td>
                </spring:bind>
            </tr>                        
        </table>
    </zynap:infobox>

    <c:set var="command" value="${command}" scope="request"/>
    <c:import url="../common/objectives/objectivesform.jsp"/>

</zynap:form>



<zynap:popup id="calendarPopup">
    <%@ include file="../includes/calendar.jsp" %>
</zynap:popup>