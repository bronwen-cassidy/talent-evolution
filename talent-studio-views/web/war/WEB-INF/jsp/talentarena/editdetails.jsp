<%@ include file="../includes/include.jsp" %>

<fmt:message key="date.format" var="datePattern"/>

<zynap:infobox title="${title}">
    <zynap:form name="_edit" method="post" encType="multipart/form-data">
        <table cellspacing="0" class="infotable">

            <%-- core details --%>
            <%@ include file="../common/users/coredetailsform.jsp"%>

				 <tr>
					  <spring:bind path="command.dateOfBirth">

                            <td class="infolabel"><fmt:message key="admin.user.dob"/>&nbsp;:&nbsp;</td>
							<td class="infodata">
								 <span style="white-space: nowrap;"><input id="dob3" name="<c:out value="${status.expression}"/>" type="text" class="input_date" value="<fmt:formatDate value="${command.dateOfBirth}" pattern="${datePattern}"/>" readonly="true"/><input type="button" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'dob3', 'dob4');"/></span>
								 <input id="dob4" name="<c:out value="${status.expression}"/>" type="hidden" value="<c:out value="${status.value}"/>"/>
								 <%@include file="../includes/error_message.jsp" %>
							</td>
					  </spring:bind>
				 </tr>

				 <%-- include extended attributes --%>
                <c:set var="currentFormName" value="_edit"/>  
			   <%@ include file="../common/attributes/artefactDASnippet.jsp" %>

            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_edit" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel" action="viewmydetails.htm"/>

<zynap:popup id="calendarPopup">
    <%@ include file="../includes/calendar.jsp" %>
</zynap:popup>