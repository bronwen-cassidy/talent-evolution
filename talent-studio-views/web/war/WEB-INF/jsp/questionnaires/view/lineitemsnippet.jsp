<%@ include file="../../includes/include.jsp" %>

<c:set var="multiQuestionLabel" value="${lineItem.label}" scope="request" />
<tr>
    <td class="infodata" title="<c:out value="${multiQuestionLabel}"/>" colspan="2">
        <table class="infotable">

            <c:set var="grid" value="${lineItem.grid}"/>
            <c:set var="numQuestions" value="${lineItem.numberOfQuestionWrappers + 1}"/>
            <c:set var="cellWidth" value="${100/numQuestions}"/>

            <%-- display headers first --%>
            <tr>
                <td class="infosubheading">
                    <c:out value="${multiQuestionLabel}"/>
                </td>
                    <c:forEach var="cell" items="${grid[0]}">
                        <td class="infosubheading">
                            <%--<c:set var="question" value="${cell}" scope="request"/>--%>
                            <%--<c:set var="editable" value="false" scope="request"/>--%>
                            <c:out value="${cell.label}"/>&nbsp;<c:if test="${cell.mandatory}">*</c:if>&nbsp;
                            <%--<c:if test="${question.hasHelpText}"><c:import url="../questionnaires/helptextinclude.jsp"/></c:if>--%>
                        </td>
                    </c:forEach>
            </tr>

            <%-- iterate questionattributewrappers inside grid --%>
            <c:forEach var="row" items="${grid}" varStatus="rowIndex" >

                <tr>
                    <c:forEach var="cell" items="${row}" varStatus="colIndex" >
                        <c:set var="question" value="${cell}" scope="request"/>
                        <c:set var="fieldId" scope="request"><zynap:id><c:out value="${question.label}"/><c:out value="${rowIndex.index}"/></zynap:id></c:set>
                        <c:set var="titleAttr" scope="request"><c:if test="${question.hasTitle}">title="<c:out value="${question.title}"/>"</c:if></c:set>

                        <c:if test="${colIndex.index == 0}">
                            <td class="infodata" width="<c:out value="${cellWidth}"/>%">
                                <c:if test="${question.lineItemLabel != null && question.lineItemLabel != ''}"><c:out value="${question.lineItemLabel}"/>&nbsp;:&nbsp;</c:if>
                                <c:set var="editable" value="false" scope="request"/>
                                <c:if test="${question.hasHelpText}"><c:import url="../questionnaires/helptextinclude.jsp"/></c:if>
                            </td>
                        </c:if>

                        <%-- display question --%>
                        <td class="infodata" width="<c:out value="${cellWidth}"/>%">
                            <c:set var="showHorizontal" value="true" scope="request"/>
                            <c:import url="../questionnaires/view/viewquestionsnippet.jsp"/>
                        </td>

                    </c:forEach>
                </tr>

            </c:forEach>

        </table>
    </td>
</tr>