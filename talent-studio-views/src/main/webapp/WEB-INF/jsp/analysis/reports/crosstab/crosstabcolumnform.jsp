<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="reports" method="post" encType="multipart/form-data">
        <spring:bind path="command">
            <%@include file="../../../includes/error_messages.jsp" %>
        </spring:bind>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <input id="backId" type="hidden" name="" value="-1"/>
        <table class="infotable" cellspacing="0">            
            <tr>
                <td class="infoheading" colspan="4"><fmt:message key="crosstab.attributes"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="horizontal"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.horizontalColumn.attribute">

                        <c:set var="btnAction">javascript:showColumnTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'columnTree', '<c:out value="${status.expression}_label"/>', '<c:out value="${status.expression}"/>', 'horizontalColumn.label')</c:set>

                        <%-- determine the correct label --%>
                        <fmt:message key="please.select" var="label"/>
                        <c:if test="${command.horizontalColumn.attributeSet}">
                            <c:set var="label" value="${command.horizontalColumn.attributeLabel}"/>
                        </c:if>

                        <span style="white-space: nowrap;"><input id="<c:out value="${status.expression}"/>_label" type="text" class="input_text"
                            value="<c:out value="${label}"/>"
                                name="<c:out value="${status.expression}_label"/>"
                                readonly="true"
                        /><input type="button"
                                class="partnerbutton"
                                value="..." id="navOUPopup"
                                onclick="<c:out value="${btnAction}"/>"/></span>
                        <input id="<c:out value="${status.expression}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />

                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
                <td class="infolabel"><fmt:message key="attribute.label"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.horizontalColumn.label">
                        <input type="text" maxlength="240" class="input_text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>

                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="vertical"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.verticalColumn.attribute">

                        <c:set var="btnAction">javascript:showColumnTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'columnTree', '<c:out value="${status.expression}_label"/>', '<c:out value="${status.expression}"/>', 'verticalColumn.label')</c:set>

                        <%-- determine the correct label --%>
                        <fmt:message key="please.select" var="label"/>
                        <c:if test="${command.verticalColumn.attributeSet}">
                            <c:set var="label" value="${command.verticalColumn.attributeLabel}"/>
                        </c:if>

                        <span style="white-space: nowrap;"><input id="<c:out value="${status.expression}"/>_label" type="text" class="input_text"
                            value="<c:out value="${label}"/>"
                                name="<c:out value="${status.expression}_label"/>"
                                readonly="true"
                            /><input type="button"
                                class="partnerbutton"
                                value="..." id="navOUPopup"
                                onclick="<c:out value="${btnAction}"/>"/></span>
                        <input id="<c:out value="${status.expression}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />

                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
                <td class="infolabel"><fmt:message key="attribute.label"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.verticalColumn.label">
                        <input type="text" maxlength="240" class="input_text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            
            <tr>
                <td class="infobutton" colspan="4" align="center">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" name="_back" type="button" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('reports', 'pgTarget', '0', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_target3" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<c:url value="/picker/crosstabreportpicker.htm" var="pickerUrl">
    <c:param name="populationType" value="${command.type}"/>
</c:url>

<zynap:window elementId="columnTree" src="${pickerUrl}"/>

