<%@ include file="../includes/include.jsp" %>

<fmt:message key="appraisal.review.questionnaire" var="reviewLabel">
    <fmt:param><c:out value="${command.evaluatorDefinition.label}"/></fmt:param>
</fmt:message>
<zynap:infobox title="${reviewLabel}" id="answerReview">

    <table class="infotable" cellspacing="0" id="QGroup">

        <tr>
            <td class="infolabel">
                <fmt:message key="appraisal.review.select.group"/>&nbsp;:&nbsp;
            </td>
            <td class="infodata" colspan="2">
                <spring:bind path="command.selectedGroup">
                    <select name="selectedGroup" onChange="setSelectedEvaluatorAnswers(this)">
                        <option value="" <c:if test="${command.selectedGroup == null}">selected</c:if>><fmt:message key="please.select"/></option>
                        <c:forEach var="questionnaireGroup" items="${command.evaluatorDefinition.questionGroups}">
                            <option value="<c:out value="${questionnaireGroup.label}"/>"<c:if
                                    test="${questionnaireGroup.label == command.selectedGroup}">selected</c:if>><c:out
                                    value="${questionnaireGroup.label}"/></option>
                        </c:forEach>
                    </select>
                </spring:bind>
            </td>
        </tr>

        <c:set var="question" value="${command.evaluatorQuestions[0]}"/>

        <c:if test="${question != null}">
            <tr><td class="infoheading" colspan="3"><fmt:message key="questionnaire.group.answers"/></td></tr>
        </c:if>

        <c:forEach var="question" items="${command.evaluatorQuestions}">
            <tr>
                <td class="infolabel"><fmt:message key="appraisal.review.question"/>&nbsp;:&nbsp;</td>
                <td class="infodata" colspan="2"><c:out value="${question.questionLabel}"/></td>
            </tr>

            <c:choose>
                <c:when test="${question.hasAnswers}">
                    <tr>
                        <td class="infolabel"><fmt:message key="user"/> &nbsp;:&nbsp;</td>
                        <td class="infolabel"><fmt:message key="appraisal.role"/>&nbsp;:&nbsp;</td>
                        <td class="infolabel"><fmt:message key="appraisal.review.answer"/>&nbsp;:&nbsp;</td>
                    </tr>

                    <c:forEach var="answer" items="${question.answers}">
                        <tr>
                            <td class="infodata"><c:out value="${answer.userName}"/></td>
                            <td class="infodata"><c:out value="${answer.role}"/></td>
                            <td class="infodata">
                                <table>
                                    <c:forEach var="displayValue" items="${answer.displayValue}">
                                        <tr>
                                            <td><c:out value="${displayValue}"/></td>                                            
                                        </tr>
                                    </c:forEach>
                                </table>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td class="infodata" colspan="3"><fmt:message key="appraisal.review.no.answers"/></td>
                    </tr>
                </c:otherwise>
            </c:choose>

            <tr>
                <td>&nbsp;</td>
            </tr>

        </c:forEach>
    </table>
</zynap:infobox>

