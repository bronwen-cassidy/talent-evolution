<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ include file="../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <zynap:window elementId="userTree" src="../picker/userpicker.htm"/>
    <spring:bind path="command">
        <%@include file="../includes/error_messages.jsp" %>
    </spring:bind>

    <fmt:message key="date.format" var="datePattern"/>
    <form:form name="_add" method="post">
        <table cellspacing="0" class="infotable">
            <tr>
                <td class="infolabel"><fmt:message key="generic.label"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata">
                        <input type="text" maxlength="100" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <tr>
                <td class="infolabel"><fmt:message key="questionnaire.end.date"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.expiryDate">
                    <td class="infodata">
                        <span style="white-space: nowrap;"><input id="dob1" name="<c:out value="displayDate"/>" type="text" class="input_date" value="<fmt:formatDate value="${command.expiryDate}" pattern="${datePattern}"/>" readonly="true"/><input type="button" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'dob1', 'dob2', null, false);" <c:if test="${published}">disabled="true"</c:if>/></span>
                        <input id="dob2" name="<c:out value="${status.expression}"/>" type="hidden" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="user.managed"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.userManagedReview">
                    <td class="infodata">
                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                               <c:if test="${status.value}">checked</c:if> <c:if test="${published}">disabled="true"</c:if> onclick="enableDisableCheckbox(this, 'sEmailId');"/>
                        <%@include file="../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="send.email"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.notifiable">
                    <td class="infodata">
                        <input id="sEmailId" type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${status.value}">checked</c:if> <c:if test="${published}">disabled="true"</c:if>/>
                        <%@include file="../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="questionnaire.managerdefinition"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.managerQuestionnaireDefinitionId">
                        <select id="managerQuestionnaireDefinitionId" name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            <c:forEach var="definition" items="${command.definitions}">
                                <option value="<c:out value="${definition.id}"/>" <c:if test="${status.value == definition.id}">selected</c:if>><c:out value="${definition.label}"/></option>
                            </c:forEach>
                        </select>
                        <%@ include file="../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>

            <tr>
                <td class="infolabel"><fmt:message key="questionnaire.generaldefinition"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.generalQuestionnaireDefinitionId">
                        <select id="generalQuestionnaireDefinitionId" name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            <c:forEach var="definition" items="${command.definitions}">
                                <option value="<c:out value="${definition.id}"/>" <c:if test="${status.value == definition.id}">selected</c:if>><c:out value="${definition.label}"/></option>
                            </c:forEach>
                        </select>
                        <%@ include file="../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="questionnaire.hr"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.hrUserLabel">
                        <span style="white-space: nowrap;">
                            <input id="nav_ou_disp_hr" type="text" class="input_text"
                                  value="<c:out value="${status.value}"/>"
                                  name="<c:out value="${status.expression}"/>"
                                  readonly="true"/><input type="button"
                                                          class="partnerbutton"
                                                          value="..." id="navOUPopup"
                                onclick="popupShowServerTree('<zynap:message code="select.hr.user" javaScriptEscape="true"/>', this, 'userTree', 'nav_ou_disp_hr', 'nav_ou_id_hr',null, true)"/>
                        </span>
                    </spring:bind>
                    <spring:bind path="command.hrUserId">
                        <input id="nav_ou_id_hr" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="questionnaire.population"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.populationId">
                        <select id="populationId" name="<c:out value="${status.expression}"/>">
                            <c:forEach var="pop" items="${command.populations}">
                                <option value="<c:out value="${pop.id}"/>" <c:if test="${status.value == pop.id}">selected</c:if>><c:out value="${pop.label}"/></option>
                            </c:forEach>
                        </select>
                        <%@ include file="../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>

            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="save" value="<fmt:message key="save"/>"/>
                </td>
            </tr>

        </table>
    </form:form>
</zynap:infobox>

<zynap:form name="_cancel" method="post">
    <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="true"/>
</zynap:form>


<zynap:popup id="calendarPopup">
    <%@ include file="../includes/calendar.jsp" %>
</zynap:popup>
