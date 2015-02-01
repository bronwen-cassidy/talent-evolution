<%@ include file="../includes/include.jsp"%>

<zynap:actionbox>

    <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
    </zynap:actionEntry>

</zynap:actionbox>

<zynap:tab defaultTab="objectives" tabParamName="activeTab" url="javascript">
    <fmt:message key="goals.page" var="objectiveLabel" />
    <zynap:tabName value="${objectiveLabel}" name="objectives"/>

<c:if test="${command.approvedOrComplete}">
    <fmt:message key="goals.assessment" var="assessmentLabel" />
    <zynap:tabName value="Assessments" name="assessments"/>
</c:if>

    <div id="objectives_span" style="display:<c:choose><c:when test="${command.activeTab == 'objectives'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">

        <%@include file="archivedobjectives.jsp"%>

        <c:forEach var="obj" items="${command.objectives}" varStatus="indexer">

            <c:set var="objectivesMsg" value="${obj.label}" scope="request"/>
            <c:set var="objectiveIndex" value="${indexer.index}" scope="request"/>
            <c:set var="objective" value="${obj}" scope="request"/>            
            <c:import url="viewobjective.jsp"/>

        </c:forEach>
    
    </div>    

<c:if test="${command.approvedOrComplete}">
    
    <div id="assessments_span" style="display:<c:choose><c:when test="${command.activeTab == 'assessments'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:form action="" method="post" name="assFrmNme">
                        
            <c:set var="assessmentsApproved" value="true" scope="request"/>
            <c:forEach var="obj" items="${command.objectives}" varStatus="indexer">

                <c:set var="objectivesMsg" value="${obj.label}" scope="request"/>
                <c:set var="objectiveIndex" value="${indexer.index}" scope="request"/>
                <c:set var="assessment" value="${obj.assessment}" scope="request"/>
                <c:set var="manager" value="false" scope="request"/>
                <c:set var="prefix" value="objectives[${indexer.index}].assessment" scope="request"/>
                <c:set var="objective" value="${obj}" scope="request"/>
                <c:if test="${assessmentsApproved}"><c:set var="assessmentsApproved" value="${assessment.approved}" scope="request"/></c:if> 

                <zynap:infobox title="${objectivesMsg}">
                    <table class="infotable" cellpadding="0" cellspacing="0">
                        <c:import url="objectiveassessmentform.jsp"/>
                        <c:if test="${indexer.last && not assessmentsApproved}" >
                            <tr>
                                <td class="infobutton" colspan="3" align="center">
                                    <span><fmt:message key="comments.completed"/></span>
                                    <span>
                                        <spring:bind path="command.sendEmail">
                                            <input class="input_checkbox" type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.sendEmail}">checked="true"</c:if> <c:if test="${command.assessmentsApproved}"> disabled="true"</c:if> />
                                        </spring:bind>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="infobutton" colspan="3" align="center">
                                    <input class="inlinebutton" type="submit" id="finish" name="_finish" value="<fmt:message key="save"/>"/>
                                </td>
                            </tr>
                        </c:if>
                    </table>
                </zynap:infobox>
            </c:forEach>                        
        </zynap:form>
    </div>
</c:if>
</zynap:tab>