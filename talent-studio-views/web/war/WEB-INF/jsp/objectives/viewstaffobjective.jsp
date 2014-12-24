<%@ include file="../includes/include.jsp"%>

<zynap:evalBack>
    <zynap:actionbox>
        <zynap:actionEntry>
            <fmt:message key="back" var="buttonLabel" />
            <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
        </zynap:actionEntry>
    </zynap:actionbox>
</zynap:evalBack>

<zynap:infobox id="viewObj" title="${title}">

    <c:set var="subject" value="${command.nodeInfo.subject}"/>
    <c:set var="organisationUnits" value="${command.nodeInfo.organisationUnits}"/>
    <c:set var="positions" value="${command.nodeInfo.positions}"/>
    <%@include file="nodeinfo.jsp"%>

</zynap:infobox>

<zynap:tab defaultTab="objectives" tabParamName="activeTab" url="javascript">
    <fmt:message key="goals.page" var="objectiveLabel" />
    <zynap:tabName value="${objectiveLabel}" name="objectives"/>

<c:if test="${command.approvedOrComplete}">
    <fmt:message key="goals.assessment" var="assessmentLabel" />
    <zynap:tabName value="Assessments" name="assessments"/>

    <c:if test="${command.hasOtherAssessments}">
        <fmt:message key="other.assessments" var="assessorLabel" />
        <zynap:tabName value="${assessorLabel}" name="assessors"/>
    </c:if>
</c:if>

    <div id="objectives_span" style="display:<c:choose><c:when test="${command.activeTab == 'objectives'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:set var="objectivesMsg" value="${command.label}" scope="request"/>
        <c:set var="objectiveIndex" value="0" scope="request"/>
        <c:set var="objective" value="${command}" scope="request"/>
        <c:import url="../objectives/viewobjective.jsp"/>
    </div>

<c:if test="${command.approvedOrComplete}">
    <div id="assessments_span" style="display:<c:choose><c:when test="${command.activeTab == 'assessments'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">

        <zynap:form action="" method="post" name="assFrmNme">

            <c:set var="objectivesMsg" value="${command.label}" scope="request"/>
            <c:set var="objectiveIndex" value="0" scope="request"/>
            <c:set var="manager" value="true" scope="request"/>
            <c:set var="prefix" value="assessment" scope="request"/>
            <c:set var="objective" value="${command}" scope="request"/>
            <c:set var="assessment" value="${command.assessment}" scope="request"/>

            <zynap:infobox title="${objectivesMsg}">
                <table class="infotable" cellpadding="0" cellspacing="0">
                    <c:import url="../objectives/objectiveassessmentform.jsp"/>
                    <c:if test="${not assessment.approved}">
                        <tr>
                            <td class="infobutton" colspan="3" align="center">
                                <spring:bind path="command.assessment.approved">
                                    <input id="assessmentsApproved" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                </spring:bind>
                                <span><fmt:message key="send.email"/></span>
                                <span>
                                    <spring:bind path="command.sendEmail">
                                        <input type="checkbox" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" <c:if test="${status.value}">checked="true"</c:if> />
                                    </spring:bind>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="infobutton" colspan="3" align="center">                                
                                <input class="inlinebutton" type="submit" id="finish" name="_finish" value="<fmt:message key="save"/>" title=""/>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </zynap:infobox>
        </zynap:form>

    </div>

    <c:if test="${command.hasOtherAssessments}">

        <div id="assessors_span" style="display:none">

            <c:set var="rowClass" value="odd"/>
            <fmt:message key="assessments" var="assessorsTitle"/>
            <zynap:infobox title="${assessorsTitle}">
                <table class="infotable" cellpadding="0" cellspacing="0">
                    <thead>
                        <tr>
                            <th><fmt:message key="assessor"/></th>
                            <th><fmt:message key="generic.comments"/></th>
                            <th><fmt:message key="generic.rating"/></th>
                        </tr>
                    </thead>
                    <c:forEach var="assessment" items="${command.assessments}">
                        <c:choose><c:when test="${rowClass == 'odd'}"><c:set var="rowClass" value="even"/></c:when><c:otherwise><c:set var="rowClass" value="odd"/></c:otherwise></c:choose>
                        <%-- assessors assessments --%>
                        <tr class="<c:out value="${rowClass}"/>">
                            <td class="infodata"><c:out value="${assessment.user.label}"/></td>
                            <td class="infodata"><c:out value="${assessment.comment}"/></td>
                            <td class="infodata"><c:out value="${assessment.ratingValue.label}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </zynap:infobox>
        </div>

    </c:if>
</c:if>

</zynap:tab>