<c:choose>
    <c:when test="${command.calculated}">
        <%@include file="dateCalcSnippet.jsp"%>
    </c:when>
    
    <c:otherwise>
        <%-- when a field is calculated there is no range available --%>
        <tr>
            <td class="infolabel"><fmt:message key="da.earliest"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <spring:bind path="command.minSize">
                    <span style="white-space: nowrap;"><input id="min4x" class="input_date" name="minDisplayDate" type="text" value="<c:out value="${command.minDate}"/>" readonly="true"/><input type="button" id="minDateSelect" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'min4x', 'min5x', '', true);"/></span>
                    <input id="min5x" name="minDate" type="hidden" value="<c:out value="${status.value}"/>"/>
                    <%@include file="../../includes/error_message.jsp" %>
                </spring:bind>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="da.latest"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <spring:bind path="command.maxSize">
                    <span style="white-space: nowrap;"><input id="fld4x" class="input_date" name="maxDisplayDate" type="text" value="<c:out value="${command.maxDate}"/>" readonly="true"/><input type="button" id="maxDateSelect" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'fld4x', 'fld5x', '', true);"/></span>
                    <input id="fld5x" name="maxDate" type="hidden" value="<c:out value="${status.value}"/>"/>
                    <%@include file="../../includes/error_message.jsp" %>
                </spring:bind>
            </td>
        </tr>
    </c:otherwise>
</c:choose>

