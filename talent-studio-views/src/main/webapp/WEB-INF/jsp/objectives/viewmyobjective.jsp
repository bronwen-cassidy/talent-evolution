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

<c:if test="${command.approved}">
    <fmt:message key="goals.assessment" var="assessmentLabel" />
    <zynap:tabName value="Assessments" name="assessments"/>
</c:if>

    <div id="objectives_span" style="display:<c:choose><c:when test="${command.activeTab == 'objectives'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:set var="objectivesMsg" value="${command.label}" scope="request"/>
        <c:set var="objectiveIndex" value="0" scope="request"/>
        <c:set var="objective" value="${command}" scope="request"/>
        <c:import url="viewobjective.jsp"/>
    </div>

<c:if test="${command.approved}">
    <div id="assessments_span" style="display:<c:choose><c:when test="${command.activeTab == 'assessments'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <zynap:form action="" method="post" name="assFrmNme">

            <c:set var="assessment" value="${command.assessment}" scope="request"/>
            
            <zynap:infobox id="memObj" title="Actions">
                <table class="infotable" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="infolabel"><fmt:message key="comments.completed"/></td>
                        <spring:bind path="command.sendEmail">
                            <td class="infodata">
                                <input class="input_checkbox" type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.sendEmail}">checked="true"</c:if><c:if test="${assessment.approved}">disabled="true"</c:if>/>
                            </td>
                        </spring:bind>
                    </tr>
                    <tr>
                        <td class="infolabel"><fmt:message key="assessment.approved"/></td>
                        <spring:bind path="command.assessment.approved">
                            <td class="infodata">
                                <input class="input_checkbox" type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${assessment.approved}">checked="true"</c:if> disabled="true"/>
                            </td>
                        </spring:bind>
                    </tr>
                </table>
            </zynap:infobox>

            <c:set var="objectivesMsg" value="${command.label}" scope="request"/>
            <c:set var="objectiveIndex" value="0" scope="request"/>
            <c:set var="manager" value="false" scope="request"/>
            <c:set var="prefix" value="assessment" scope="request"/>
            <c:set var="objective" value="${command}" scope="request"/>
            
            <zynap:infobox title="${objectivesMsg}">
                <table class="infotable" cellpadding="0" cellspacing="0">
                    <c:import url="objectiveassessmentform.jsp"/>
                    <c:if test="${not assessment.approved}">
                        <tr>
                            <td class="infobutton" colspan="3" align="center">
                                <input class="inlinebutton" type="submit" id="finish" name="_finish" value="<fmt:message key="save"/>"/>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </zynap:infobox>
        </zynap:form>
    </div>
</c:if>
</zynap:tab>