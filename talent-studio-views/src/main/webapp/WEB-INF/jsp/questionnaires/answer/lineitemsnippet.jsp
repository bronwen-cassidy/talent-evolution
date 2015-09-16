<%@ include file="../../includes/include.jsp" %>

<c:set var="multiQuestionLabel" value="${lineItem.label}" scope="request" />

<tr>
    <td class="infodata" title="<c:out value="${multiQuestionLabel}"/>" colspan="2">

        <c:set var="tableId" value="infotable_id_${index}" scope="request"/>

        <table class="infotable" id="infotable_id_<c:out value="${index}"/>" >

            <c:set var="grid" value="${lineItem.grid}"/>
            <c:set var="numQuestions" value="${lineItem.numberOfQuestionWrappers + 1}" scope="request"/>
            <c:set var="cellWidth" value="${90/numQuestions}%"/>

            <%-- display headers first --%>
            <tr>
                <td class="infosubheading" style="max-width:5px; min-width:5px;">
                    <c:out value="${multiQuestionLabel}"/>
                </td>

                <%-- headings--%>
                <c:forEach var="cell" items="${grid[0]}">

                    <c:set var="cellStyle" value="${cell.cellStyle}"/>
                    <c:if test="${cellStyle == null || cellStyle == ''}">
                        <c:set var="cellStyle">max-width:<c:out value="${cellWidth}"/>%;min-width:<c:out value="${cellWidth}"/>%</c:set>
                    </c:if>

                    <td style="<c:out value="${cellStyle}"/>" class="infosubheading">
                        <c:out value="${cell.label}"/>&nbsp;<c:if test="${cell.mandatory}">*</c:if>&nbsp;
                    </td>
                </c:forEach>

                <%--space for any delete buttons or disable checkboxes--%>
                <c:if test="${lineItem.dynamicOrManagerDisable}">
                    <td style="max-width:5px; min-width:5px" class="infosubheading">
                        <!-- the disable checkbox for managers -->
                        <c:if test="${lineItem.canManagerDisable && command.managerView}"><fmt:message key="click.to.disable.row"/></c:if>
                    </td>
                </c:if>
            </tr>

            <%-- a row of the grid --%>
            <c:forEach var="row" items="${grid}" varStatus="rowIndex">

                <c:set var="rowId" value="litm_row_${rowIndex.index}${index}" scope="request"/>

                <tr id="<c:out value="${rowId}"/>">

                    <c:forEach var="cell" items="${row}" varStatus="colIndex" >

                        <%-- record number of columns for later - required to set colspan on row that holds add button --%>
                        <c:set var="numCols" value="${colIndex.index}" scope="request"/>
                        <c:set var="question" value="${cell}" scope="request"/>
                        <c:set var="isDynamic" value="${question.dynamic}"/>
                        <c:set var="queDisabled" value="${question.rowDisabled}" scope="request"/>
                        <c:set var="fieldId" scope="request"><zynap:id><c:out value="${question.label}"/><c:out value="${rowIndex.index}"/><c:out value="${index}"/></zynap:id></c:set>
                        <c:set var="titleAttr" scope="request"><c:if test="${question.hasTitle}">title="<c:out value="${question.title}"/>"</c:if></c:set>

                        <c:choose>
                            <c:when test="${isDynamic}">
                                <c:set var="prefix" value="command.wrappedDynamicAttributes[${index}].questionWrappers[${rowIndex.index}]" scope="request"/>
                                <c:set var="dynamicIndex" value="${rowIndex.index + 1}" scope="request"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="prefix" value="command.wrappedDynamicAttributes[${index}]" scope="request"/>
                            </c:otherwise>
                        </c:choose>

                        <%-- labels before the first column only --%>
                        <c:if test="${numCols == 0}">
                            <td class="infodata" style="max-width:5px; min-width:5px">
                                <c:if test="${question.lineItemLabel != null && question.lineItemLabel != ''}"><c:out value="${question.lineItemLabel}"/>&nbsp;:&nbsp;</c:if>
                                <c:set var="editable" value="false" scope="request"/>
                                <c:if test="${question.hasHelpText}"><c:import url="../questionnaires/helptextinclude.jsp"/></c:if>
                            </td>
                        </c:if>

                        <c:set var="cellStyle" value="${cell.cellStyle}"/>
                        <c:if test="${cellStyle == null || cellStyle == ''}">
                            <c:set var="cellStyle">max-width:<c:out value="${cellWidth}"/>%;min-width:<c:out value="${cellWidth}"/>%</c:set>
                        </c:if>
                        <%-- display question --%>
                        <td style="<c:out value="${cellStyle}"/>" class="questiondata">
                            <c:set var="showHorizontal" value="true" scope="request"/>
                            <c:import url="../questionnaires/answer/editquestionsnippet.jsp"/>
                        </td>

                        <!-- delete button or disable checkbox at the end -->
                        <c:if test="${lineItem.dynamicOrManagerDisable  && (numCols == (numQuestions - 2))}">
                            <td class="infodata">
                                <table>
                                    <tr>
                                        <td class="actionButton">
                                            <c:if test="${command.managerView && question.canDisable}">
                                                <fmt:message key="click.to.disable.row" var="disableTitleMsg"/>
                                                <spring:bind path="${prefix}.disabled">
                                                    <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                           <c:if test="${queDisabled}">checked="true"</c:if>
                                                           onclick="disableRow('<c:out value="${rowId}"/>', this,'<c:out value="${command.questionnaireId}"/>','<c:out value="${question.lineItemId}"/>');" title="<c:out value="${disableTitleMsg}"/>"/>
                                                    <input name="eventJsId" id="<c:out value="${rowId}_eventJsId"/>" type="hidden" value="13"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
                                                    <input name="lineItemId" id="<c:out value="${rowId}"/>" type="hidden" value="<c:out value="${question.lineItemId}"/>"/> <!-- needed - clonning-->
                                                </spring:bind>
                                            </c:if>
                                        </td>
                                        <td class="actionButton">
                                            <c:if test="${isDynamic}">
                                                <input type="button" class="inlinebutton" <c:if test="${rowIndex.index <= 0 || (!command.managerView && queDisabled)}">style="display:none;"</c:if>
                                                       name="del_d<c:out value="${index}"/>"
                                                       value="<fmt:message key="delete"/>"
                                                       onclick="deleteLineItemRow('<c:out value="${command.questionnaireId}"/>',
                                                               '<c:out value="${question.lineItemId}"/>',
                                                               '<c:out value="${tableId}"/>',
                                                               '<c:out value="${rowId}"/>');"/>

                                                <input name="eventJsId" id="<c:out value="${rowId}_eventJsId"/>" type="hidden" value="14"/>
                                                <input name="lineItemId" id="<c:out value="${rowId}"/>" type="hidden" value="<c:out value="${question.lineItemId}"/>"/> <!-- needed - clonning-->
                                            </c:if>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </c:if>

                        <%-- save old for use when including add button below --%>
                        <c:set var="oldIndex" value="${index}" scope="request"/>

                        <%-- increment index --%>
                        <c:set var="index" value="${index + 1}" scope="request"/>

                    </c:forEach>
                </tr>

                <%-- decrement index at end of columns --%>
                <c:if test="${isDynamic}">
                    <c:set var="index" value="${index - lineItem.numberOfQuestionWrappers}" scope="request"/>
                </c:if>

            </c:forEach>

            <c:if test="${isDynamic}">
                <tr>
                        <%-- add two to num cols always --%>
                    <td class="infodata" colspan="<c:out value="${numCols + 2}"/>">
                        <input type="button" class="inlinebutton"
                               name="add_dlibutton"
                               value="<fmt:message key="add"/>"
                               onclick="addDynamicLineItemRow('infotable_id_<c:out value="${index}"/>','numExpFldId','<c:out value="${command.questionnaireId}"/>')"/>
                        <input id="numExpFldId" type="hidden" value="<c:out value="${index + lineItem.numberOfQuestionWrappers}"/>"/>
                    </td>
                </tr>
            </c:if>

            <%-- increment overall index now that we have finished iterating all rows --%>
            <c:if test="${isDynamic}">
                <c:set var="index" value="${index + lineItem.numberOfQuestionWrappers}" scope="request"/>
            </c:if>
        </table>
    </td>
</tr>