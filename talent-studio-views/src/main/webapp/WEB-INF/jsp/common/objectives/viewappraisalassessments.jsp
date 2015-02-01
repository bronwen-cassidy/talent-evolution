<%@ include file="../../includes/include.jsp"%>
<zynap:infobox title="${objectivesMsg}">

    <table class="infotable" cellpadding="0" cellspacing="0">
        <tr>
            <td class="infonarrative"><c:out value="${objective.label}"/>&nbsp;:&nbsp;<c:out value="${objective.description}"/></td>
        </tr>
    </table>

    <c:set var="hasManagerAssessment" value="${assessment.managerComment != null || assessment.managerRatingValue != null}"/>
    <c:set var="hasSelfAssessment" value="${assessment.selfComment != null || assessment.selfRatingValue != null}"/>
    <c:set var="hasAssessorsAssessments" value="${not empty assessments}"/>

    <c:set var="rowClass" value="odd"/>

    <table class="infotable" cellpadding="0" cellspacing="0">
        <c:choose>
            <c:when test="${hasManagerAssessment || hasSelfAssessment || hasAssessorsAssessments}">
                <thead>
                    <tr>
                        <th><fmt:message key="assessor"/></th>
                        <th><fmt:message key="generic.comments"/></th>
                        <th><fmt:message key="generic.rating"/></th>
                    </tr>
                </thead>
                <c:if test="${hasManagerAssessment}">                    
                    <!-- managers assessement -->
                    <tr class="<c:out value="${rowClass}"/>">
                        <td class="infodata"><fmt:message key="manager.assessment"/> </td>
                        <td class="infodata"><c:out value="${assessment.managerComment}"/></td>
                        <td class="infodata"><c:out value="${assessment.managerRatingValue}"/></td>
                    </tr>
                </c:if>
                <c:if test="${hasSelfAssessment}">
                    <c:choose><c:when test="${rowClass == 'odd'}"><c:set var="rowClass" value="even"/></c:when><c:otherwise><c:set var="rowClass" value="odd"/></c:otherwise></c:choose>
                    <!--self assessment -->
                    <tr class="<c:out value="${rowClass}"/>">
                        <td class="infodata"><c:out value="${subject.label}"/> </td>
                        <td class="infodata"><c:out value="${assessment.selfComment}"/></td>
                        <td class="infodata"><c:out value="${assessment.selfRatingValue}"/></td>
                    </tr>
                </c:if>

                <c:forEach var="assessment" items="${assessments}">
                    <c:choose><c:when test="${rowClass == 'odd'}"><c:set var="rowClass" value="even"/></c:when><c:otherwise><c:set var="rowClass" value="odd"/></c:otherwise></c:choose>
                    <%-- assessors assessments --%>
                    <tr class="<c:out value="${rowClass}"/>">
                        <td class="infodata"><c:out value="${assessment.user.label}"/></td>
                        <td class="infodata"><c:out value="${assessment.comment}"/></td>
                        <td class="infodata"><c:out value="${assessment.ratingValue.label}"/></td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td class="infonmessage">
                        <div class="infomessage">
                            <fmt:message key="no.assessments"/>
                        </div>
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>
</zynap:infobox>
