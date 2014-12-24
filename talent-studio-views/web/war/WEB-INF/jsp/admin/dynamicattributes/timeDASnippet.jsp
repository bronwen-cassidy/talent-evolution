
<tr>
    <td class="infolabel"><fmt:message key="da.earliest"/>&nbsp;:&nbsp;</td>
    <spring:bind path="command.minSize">
        <td class="infodata">
            <select id="minTimeHour" name="minHour" onchange="javascript:toggleTime('minTimeHour', 'minTimeMin');">
                <option value="" <c:if test="${empty command.minTime[0]}">selected</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="hour" begin="0" end="23">
                    <option value="<fmt:formatNumber value="${hour}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty command.minTime[0] && command.minTime[0] == hour}">selected</c:if>>
                        <fmt:formatNumber value="${hour}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>&nbsp;:&nbsp;<select id="minTimeMin" name="minMinute" onchange="javascript:toggleTime('minTimeMin', 'minTimeHour');">
                <option value="" <c:if test="${empty command.minTime[1]}">selected</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="minute" begin="0" end="59">
                    <option value="<fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty command.minTime[1] && command.minTime[1] == minute}">selected</c:if>>
                        <fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>
<tr>
    <spring:bind path="command.maxSize">
        <td class="infolabel"><fmt:message key="da.latest"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <select id="maxTimeHour" name="maxHour" onchange="javascript:toggleTime('maxTimeHour', 'maxTimeMin');">
                <option value="" <c:if test="${empty command.maxTime[0]}">selected</c:if>>
                    <c:out value=""/>
                </option>            
                <c:forEach var="maxhour" begin="0" end="23">
                    <option value="<fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty command.maxTime[0] && command.maxTime[0] == maxhour}">selected</c:if>>
                        <fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>&nbsp;:&nbsp;<select id="maxTimeMin" name="maxMinute" onchange="javascript:toggleTime('maxTimeMin', 'maxTimeHour');">
                <option value="" <c:if test="${empty command.maxTime[1]}">selected</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="minute" begin="0" end="59">
                    <option value="<fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>" <c:if test="${!empty command.maxTime[1] && command.maxTime[1] == minute}">selected</c:if>>
                        <fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>
            <%@include file="../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>
