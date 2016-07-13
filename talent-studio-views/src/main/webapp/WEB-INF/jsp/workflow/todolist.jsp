<%@ page import="com.zynap.talentstudio.web.workflow.WorkflowConstants"%>
<%@ page import="com.zynap.talentstudio.web.workflow.WorklistController" %>
<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:infobox title="${listTabLabel}" id="worklist">

    <spring:bind path="command">
        <%@ include file="../includes/error_messages.jsp"%>
    </spring:bind>

    <c:if test="${command.notificationList != null}">

        <c:url var="viewListPageUrl" value="${request.requestURI}"><c:param name="_pageNum" value="0"/></c:url>
        <zynap:artefactLink var="viewPageUrl" url="${viewListPageUrl}" tabName="activeTab" activeTab="${command.activeTab}" commandAction="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>"/>

        <fmt:message key="worklist.task" var="headertask" />
        <fmt:message key="appraisal.role" var="headerrole" />
        <fmt:message key="worklist.appraisal.name" var="headername"/>
        <fmt:message key="worklist.evaluatee" var="headerevaluatee" />
        <fmt:message key="worklist.actions" var="headeractions" />
        <fmt:message key="worklist.duedate" var="headerduedate" />
        <fmt:message key="close.warning" var="closewarning" />
        <fmt:message key="approve.warning" var="approvewarning" />
        <fmt:message key="verify.warning" var="verifywarning" />
        <zynap:message code="complete.warning" var="completewarning" javaScriptEscape="true"/>

        <display:table name="${command.notificationList}" id="notification" sort="list" defaultsort="2"  pagesize="15" requestURI="${viewPageUrl}" class="pager" excludedParams="*">

            <c:set var="hasNoActions" value="${notification.action == 'CLOSE'}"/>

            <c:choose>
                <c:when test="${command.performanceReview && notification.target == '5'}">
                    <%-- if target is 5 this is a review that needs roles assigning --%>
                    <c:choose>
                        <c:when test="${notification.actionable}">
                            <fmt:message key="worklist.appraisal.start" var="label"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="worklist.appraisal.assign.roles" var="label"/>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${notification.actionable}">
                            <%-- handles the case when notification indicates the process has completed --%>
                            <c:choose>
                                <c:when test="${hasNoActions}"><fmt:message key="close" var="label"/></c:when>
                                <c:otherwise><fmt:message key="worklist.complete" var="label"/></c:otherwise>
                            </c:choose>
                        </c:when>
                        <%-- not yet actionable we therefore answer it --%>
                        <c:otherwise>
                            <fmt:message key="worklist.appraisal.answer.invitation" var="label"/>
                        </c:otherwise>
                     </c:choose>
                </c:otherwise>
            </c:choose>

            <%-- display the questionnaires name --%>
            <display:column title="${headername}" property="workflowName" sortable="true" headerClass="sortable" class="pager" comparator="org.displaytag.model.RowSorter"/>
            <%-- display any performance review dependant stuff --%>
            <c:if test="${command.performanceReview}">
                <display:column title="${headerrole}" property="roleName" sortable="true" headerClass="sortable" class="pager" sortProperty="roleName" comparator="org.displaytag.model.RowSorter"/>
                <display:column property="subjectName" title="${headerevaluatee}" sortable="true" headerClass="sortable" class="pager"/>
            </c:if>

            <display:column decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator" property="dueDate" title="${headerduedate}" sortable="true" headerClass="sortable" class="pager"/>

            <display:column title="${headeractions}" sortable="true" headerClass="sortable" class="pager" sortProperty="action">

                <form method="post" action="" name="notifUrl<c:out value="${notification.id}"/>" >
                    <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.UPDATE_COMMAND%>"/>
                    <input type="hidden" name="_target<c:out value="${notification.target}"/>" value="<c:out value="${notification.target}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.NOTIFICATION_ID_PARAM%>" value="<c:out value="${notification.id}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.workflowId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.SUBJECT_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.subjectId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.ROLE_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.roleId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.APPRAISAL_ID%><c:out value="${notification.id}"/>" value="<c:out value="${notification.performanceReviewId}"/>"/>
                </form>
                <form method="post" action="" name="viewQuestionnaireUrl<c:out value="${notification.id}"/>" >
                    <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.UPDATE_COMMAND%>"/>
                    <input type="hidden" name="_target6" value="6"/>
                    <input type="hidden" name="<%=WorkflowConstants.NOTIFICATION_ID_PARAM%>" value="<c:out value="${notification.id}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.workflowId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.SUBJECT_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.subjectId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.ROLE_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.roleId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.APPRAISAL_ID%><c:out value="${notification.id}"/>" value="<c:out value="${notification.performanceReviewId}"/>"/>
                </form>
                <form method="post" action="" name="notificationForm<c:out value="${notification.id}"/>" >
                    <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.UPDATE_COMMAND%>"/>
                    <input type="hidden" name="<%=WorkflowConstants.NOTIFICATION_ID_PARAM%>" value="<c:out value="${notification.id}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.workflowId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.SUBJECT_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.subjectId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.APPRAISAL_ID%><c:out value="${notification.id}"/>" value="<c:out value="${notification.performanceReviewId}"/>"/>
                    <input type="hidden" name="_target1" value="<%=WorklistController.RESPOND_NOTIFICATION%>"/>                    
                </form>
                <form method="post" action="" name="approveApraisal<c:out value="${notification.id}"/>" >
                    <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.UPDATE_COMMAND%>"/>
                    <input type="hidden" name="_target7" value="7"/>
                    <input type="hidden" name="<%=WorkflowConstants.NOTIFICATION_ID_PARAM%>" value="<c:out value="${notification.id}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.workflowId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.SUBJECT_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.subjectId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.ROLE_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.roleId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.APPRAISAL_ID%><c:out value="${notification.id}"/>" value="<c:out value="${notification.performanceReviewId}"/>"/>
                </form>
                <form method="post" action="" name="verifyApraisal<c:out value="${notification.id}"/>" >
                    <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.UPDATE_COMMAND%>"/>
                    <input type="hidden" name="_target8" value="8"/>
                    <input type="hidden" name="<%=WorkflowConstants.NOTIFICATION_ID_PARAM%>" value="<c:out value="${notification.id}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.workflowId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.SUBJECT_ID_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.subjectId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.ROLE_PARAM_PREFIX%><c:out value="${notification.id}"/>" value="<c:out value="${notification.roleId}"/>"/>
                    <input type="hidden" name="<%=WorkflowConstants.APPRAISAL_ID%><c:out value="${notification.id}"/>" value="<c:out value="${notification.performanceReviewId}"/>"/>
                </form>
                <c:choose>
                    <c:when test="${notification.action != 'CLOSE'}">
                        <c:choose>
                            <%-- target 5 this is an appraisal role management --%>
                            <c:when test="${notification.target == '5'}">
                                <%--show the action task link --%>
                                <c:choose>
                                    <c:when test="${notification.actionable}">
                                        <%-- start the process or edit the roles --%>
                                        <a href="javascript:respondNotification('notificationForm<c:out value="${notification.id}"/>');"><c:out value="${label}"/></a>
                                        &nbsp;|&nbsp;<a href="javascript:postQuestionnaireTarget('notifUrl<c:out value="${notification.id}"/>');"><fmt:message key="worklist.appraisal.edit.roles"/></a>
                                    </c:when>                                    
                                    <c:otherwise>
                                        <%-- assign roles option --%>
                                        <a href="javascript:postQuestionnaireTarget('notifUrl<c:out value="${notification.id}"/>');"><c:out value="${label}"/></a>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${notification.action == 'AWAITING_APPROVAL'}">
                                <%-- view a readonly version of the review with an approved checkbox only on the form  --%>
                                <%-- edit the review, questionnaire --%>
                                <%-- edit the review, questionnaire --%>
                                <a href="javascript:postQuestionnaireTarget('<c:out value="notifUrl${notification.id}"/>');"><fmt:message key="worklist.edit"/></a>
                                &nbsp;|&nbsp;<fmt:message key="worklist.appraisal.awaiting.approval"/>
                            </c:when>
                            <c:when test="${notification.action == 'APPROVE'}">
                                <%-- view a readonly version of the review with an approved checkbox only on the form  --%>
                                <%-- edit the review, questionnaire --%>
                                <a href="javascript:postQuestionnaireTarget('<c:out value="viewQuestionnaireUrl${notification.id}"/>');"><fmt:message key="worklist.view"/></a>
                                &nbsp;|&nbsp;<a href="javascript:respondNotificationWarning('approveApraisal<c:out value="${notification.id}"/>', '<c:out value="${approvewarning}"/>');"><fmt:message key="worklist.approval.invitation"/></a>
                            </c:when>
                            <c:when test="${notification.action == 'VERIFY'}">
                                <%-- view a readonly version of the review with an approved checkbox only on the form  --%>
                                <a href="javascript:postQuestionnaireTarget('<c:out value="verifyApraisal${notification.id}"/>');"><fmt:message key="worklist.view"/></a>
                                &nbsp;|&nbsp;<a href="javascript:respondNotificationWarning('verifyApraisal<c:out value="${notification.id}"/>', '<c:out value="${verifywarning}"/>');"><fmt:message key="worklist.verify.invitation"/> </a>
                            </c:when>
                            <c:otherwise>
                                <%-- we have answer, complete, edit for the questionnaire or the appraisal --%>
                                <%-- the process has completed and all we need to do is wait for approval, we may still edit though --%>
                                <c:if test="${notification.actionable}">
                                    <c:choose>
                                        <c:when test="${notification.action == 'ANSWER'}">
                                            <%-- answer the review questionnaire --%>
                                            <a href="javascript:respondNotification('notifUrl<c:out value="${notification.id}"/>');"><fmt:message key="worklist.appraisal.answer.invitation"/> </a>
                                        </c:when>
                                        <c:otherwise>
                                            <%-- edit the review, questionnaire --%>
                                            <a href="javascript:postQuestionnaireTarget('<c:out value="notifUrl${notification.id}"/>');"><fmt:message key="worklist.edit"/></a>
                                            <%-- complete the questionnaire --%>
                                            &nbsp;|&nbsp;<a href="javascript:respondNotificationWarning('<c:out value="notificationForm${notification.id}"/>', '<c:out value="${completewarning}"/>');"><c:out value="${label}"/></a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>

                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <%-- handles the case when notification indicates the process has completed only get this if you have launched it--%>
                        <a href="javascript:respondNotificationWarning('notificationForm<c:out value="${notification.id}"/>', '<c:out value="${closewarning}"/>');"><c:out value="${label}"/></a>
                    </c:otherwise>
                </c:choose>
            </display:column>
        </display:table>
    </c:if>
</zynap:infobox>
