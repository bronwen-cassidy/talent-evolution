<%@ page import="com.zynap.talentstudio.web.workflow.WorkflowConstants" %>
<%@ page import="com.zynap.talentstudio.web.workflow.WorklistController" %>
<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@include file="../questionnaires/questionnairepopupinclude.jsp"%>
<c:if test="${command.performanceRoles != null}">
    <zynap:window elementId="subjectUserTree" src="../picker/subjectuserpicker.htm"/>
</c:if>

<zynap:tab defaultTab="${command.activeTab}" id="qd_info" url="javascript" tabParamName="activeTab">

   <c:choose>
     <c:when test="${command.performanceReview}">
       <fmt:message key="appraisal.list" var="listTabLabel" scope="request"/>
     </c:when>
     <c:otherwise>
        <fmt:message key="worklist.questionnaire.list" var="listTabLabel" scope="request"/>
     </c:otherwise>
   </c:choose>

    <zynap:tabName value="${listTabLabel}" name="worklist"  />

    <c:if test="${command.questionnaire != null}">
        <c:set var="questionnaireLabel" value="${command.questionnaireLabel}" scope="request"/>
        <zynap:tabName value="${questionnaireLabel}" name="questionnaire"/>

        <c:if test="${command.performanceReview && command.managerEvaluation}">
            <fmt:message key="appraisal.review" var="reviewTabLabel">
                <fmt:param value="${questionnaireLabel}"/>
            </fmt:message>

            <zynap:tabName value="${reviewTabLabel}" name="review"/>

            <!-- tab for the objectives of the evaluatee -->
            <fmt:message key="objectives" var="objectivesLabel"/>
            <zynap:tabName name="objectives" value="${objectivesLabel}"/>

            <c:if test="${command.objective != null}">
                <zynap:tabName name="objectiveinfo" value="${command.objective.label}"/>
            </c:if>
        </c:if>

    </c:if>

    <c:if test="${command.performanceRoles != null}">
        <fmt:message key="appraisal.roles" var="tabLabel" />
        <zynap:tabName value="${tabLabel}" name="roles"/>
    </c:if>

  
    <div id="worklist_span" style="display:<c:choose><c:when test="${command.activeTab == 'worklist'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:import url="../workflow/todolist.jsp"/>
    </div>


    <c:if test="${command.questionnaire != null}">
        <div id="questionnaire_span" style="display:<c:choose><c:when test="${command.activeTab == 'questionnaire'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <c:set var="questionnaireGroups" value="${command.questionnaireGroups}" scope="request"/>
            <c:set var="previewMode" value="false" scope="request"/>
            <c:set var="managerAccessView" value="${command.performanceReview && command.managerEvaluation}" scope="request"/>
            <c:choose>
                <c:when test="${command.saved}">
                    <zynap:actionbox id="answerqbox">
                        <zynap:actionEntry>
                            <form action="" method="post" name="qclose">
                                <input type="hidden" name="_target4" value="4"/>
                                <input class="actionbutton" type="button" value="<fmt:message key="close"/>" onclick="document.forms.qclose.submit();"/>
                            </form>
                        </zynap:actionEntry>

                        <zynap:actionEntry>
                            <form action="" method="post" name="qedit">
                                <input type="hidden" name="_target2" value="2"/>
                                <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.SAVE_COMMAND%>"/>
                                <input type="hidden" name="<%=WorkflowConstants.NOTIFICATION_ID_PARAM%>" value="<c:out value="${command.notificationId}"/>"/>
                                <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%><c:out value="${command.notificationId}"/>" value="<c:out value="${command.workflowId}"/>"/>
                                <input type="hidden" name="<%=WorkflowConstants.SUBJECT_ID_PARAM_PREFIX%><c:out value="${command.notificationId}"/>" value="<c:out value="${command.subjectId}"/>"/>
                                <input type="hidden" name="<%=WorkflowConstants.ROLE_PARAM_PREFIX%><c:out value="${command.notificationId}"/>" value="<c:out value="${command.role}"/>"/>
                                <input class="actionbutton" type="button" value="<fmt:message key="edit"/>" onclick="document.forms.qedit.submit();"/>
                            </form>
                            <%-- form used to review evaluators answers when the questionnaire is in view mode --%>
                            <form action="" method="post" name="questionnaireForm">
                                <input id="actId" type="hidden" name="action" value=""/>
                                <input id="tarId" type="hidden" name="" value=""/>
                                <input id="selGroupId" type="hidden" name="<%=WorklistController.SELECTED_GROUP_PARAM%>" value=""/>
                            </form>
                        </zynap:actionEntry>
                        <zynap:actionEntry>
                            <%@ include file="../questionnaires/view/pdfexportmenusnippet.jsp" %>
                        </zynap:actionEntry>

                    </zynap:actionbox>
                    <%@ include file="../questionnaires/view/viewquestionnairesnippet.jsp" %>
                </c:when>
                <c:otherwise>
                    <c:import url="../questionnaires/answer/questionnaireform.jsp"/>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>


   

    <c:if test="${managerAccessView}">
        <div id="review_span" style="display:<c:choose><c:when test="${command.activeTab == 'review'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <c:import url="../performance/reviewevaluatoranswers.jsp"/>
        </div>

        <!-- set the variables for the objectives view -->
        <c:set var="artefact" value="${command.artefact}" scope="request"/>
        <c:set var="displayConfigView" value="${command.displayConfigView}" scope="request"/>
        <c:set var="imageUrl" value="/image/viewsubjectimage.htm" scope="request"/>
        <c:set var="checkNodeAccess" value="true" scope="request"/>
        <c:set var="perfManager" value="${command.performanceReview && command.managerEvaluation}" scope="request"/>

        <div id="objectives_span" style="display:<c:choose><c:when test="${command.activeTab == 'objectives'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <c:import url="../performance/viewevaluateeobjectives.jsp"/>
        </div>

        <c:if test="${command.objective != null}">
            <div id="objectiveinfo_span" style="display:<c:choose><c:when test="${command.activeTab == 'objectiveinfo'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
                <c:set var="objective" value="${command.objective}" scope="request"/>
                <c:set var="attributes" value="${objective.wrappedDynamicAttributes}" scope="request"/>
                <c:set var="approved" value="${objective.approvedOrComplete}" scope="request"/>
                <fmt:message key="view.objective" var="objmsg" scope="request"/>
                <zynap:infobox id="viewAch" title="${objmsg}">
                    <c:import url="../common/objectives/viewevaluateeobjective.jsp"/>
                </zynap:infobox>
            </div>
        </c:if>
    </c:if>

    <c:if test="${command.performanceRoles != null}">
        <div id="roles_span" style="display:<c:choose><c:when test="${command.activeTab == 'roles'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
            <c:import url="../performance/rolesform.jsp"/>
        </div>
    </c:if>


</zynap:tab>


