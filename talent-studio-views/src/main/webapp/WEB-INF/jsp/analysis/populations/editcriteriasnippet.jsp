<c:set var="attr" scope="request" value="${criteria.attributeDefinition}"/>
<c:set var="attrType" scope="request" value="${attr.attributeDefinition.type}"/>

<c:if test="${attrType == 'TEXT'}">
    <spring:bind path="${prefix}.value">
        <input id="id_<c:out value="${attr.attributeDefinition.id}"/>" type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${attr.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>
    </spring:bind>
</c:if>

<c:if test="${attrType == 'NUMBER' || attrType == 'SUM' || attrType == 'DOUBLE'}">
<spring:bind path="${prefix}.value">
        <input id="id_<c:out value="${attr.attributeDefinition.id}"/>" type="text" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${attr.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>
    </spring:bind>
</c:if>

<c:if test="${attrType == 'LINK'}">
    <spring:bind path="${prefix}.value">
        <input id="id_<c:out value="${attr.attributeDefinition.id}"/>" type="text" class="input_linke" name="<c:out value="${status.expression}"/>" value="<c:out value="${attr.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>
    </spring:bind>
</c:if>

<c:if test="${attrType == 'TEXTAREA' || attrType == 'COMMENTS'}">
    <spring:bind path="${prefix}.value">
        <textarea id="id_<c:out value="${attr.attributeDefinition.id}"/>" name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${attr.value}"/></textArea>
        <%@ include file="../../includes/error_message.jsp" %>
    </spring:bind>
</c:if>

<c:if test="${attrType == 'TIME'}">
    <c:set var="hourId" value="id_${attr.attributeDefinition.id}_hours" />
    <c:set var="minId" value="id_${attr.attributeDefinition.id}_mins" />
    <spring:bind path="${prefix}.time">
        <span style="white-space:nowrap">
            <select id="<c:out value="${hourId}"/>" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.hour" onchange="javascript:toggleTime('<c:out value="${hourId}"/>', '<c:out value="${minId}"/>');">
                <option <c:if test="${attr.hour == null}">selected="selected"</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="maxhour" begin="0" end="23">
                    <option value="<fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>" <c:if test="${attr.hour == maxhour}">selected="selected"</c:if>>
                        <fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>&nbsp;:&nbsp;<select id="<c:out value="${minId}"/>" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.minute" onchange="javascript:toggleTime('<c:out value="${minId}"/>', '<c:out value="${hourId}"/>');">
                <option <c:if test="${attr.minute == null}">selected="selected"</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="minute" begin="0" end="59">
                    <option value="<fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>" <c:if test="${attr.minute == minute}">selected="selected"</c:if>>
                        <fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>
        </span>
        <%@ include file="../../includes/error_message.jsp" %>
    </spring:bind>
</c:if>

<c:if test="${attrType == 'STRUCT' || attrType == 'MULTISELECT' || attrType == 'SELECT'  || attrType == 'RADIO' || attrType == 'ENUMMAPPING'}">
    <spring:bind path="${prefix}.value">
        <select name="<c:out value="${status.expression}"/>">
            <option value="" <c:if test="${attr.value == null}">selected="selected"</c:if>><fmt:message key="please.select"/></option>
            <c:forEach var="vals" items="${attr.attributeDefinition.activeLookupValues}">
                <option value="<c:out value="${vals.id}"/>" <c:if test="${attr.value == vals.id}">selected="selected"</c:if>><c:out value="${vals.label}"/></option>
            </c:forEach>
        </select>
        <%@ include file="../../includes/error_message.jsp" %>
    </spring:bind>
</c:if>

<c:if test="${attrType == 'DATE'}">
    <spring:bind path="${prefix}.date">
        <span style="white-space:nowrap"><input id="id_<c:out value="${count.index}"/>_disp" class="input_date" name="ignoredname<c:out value="${count.index}"/>" type="text" value="<c:out value='${attr.displayValue}'/>"
                    readonly="true"/><input type="button" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'id_<c:out value="${count.index}"/>_disp', 'id_<c:out value="${count.index}"/>_iso');"/></span>
            <input id="id_<c:out value="${count.index}"/>_iso" name="<c:out value="${status.expression}"/>" type="hidden" value="<c:out value="${attr.date}"/>"/>
        <%@include file="../../includes/error_message.jsp" %>
    </spring:bind>
</c:if>

<c:if test="${attrType == 'DATETIME' || attrType == 'LASTUPDATED'}">
    <span style="white-space:nowrap">
        <spring:bind path="${prefix}.dateTime">
            <input id="id_<c:out value="${count.index}"/>_disp" class="input_text" name="ignoredname<c:out value="${count.index}"/>" type="text" value="<c:out value='${attr.dateTimeDateDisplayValue}'/>"
                    readonly="true"/><input type="button" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'id_<c:out value="${count.index}"/>_disp', 'id_<c:out value="${count.index}"/>_iso');"/>
            <input id="id_<c:out value="${count.index}"/>_iso" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.date" type="hidden" value="<c:out value="${attr.date}"/>"/>
        </spring:bind>
        <spring:bind path="command.populationCriterias[${count.index}].attributeDefinition.dateTime">
            <c:set var="hourId" value="id_${count.index}_hours" />
            <c:set var="minId" value="id_${count.index}_mins" />
            <select id="<c:out value="${hourId}"/>" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.hour" onchange="javascript:toggleTime('<c:out value='${hourId}'/>', '<c:out value='${minId}'/>');">
                <option <c:if test="${attr.hour == null}">selected="selected"</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="maxhour" begin="0" end="23">
                    <option value="<fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>" <c:if test="${attr.hour == maxhour}">selected="selected"</c:if>>
                        <fmt:formatNumber value="${maxhour}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>&nbsp;:&nbsp;<select id="<c:out value="${minId}"/>" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.minute" onchange="javascript:toggleTime('<c:out value='${minId}'/>', '<c:out value='${hourId}'/>');">
                <option <c:if test="${attr.minute == null}">selected="selected"</c:if>>
                    <c:out value=""/>
                </option>
                <c:forEach var="minute" begin="0" end="59">
                    <option value="<fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>" <c:if test="${attr.minute == minute}">selected="selected"</c:if>>
                        <fmt:formatNumber value="${minute}" type="number" minIntegerDigits="2"/>
                    </option>
                </c:forEach>
            </select>
        </spring:bind>
    </span>
    <%@ include file="../../includes/error_message.jsp" %>
</c:if>
<c:if test="${attrType == 'LASTUPDATEDBY'}">

    <spring:bind path="${prefix}.value">

        <zynap:message code="users" var="userMsg" javaScriptEscape="true"/>

        <span style="white-space:nowrap"><input id="usersX4<c:out value="${count.index}"/>" type="text" class="input_text" value="<c:out value="${criteria.nodeLabel}"/>" name="populationCriterias[<c:out value="${count.index}"/>].nodeLabel"
            readonly="true"/><input type="button" id="pick_user" class="partnerbutton" value="..." onclick="popupShowServerTree('<c:out value="${userMsg}"/>', this, 'userTree', 'usersX4<c:out value="${count.index}"/>', 'usersX5<c:out value="${count.index}"/>');"/></span>
        <input id="usersX5<c:out value="${count.index}"/>" type="hidden" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.value" value="<c:out value="${command.populationCriterias[count.index].attributeDefinition.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>

    </spring:bind>
</c:if>
