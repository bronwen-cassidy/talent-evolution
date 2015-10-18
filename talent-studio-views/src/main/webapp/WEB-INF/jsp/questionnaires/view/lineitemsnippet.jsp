<%@ include file="../../includes/include.jsp" %>

<c:set var="multiQuestionLabel" value="${lineItem.label}" scope="request" />
<tr>
    <td title="<c:out value="${multiQuestionLabel}"/>" colspan="2">
        <table class="question_table">

            <c:set var="grid" value="${lineItem.grid}"/>
            <c:set var="numQuestions" value="${lineItem.numberOfQuestionWrappers + 1}"/>
            <c:set var="cellWidth" value="${90/numQuestions}%"/>

            <c:set var="rowStyle" value="${grid[0][0].rowStyle}"/>
            <c:set var="headerStyle" value="${grid[0][0].headerStyle}"/>
            <c:if test="${headerStyle == null || headerStyle == ''}">
                <c:set var="headerStyle">max-width:5%;min-width:5%;width:5%;"/></c:set>
            </c:if>
            <c:set var="footerStyle" value="${grid[0][0].footerStyle}"/>
            <c:if test="${footerStyle == null || footerStyle == ''}">
                <c:set var="footerStyle">max-width:5%;min-width:5%;width:5%;"/></c:set>
            </c:if>

            <%-- display headers first --%>
            <tr style="<c:out value="${rowStyle}"/>">
                <td class="infosubheading" style="<c:out value="${headerStyle}"/>">
                    <c:out value="${multiQuestionLabel}"/>
                </td>

                <%-- headings--%>
                <c:forEach var="cell" items="${grid[0]}">

                    <c:set var="cellStyle" value="${cell.cellStyle}"/>
                    <c:if test="${cellStyle == null || cellStyle == ''}">
                        <c:set var="cellStyle">max-width:<c:out value="${cellWidth}"/>;min-width:<c:out value="${cellWidth}"/></c:set>
                    </c:if>

                    <td style="<c:out value="${cellStyle}"/>" class="infosubheading">
                        <c:out value="${cell.label}"/>&nbsp;<c:if test="${cell.mandatory}">*</c:if>&nbsp;
                    </td>
                </c:forEach>
            </tr>

            <%-- iterate questionattributewrappers inside grid --%>
            <c:forEach var="row" items="${grid}" varStatus="rowIndex" >

                <tr style="<c:out value="${rowStyle}"/>">
                    <c:forEach var="cell" items="${row}" varStatus="colIndex" >

                        <c:set var="cellStyle" value="${cell.cellStyle}"/>
                        <c:if test="${cellStyle == null || cellStyle == ''}">
                            <c:set var="cellStyle">max-width:<c:out value="${cellWidth}"/>;min-width:<c:out value="${cellWidth}"/></c:set>
                        </c:if>

                        <c:set var="question" value="${cell}" scope="request"/>
                        <c:set var="fieldId" scope="request"><zynap:id><c:out value="${question.label}"/><c:out value="${rowIndex.index}"/></zynap:id></c:set>
                        <c:set var="titleAttr" scope="request"><c:if test="${question.hasTitle}">title="<c:out value="${question.title}"/>"</c:if></c:set>

                        <c:if test="${colIndex.index == 0}">
                            <td class="infodata" style="<c:out value="${headerStyle}"/>">
                                <c:if test="${question.lineItemLabel != null && question.lineItemLabel != ''}"><c:out value="${question.lineItemLabel}"/>&nbsp;:&nbsp;</c:if>
                                <c:set var="editable" value="false" scope="request"/>
                                <c:if test="${question.hasHelpText}"><c:import url="../questionnaires/helptextinclude.jsp"/></c:if>
                            </td>
                        </c:if>

                        <%-- display question --%>
                        <td style="<c:out value="${cellStyle}"/>" class="questiondata">
                            <c:set var="showHorizontal" value="true" scope="request"/>
                            <c:import url="../questionnaires/view/viewquestionsnippet.jsp"/>
                        </td>

                    </c:forEach>
                </tr>

            </c:forEach>

        </table>
    </td>
</tr>