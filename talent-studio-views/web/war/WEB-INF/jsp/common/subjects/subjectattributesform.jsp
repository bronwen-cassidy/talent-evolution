<%@ include file="../../includes/include.jsp" %>

<fmt:message key="generic.for" var="for"/>
<c:set var="msg" value="${command.displayContentLabel} ${for} ${command.label}"/>

<zynap:infobox title="${msg}">

    <input type="hidden" id="google_spell_allow" name="gsa_x" value="true"/>

    <zynap:form name="addsubda" method="post" encType="multipart/form-data">
        <table class="infotable" cellspacing="0">
            <c:forEach var="core" items="${command.coreValues}">
                <tr>
                    <td class="infolabel"><c:out value="${core.label}"/>&nbsp;:&nbsp;<c:if test="${core.systemAttribute || core.mandatory}">*</c:if></td>
                    <td class="infodata">
                        <c:choose>
                            <c:when test="${core.attributeName == 'picture'}">
                                <spring:bind path="command.file">
                                    <c:if test="${command.hasPicture}"><fmt:message key="subject.picture.provided"/><br/></c:if>
                                    <input id="subPict" type="file" class="input_file" name="<c:out value="${status.expression}"/>"/>
                                    <%@ include file="../../includes/error_message.jsp" %>
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
                                    <%@ include file="../../includes/error_message.jsp" %>
                                </spring:bind>
                            </c:when>

                            <c:when test="${core.columnType == 'DATE'}">
                                <fmt:message key="date.format" var="datePattern"/>
                                <spring:bind path="command.${core.attributeName}">
                                    <span style="white-space: nowrap;"><input id="dob3" name="<c:out value="displayDate"/>" type="text" class="input_date" value="<fmt:formatDate value="${command.dateOfBirth}" pattern="${datePattern}"/>" readonly="true"/><input type="button" class="partnerbutton" value="..." onclick="popupShowCalendar('<zynap:message code="select.date" javaScriptEscape="true"/>', this, 'calendarPopup', 'dob3', 'dob4', null, true);"/></span>
                                    <input id="dob4" name="<c:out value="${core.attributeName}"/>" type="hidden" value="<c:out value="${status.value}"/>"/>
                                    <%@ include file="../../includes/error_message.jsp" %>
                                </spring:bind>
                            </c:when>

                            <c:otherwise>
                                <spring:bind path="command.${core.attributeName}">
                                    <input type="text" maxlength="150" class="input_text" name="<c:out value="${core.attributeName}"/>" value="<c:out value="${status.value}"/>"/>
                                    <%@ include file="../../includes/error_message.jsp" %>
                                </spring:bind>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            
            <%-- include extended attributes --%>
            <c:set var="currentFormName" value="addsubda"/>
            <%@ include file="../attributes/artefactDASnippet.jsp" %>

            <c:set var="nothingEditable" value="${empty command.coreValues && empty command.wrappedDynamicAttributes}"/>
            <c:if test="${nothingEditable}">
                <tr>
                    <td class="infomessage" colspan="2"><fmt:message key="nothing.found.to.edit"/></td>
                </tr>
            </c:if>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <c:if test="${not nothingEditable}">
                        <input class="inlinebutton" type="submit" name="_edit" value="<fmt:message key="save"/>"/>
                    </c:if>
                </td>
            </tr>

        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<zynap:popup id="calendarPopup">
    <%@ include file="../../includes/calendar.jsp" %>
</zynap:popup>
