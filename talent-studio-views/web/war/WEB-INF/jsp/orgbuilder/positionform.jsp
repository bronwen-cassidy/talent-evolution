<%@ include file="../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>

<c:set var="hasOuTree" value="${outree != null && !empty outree}"/>

<zynap:infobox title="${msg}">
    <zynap:form name="_next" method="post" encType="multipart/form-data">
        <table class="infotable" cellspacing="0">
            <c:forEach var="core" items="${command.coreValues}">
                <tr>
                    <td class="infolabel"><c:out value="${core.label}"/>&nbsp;:&nbsp;<c:if test="${core.systemAttribute}">*</c:if></td>
                    <spring:bind path="command.position.${core.attributeName}">
                        <td class="infodata">
                            <c:choose>

                                <c:when test="${core.attributeName == 'organisationUnit.label'}">
                                    <zynap:message code="organisation.unit" var="ouMsg" javaScriptEscape="true"/>
                                    <span style="white-space: nowrap;"><input id="oufld4" type="text" class="input_text"
                                                                              value="<c:out value="${command.position.organisationUnit.label}"/>"
                                                                              name="position.organisationUnit.label" readonly="true"/><input
                                            type="button" id="pick_ou" class="partnerbutton" <c:if test="${!hasOuTree}">disabled</c:if> value="..."
                                            onclick="popupShowTree('<c:out value="${ouMsg}"/>', this, 'secureOuPicker', 'oufld4', 'oufld5');"/></span>
                                    <input id="oufld5" type="hidden" name="position.organisationUnit.id"
                                           value="<c:out value="${command.position.organisationUnit.id}"/>"/>
                                </c:when>

                                <c:when test="${core.columnType == 'TEXTAREA'}">
                                    <textarea name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textArea>
                                </c:when>

                                <c:otherwise>
                                    <input type="text" maxlength="150" class="input_text" name="position.<c:out value="${core.attributeName}"/>"
                                           value="<c:out value="${status.value}"/>"/>
                                </c:otherwise>

                            </c:choose>
                            <%@ include file="../includes/error_message.jsp" %>
                        </td>
                    </spring:bind>
                </tr>
            </c:forEach>

            <c:if test="${command.wrappedDynamicAttributes != null}">
                <c:set var="currentFormName" value="_next"/>
                <%@ include file="../common/attributes/artefactDASnippet.jsp" %>
            </c:if>

            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>"
                           onclick="document.forms._cancel.submit();"/>
                    <c:choose>
                        <%-- If the position is the default position no associations can be added so the save button must be displayed --%>
                        <c:when test="${command.default}">
                            <input class="inlinebutton" type="submit" name="_next" value="<fmt:message key="save"/>"/>
                            <input type="hidden" name="_finish" value="_finish"/>
                        </c:when>
                        <c:otherwise>
                            <input class="inlinebutton" type="submit" name="_target1" value="<fmt:message key="wizard.next"/>"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<c:if test="${hasOuTree}">
    <zynap:window elementId="secureOuPicker">
        <zynap:serverTree trees="${outree}" branchIcon="ClosedFolder.gif" emptyBranchIcon="ou.gif" leafIcon="ou.gif" branchSelectable="true"/>
    </zynap:window>
</c:if>

<zynap:popup id="calendarPopup">
    <%@ include file="../includes/calendar.jsp" %>
</zynap:popup>
