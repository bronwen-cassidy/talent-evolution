<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}" id="editWF">
<c:set var="published" value="${command.status == 'PUBLISHED' || command.status == 'COMPLETED'}"/>
    
    <zynap:form method="post" name="createQ">
        <table class="infotable" cellspacing="0">
            <c:choose>
                <c:when test="${published}">
                    <%@include file="editquestionnaireworkflowsnippet.jsp"%>
                    <tr>
                        <td class="infolabel"><fmt:message key="questionnaire.definition"/>&nbsp;:&nbsp;</td>
                        <td class="infodata"><c:out value="${command.questionnaireDefinition.label}"/></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <%@include file="editquestionnaireworkflowsnippet.jsp"%>
                    <tr>
                        <c:choose>
                            <c:when test="${not empty command.definitions && command.definitions != null}">
                                <td class="infolabel"><fmt:message key="questionnaire.definition"/>&nbsp;:&nbsp;*</td>
                                <td class="infodata">
                                    <spring:bind path="command.questionnaireDefinition.id">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <option value="" <c:if test="${command.questionnaireDefinition.id == null}">selected="true"</c:if> ><fmt:message key="please.select"/></option>
                                            <c:forEach var="def" items="${command.definitions}">
                                                <option value="<c:out value="${def.id}"/>" <c:if test="${def.id == command.questionnaireDefinition.id}">selected="true"</c:if>>
                                                    <c:out value="${def.label}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <%@ include file="../../includes/error_message.jsp" %>
                                    </spring:bind>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="infolabel"><fmt:message key="questionnaire.definition"/>&nbsp;:</td>
                                <td class="infodata">
                                    <%-- just display the questionnaire definition label --%>
                                    <c:out value="${command.questionnaireDefinition.label}"/>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <tr>
                        <td class="infolabel"><fmt:message key="questionnaire.end.date"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                            <spring:bind path="command.expiryDate">
                                <fmt:message key="date.format" var="datePattern"/>
                                <span style="white-space: nowrap;">
                                    <input id="dob1" name="<c:out value="displayDate"/>" type="text"
                                                  class="input_date"
                                                  value="<fmt:formatDate value="${command.expiryDate}" pattern="${datePattern}"/>"
                                                  readonly="true"/><input type="button" class="partnerbutton"
                                                                          value="..."
                                                                          onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'dob1', 'dob2', null, true);"
                                        <c:if test="${published}">disabled="true"</c:if>
                                        /></span>
                                <input id="dob2" name="<c:out value="${status.expression}"/>" type="hidden" value="<c:out value="${status.value}"/>"/>
                                <%@ include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                    </tr>
                    <tr>
                        <td class="infolabel"><fmt:message key="questionnaire.access_permissions"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                            <table>
                                <tr>
                                    <th></th>
                                    <th>Read</th>
                                    <th>Write</th>
                                </tr>
                                <tr>
                                    <td><fmt:message key="questionnaire.individual_permissions"/>&nbsp;:&nbsp;</td>
                                    <td>
                                        <spring:bind path="command.individualRead">
                                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                    <c:if test="${status.value||command.individualWrite}">checked</c:if>
                                                    <c:if test="${command.individualWrite}">disabled</c:if>
                                                    <c:if test="${published}">disabled="true"</c:if>
                                                    />
                                            <%@ include file="../../includes/error_message.jsp" %>
                                        </spring:bind>
                                    </td>
                                    <td>
                                        <spring:bind path="command.individualWrite">
                                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                    <c:if test="${status.value}">checked</c:if>
                                                    <c:if test="${published}">disabled="true"</c:if>
                                                   onclick="writeSelected(document.createQ.individualRead, document.createQ.individualWrite)"
                                                    />
                                            <c:set var="permissions_error" value="${status.errorMessage}"/>
                                        </spring:bind>
                                    </td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="questionnaire.manager_permissions"/>&nbsp;:&nbsp;</td>
                                    <td>
                                        <spring:bind path="command.managerRead">
                                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                    <c:if test="${status.value||command.managerWrite}">checked</c:if>
                                                    <c:if test="${command.managerWrite}">disabled</c:if>
                                                    <c:if test="${published}">disabled="true"</c:if>
                                                    />
                                            <%@ include file="../../includes/error_message.jsp" %>
                                        </spring:bind>
                                    </td>
                                    <td>
                                        <spring:bind path="command.managerWrite">
                                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                    <c:if test="${status.value}">checked</c:if>
                                                    <c:if test="${published}">disabled="true"</c:if>
                                                   onclick="writeSelected(document.createQ.managerRead, document.createQ.managerWrite)"
                                                    />
                                            <%@ include file="../../includes/error_message.jsp" %>
                                        </spring:bind>
                                    </td>
                                </tr>
                            </table>
                            <!-- display error for permissions condition -->
                            <c:if test="${not permissions_error}">
                                <div class="error">
                                    <c:out value="${permissions_error}"/>
                                </div>
                            </c:if>
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
                                        <option value="<c:out value="${pop.id}"/>"
                                                <c:if test="${status.value == pop.id}">selected</c:if>
                                                >
                                            <c:out value="${pop.label}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                                <%@ include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                    </tr>
                    <tr>
                        <td class="infolabel"><fmt:message key="send.email"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                             <spring:bind path="command.sendEmail">
                                <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                        <c:if test="${status.value}">checked</c:if>
                                        <c:if test="${published}">disabled="true"</c:if>
                                         />
                                <%@ include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            <tr>
                <td class="actionButton">
                    <input type="button" class="inlinebutton" name="cancel" value="<fmt:message key="cancel"/>" onclick="document.forms.cnclQuest.submit();"/>
                    <input type="submit" class="inlinebutton" name="save" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="cnclQuest">
    <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="<%=ParameterConstants.CANCEL_PARAMETER%>"/>
</zynap:form>

<zynap:popup id="calendarPopup">
    <%@ include file="../../includes/calendar.jsp" %>
</zynap:popup>
<zynap:window elementId="userTree" src="../picker/userpicker.htm"/>