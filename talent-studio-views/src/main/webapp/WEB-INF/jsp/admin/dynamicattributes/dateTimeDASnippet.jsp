<tr>
    <td class="infolabel"><fmt:message key="da.earliest"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.minSize">
        <td class="infodata">
            <span style="white-space: nowrap;"><input id="min4x" class="input_date" type="text" value="<c:out value="${command.minDateTimeDisplayValue}"/>" readonly="true"/><input type="button" id="minDateSelect" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'min4x', 'min5x', '', true);"/></span>
            <input id="min5x" name="minDate" type="hidden" value="<c:out value="${command.minDateTime[0]}"/>"/>

            <select id="minTimeHour" name="minHour" onchange="toggleTime('minTimeHour', 'minTimeMin');">
                <option value="" <c:if test="${empty command.minDateTime[1]}">selected</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="hour" begin="0" end="23">
                    <option value="<fmt:formatNumber value="${hour}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty command.minDateTime[1] && command.minDateTime[1] == hour}">selected</c:if>>
                        <fmt:formatNumber value="${hour}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>&nbsp;:&nbsp;<select id="minTimeMin" name="minMinute" onchange="toggleTime('minTimeMin', 'minTimeHour');">
                <option value="" <c:if test="${empty command.minDateTime[2]}">selected</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="minute" begin="0" end="59">
                    <option value="<fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty command.minDateTime[2] && command.minDateTime[2] == minute}">selected</c:if>>
                        <fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>
<tr>
    <td class="infolabel"><fmt:message key="da.latest"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.maxSize">
        <td class="infodata">
            <span style="white-space: nowrap;"><input id="fld4x" class="input_date" type="text" value="<c:out value="${command.maxDateTimeDisplayValue}"/>" readonly="true"/><input type="button" id="maxDateSelect" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'fld4x', 'fld5x', '', true);"/></span>
            <input id="fld5x" name="maxDate" type="hidden" value="<c:out value="${command.maxDateTime[0]}"/>"/>            
            <select id="maxTimeHour" name="maxHour" onchange="toggleTime('maxTimeHour', 'maxTimeMin');">
                <option value="" <c:if test="${empty command.maxDateTime[1]}">selected</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="maxhour" begin="0" end="23">
                    <option value="<fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty command.maxDateTime[1] && command.maxDateTime[1] == maxhour}">selected</c:if>>
                        <fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>&nbsp;:&nbsp;<select id="maxTimeMin" name="maxMinute" onchange="toggleTime('maxTimeMin', 'maxTimeHour');">
                <option value="" <c:if test="${empty command.maxDateTime[2]}">selected</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="minute" begin="0" end="59">
                    <option value="<fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty command.maxDateTime[2] && command.maxDateTime[2] == minute}">selected</c:if>>
                        <fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>
