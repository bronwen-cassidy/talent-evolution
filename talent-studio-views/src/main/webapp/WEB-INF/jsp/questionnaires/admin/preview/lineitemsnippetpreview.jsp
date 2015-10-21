<%@ include file="../../../includes/include.jsp" %>

<c:set var="multiQuestionLabel" value="${lineItem.label}" scope="request" />

<tr>
    <td class="infodata" title="<c:out value="${multiQuestionLabel}"/>" colspan="2">

        <table class="question_table">
            <c:set var="grid" value="${lineItem.grid}"/>
            <c:set var="numQuestions" value="${lineItem.numberOfQuestionWrappers + 1}"/>
            <c:set var="cellWidth" value="${90/numQuestions}%"/>

            <%-- display headers first --%>
            <tr>
                <td class="infosubheading">
                    <div class="<c:out value="${openStyle}"/>"><c:out value="${lineItem.label}"/></div>
                    <div class="<c:out value="${closedStyle}"/>">
                        <!-- the multi question label -->
                        <spring:bind path="command.questionnaireGroups[${indexer.index}].wrappedDynamicAttributes[${questionIndexer.index}].label">
                            <input class="label_input_text" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                            <%@include file="../../../includes/error_message.jsp"%>
                        </spring:bind>
                    </div>
                </td>
                
                <c:forEach var="cell" items="${grid[0]}" varStatus="lineItemQIndexer">
                    <td class="infosubheading">
                        <div class="<c:out value="${openStyle}"/>"><c:out value="${cell.label}"/>&nbsp;<c:if test="${cell.mandatory}">*</c:if>&nbsp;</div>
                        <div class="<c:out value="${closedStyle}"/>">
                            <spring:bind path="command.questionnaireGroups[${indexer.index}].wrappedDynamicAttributes[${questionIndexer.index}].grid[0][${lineItemQIndexer.index}].label">
                                <input class="label_input_text" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                <%@include file="../../../includes/error_message.jsp"%>
                            </spring:bind>
                        </div>
                    </td>
                </c:forEach>
                <%--space for any delete buttons or disable checkboxes--%>
                <c:if test="${lineItem.dynamicOrManagerDisable}">
                    <td class="infosubheading">
                        <!-- the disable checkbox for managers -->
                        <c:if test="${lineItem.canManagerDisable && command.managerView}"><fmt:message key="click.to.disable.row"/></c:if>
                    </td>
                </c:if>
            </tr>

            <%-- iterate questionattributewrappers inside grid --%>
            <c:forEach var="row" items="${grid}" varStatus="rowIndex" >

                <tr>

                    <c:forEach var="cell" items="${row}" varStatus="colIndex" >

                        <%-- record number of columns for later - required to set colspan on row that holds add button --%>
                        <c:set var="numCols" value="${colIndex.index}"/>
                        <c:set var="question" value="${cell}" scope="request"/>
                        <c:set var="isDynamic" value="${question.dynamic}"/>
                        <c:set var="fieldId" scope="request"><zynap:id><c:out value="${question.label}"/><c:out value="${rowIndex.index}"/></zynap:id></c:set>
                        <c:set var="titleAttr" scope="request"><c:if test="${question.hasTitle}">title="<c:out value="${question.title}"/>"</c:if></c:set>
                        <c:set var="prefix" value="command.wrappedDynamicAttributes[${index}]" scope="request"/>
                        <c:if test="${isDynamic}">
                            <c:set var="prefix" value="command.wrappedDynamicAttributes[${index}].questionWrappers[${rowIndex.index}]" scope="request"/>
                        </c:if>                            

                        <%-- Navigation only put before first column --%>
                        <c:if test="${numCols == 0}">
                            <td class="infodata">
                                <div class="<c:out value="${openStyle}"/>">
                                    <c:if test="${question.lineItemLabel != null && question.lineItemLabel != ''}"><c:out value="${question.lineItemLabel}"/>&nbsp;:&nbsp;</c:if>
                                </div>
                                <div class="<c:out value="${closedStyle}"/>">
                                    <!--line item label -->
                                    <spring:bind path="command.questionnaireGroups[${indexer.index}].wrappedDynamicAttributes[${questionIndexer.index}].grid[${rowIndex.index}][${colIndex.index}].lineItemLabel">
                                        <input class="label_input_text" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                        <%@include file="../../../includes/error_message.jsp"%>
                                    </spring:bind>
                                </div>
                                <c:set var="editable" value="true" scope="request"/>
                                <c:import url="../questionnaires/helptextinclude.jsp"/>
                            </td>
                        </c:if>
                        
                        <%-- display question --%>
                        <td class="infodata">
                            <c:set var="showHorizontal" value="true" scope="request"/>
                            <c:import url="../questionnaires/admin/preview/viewquestionpreviewsnippet.jsp"/>
                        </td>

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
                            <input type="button" class="inlinebutton" name="add_dlibutton" value="<fmt:message key="add"/>" disabled="true" />
                        </td>
                    </tr>
                <c:set var="index" value="${index + lineItem.numberOfQuestionWrappers}" scope="request"/>
            </c:if>
            
        </table>
    </td>
</tr>