<%@ include file="../../includes/include.jsp" %>

<c:set var="hasOuTree" value="${outree != null && !empty outree}"/>
<%@include file="../../includes/orgunitpicker.jsp"%>

<fmt:message key="generic.for" var="for"/>
<c:set var="msg" value="${command.displayContentLabel} ${for} ${command.label}"/>

<zynap:infobox title="${msg}">
    <zynap:form name="editposda" method="post" encType="multipart/form-data">
        <table class="infotable" cellspacing="0">
            <c:forEach var="core" items="${command.coreValues}">
                <tr>
                    <td class="infolabel"><c:out value="${core.label}"/>&nbsp;:&nbsp;<c:if test="${core.systemAttribute || core.mandatory}">*</c:if></td>
                    <spring:bind path="command.position.${core.attributeName}">
                        <td class="infodata">
                            <c:choose>
                                <c:when test="${core.attributeName == 'organisationUnit.label'}">
                                    <zynap:message code="organisation.unit" var="ouMsg" javaScriptEscape="true"/>
                                    <span style="white-space: nowrap;"><input id="oufld4" type="text" class="input_text" value="<c:out value="${command.position.organisationUnit.label}"/>" name="position.organisationUnit.label" readonly="true"/><input type="button" id="pick_ou" class="partnerbutton" <c:if test="${!hasOuTree}">disabled</c:if> value="..." onclick="popupShowTree('<c:out value="${ouMsg}"/>', this, 'ouPicker', 'oufld4', 'oufld5');"/></span>
                                    <input id="oufld5" type="hidden" name="position.organisationUnit.id" value="<c:out value="${command.position.organisationUnit.id}"/>"/>
                                </c:when>          
                                <c:when test="${core.columnType == 'TEXTAREA'}">
                                    <textarea name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textArea>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" maxlength="150" class="input_text" name="position.<c:out value="${core.attributeName}"/>" value="<c:out value="${status.value}"/>"/>
                                </c:otherwise>
                            </c:choose>
                            <%@include file="../../includes/error_message.jsp" %>
                        </td>
                    </spring:bind>
                </tr>
            </c:forEach>
            <%--include the active flag --%>
            <tr>
                <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.active">
                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.active}">checked="yes"</c:if>/>
                    </spring:bind>
                </td>
            </tr>
            
            <%-- include extended attributes --%>
            <c:set var="currentFormName" value="editposda"/>
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
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
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
