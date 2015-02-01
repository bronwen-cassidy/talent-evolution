<%@ include file="../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="es_next" method="post" encType="multipart/form-data">
        <table class="infotable" cellspacing="0">
            <!-- add the title -->
            <!-- add all attributes defined in the displayConfigItem -->
            <c:forEach var="core" items="${command.coreValues}">
                <tr>
                    <td class="infolabel"><c:out value="${core.label}"/>&nbsp;:&nbsp;<c:if test="${core.systemAttribute}">*</c:if></td>
                    <td class="infodata">
                        <c:choose>
                            <c:when test="${core.attributeName == 'picture'}">
                                <spring:bind path="command.file">
                                    <c:if test="${empty status.errorMessage && command.file != null}"><fmt:message key="subject.picture.provided"/><br/></c:if>
                                    <input id="subPict" type="file" class="input_file" name="<c:out value="${status.expression}"/>" value="<fmt:message key="subject.picture.alttext"/>"/>
                                    <%@ include file="../includes/error_message.jsp" %>
                                </spring:bind>
                            </c:when>
                            <%--  special attribute, it is the only core detail that gets it's value from a selection --%>
                            <c:when test="${core.attributeName == 'coreDetail.title'}">
                                <spring:bind path="command.${core.attributeName}">
                                    <select name="<c:out value="${core.attributeName}"/>">
                                        <option value="" <c:if test="${command.title == null}">selected</c:if>><fmt:message key="please.select"/></option>
                                        <c:forEach var="tle" items="${titles}">
                                            <option value="<c:out value="${tle.label}"/>"
                                                <c:if test="${command.title == tle.label}">selected</c:if>><c:out value="${tle.label}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <%@ include file="../includes/error_message.jsp" %>
                                </spring:bind>
                            </c:when>

                            <c:when test="${core.columnType == 'DATE'}">
                                <fmt:message key="date.format" var="datePattern"/>
                                <spring:bind path="command.${core.attributeName}">
                                    <span style="white-space: nowrap;"><input id="dob3" name="<c:out value="displayDate"/>" type="text" class="input_date" value="<fmt:formatDate value="${command.dateOfBirth}" pattern="${datePattern}"/>" readonly="true"/><input type="button" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'dob3', 'dob4', null, true);"/></span>
                                    <input id="dob4" name="<c:out value="${core.attributeName}"/>" type="hidden" value="<c:out value="${status.value}"/>"/>
                                    <%@ include file="../includes/error_message.jsp" %>
                                </spring:bind>
                            </c:when>

                            <c:otherwise>
                                <spring:bind path="command.${core.attributeName}">
                                    <input type="text" maxlength="150" class="input_text" name="<c:out value="${core.attributeName}"/>" value="<c:out value="${status.value}"/>"/>
                                    <%@ include file="../includes/error_message.jsp" %>
                                </spring:bind>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>

           <c:if test="${command.wrappedDynamicAttributes != null}">
                <c:set var="currentFormName" value="es_next"/>
                <%@ include file="../common/attributes/artefactDASnippet.jsp" %>
            </c:if>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="submit" name="_cancel" value="<fmt:message key="cancel"/>"/>
                    <input class="inlinebutton" type="submit" name="_target1" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:popup id="calendarPopup">
    <%@ include file="../includes/calendar.jsp" %>
</zynap:popup>
