<%@ page import="com.zynap.talentstudio.analysis.populations.IPopulationEngine,
                 com.zynap.talentstudio.web.analysis.reports.BaseReportsWizardController"%>
<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="analysis.function.form" var="msg"/>

<zynap:infobox title="${msg}">
    <zynap:form name="function" method="post" encType="multipart/form-data">
        <table class="infotable" cellspacing="0" id="details">
            <!-- Add data here -->
            <input id="pgTarget" type="hidden" name="" value="-1"/>

            <c:set var="firstPage" value="false"/>

            <tr>
                <td class="infodata" colspan="2">
                    <table class="infotable" cellspacing="0" id="function">
                       <c:if test="${command.selectedFunction.operands!=null  && not empty command.selectedFunction.operands}">
                        <tr>
                            <td class="infolabel">
                                <fmt:message key="analysis.function.bkt"/>
                            </td>

                            <td class="infolabel">
                                <fmt:message key="analysis.function.attribute"/>
                            </td>
                            <td class="infolabel">
                                <fmt:message key="analysis.function.bkt"/>
                            </td>
                            <td class="infolabel">
                                <fmt:message key="analysis.function.operator"/>
                            </td>
                        </tr>
                        </c:if>
                        <c:forEach var="operand" items="${command.selectedFunction.operands}" varStatus="count" >
                            <tr>
                                <spring:bind path="command.selectedFunction.operands[${count.index}].leftBracket">
                                    <td class="infodata">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <option value="" <c:if test="${operand.leftBracket == ''}">selected</c:if>>    </option>
                                            <option value="<%=IPopulationEngine.LEFT_BRCKT_%>" <c:if test="${operand.leftBracket == '('}">selected</c:if>> ( </option>
                                        </select>
                                        <%@include file="../../../includes/error_message.jsp" %>                                        
                                    </td>
                                </spring:bind>

                                <spring:bind path="command.selectedFunction.operands[${count.index}].attribute">
                                    <td class="infodata">

                                        <c:set var="btnAction">javascript:showFormulaTree('<c:out value="${count.index}"/>', '<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'criteriaTree', '<c:out value="${status.expression}_label"/>', '<c:out value="${status.expression}"/>')</c:set>
                                        <%-- determine the correct label --%>
                                        <fmt:message key="please.select" var="label"/>
                                        <c:if test="${operand.attributeSet}">
                                            <c:set var="label" value="${operand.attributeLabel}"/>
                                        </c:if>

                                        <span style="white-space:nowrap;"><input id="<c:out value="${status.expression}"/>_label" type="text" class="input_text"
                                            value="<c:out value="${label}"/>"
                                                name="<c:out value="${status.expression}_label"/>"
                                                readonly="true"
										/><input type="button"
                                                class="partnerbutton"
                                                value="..." id="navOUPopup"
                                                onclick="<c:out value="${btnAction}"/>"/></span>
                                        <input id="<c:out value="${status.expression}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />

                                        <%@include file="../../../includes/error_message.jsp" %>
                                    </td>
                                </spring:bind>

                                <spring:bind path="command.selectedFunction.operands[${count.index}].rightBracket">
                                    <td class="infodata">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <option value="" <c:if test="${operand.rightBracket == ''}">selected</c:if>>    </option>
                                            <option value="<%=IPopulationEngine.RIGHT_BRCKT_%>" <c:if test="${operand.rightBracket ==')'}">selected</c:if>> ) </option>
                                        </select>
                                        <%@include file="../../../includes/error_message.jsp" %>
                                    </td>
                                </spring:bind>

                                <spring:bind path="command.selectedFunction.operands[${count.index}].operator">
                                    <td class="infodata">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <option value="" <c:if test="${operand.operator == null}">selected</c:if>><fmt:message key="please.select"/></option>
                                            <c:forEach var="operator" items="${operators}">
                                                <option value="<c:out value="${operator}"/>" <c:if test="${operand.operator == operator}">selected</c:if>><c:out value="${operator}"/></option>
                                            </c:forEach>
                                        </select>
                                        <%@include file="../../../includes/error_message.jsp" %>
                                    </td>
                                </spring:bind>

                                <td class="infodata">
                                    <c:if test="${count.index > 1}">
                                        <input class="inlinebutton" type="button" name="" value="<fmt:message key="delete"/>" onclick="removeOperand('<c:out value="${count.index}"/>');"/>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <input class="inlinebutton" type="button" name="addOpe" value="<fmt:message key="add"/>" onclick="addOperand()"/>
                </td>
            </tr>

            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="cancelButton" value="<fmt:message key="cancel"/>" onClick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="saveButton" value="<fmt:message key="save"/>" onClick="saveFormula();"/>
                </td>
            </tr>

        </table>

        <input id="<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>" type="hidden" name="<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>" value="<c:out value="${command.currentColumnIndex}"/>">

    </zynap:form>
</zynap:infobox>


<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_target10" value="10"/>
</zynap:form>



<c:url value="/picker/metricpicker.htm" var="pickerUrl">
    <c:param name="populationType" value="${command.type}"/>
</c:url>

<zynap:window elementId="criteriaTree" src="${pickerUrl}"/>


<%-- separate form to hold hidden parameters used by javascript --%>
<form name="temp">
    <input type="hidden" name="index" value="">
</form>
