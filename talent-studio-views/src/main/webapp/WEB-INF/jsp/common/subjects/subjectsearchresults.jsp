<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants, com.zynap.talentstudio.web.history.HistoryHelper"%>
<%@ page import="com.zynap.talentstudio.web.workflow.WorkflowConstants" %>

<fmt:message key="subject.last.names" var="headerlastname" />
<fmt:message key="subject.first.names" var="headerfirstname" />
<fmt:message key="subject.can.login" var="headerlogsin" />
<fmt:message key="generic.active" var="headeractive" />
<fmt:message key="worklist.actions" var="actionHeader"/>
<fmt:message key="current.job" var="headercurrentjob"/>

<fmt:message key="searchresults.subject" var="msg"/>

<zynap:infobox title="${msg}" id="results">
    <c:if test="${subjects != null}">

        <zynap:artefactLink var="viewPageUrl" url="${viewListPageUrl}" tabName="activeTab" activeTab="${command.activeTab}" commandAction="<%=ParameterConstants.UPDATE_COMMAND%>">
            <zynap:param name="_parameter_save_command_.activeSearchTab" value="${activeSearchTab}"/>

            <%-- parameter that indicates that the user has changed page --%>
            <zynap:param name="pageChange" value="true"/>

            <%-- page number parameter required to maintain correct page for display tag --%>
            <c:set var="pageNumberParameter" value="${command.pageNumberParameter}"/>
            <c:if test="${pageNumberParameter != null}">
                <zynap:param name="${pageNumberParameter.key}" value="${pageNumberParameter.value}"/>
            </c:if>
        </zynap:artefactLink>
        <display:table name="${subjects}" id="subjecttable" sort="list" pagesize="25" requestURI="${viewPageUrl}" class="pager" excludedParams="*" defaultsort="1">

            <display:column title="${headerlastname}" sortable="true" headerClass="sortable" class="pager" sortProperty="secondName">
                <a href="#" onclick="javascript:setHiddenFromList(<c:out value="${subjecttable.id}"/>, 'hidden_node_id_results', 'navigationResults');"><c:out value="${subjecttable.secondName}"/></a>
            </display:column>

            <display:column property="firstName" title="${headerfirstname}" sortable="true" headerClass="sortable" class="pager"/>

            <display:column property="currentJobInfo" title="${headercurrentjob}" sortable="true" headerClass="sortable" class="pager"/>

            <display:column title="${headerlogsin}" sortable="true" headerClass="sortable" class="pager">
                <c:choose>
                    <c:when test="${subjecttable.canLogIn}"><fmt:message key="true"/></c:when>
                    <c:otherwise><fmt:message key="false"/></c:otherwise>
                </c:choose>
            </display:column>

            <c:if test="${hasCustomColumn}">
                <display:column property="id" title="${attrLabel}" sortable="false" headerClass="sorted" class="pager" decorator="com.zynap.talentstudio.web.utils.displaytag.MyTeamColumnDecorator"/>                   
            </c:if>

            <c:if test="${command.filter.questionnaireId != null}">
                <%-- this column displays the edit link for the questionnaires --%>                
                <display:column title="${actionHeader}" sortable="false" class="pager" headerClass="sorted">
                     <zynap:artefactLink var="editQuestionnaireUrl" url="editsubjectquestionnaire.htm" tabName="activeTab" activeTab="${tabContent.key}" >

                        <zynap:param name="_parameter_save_command_.activeSearchTab" value="${activeSearchTab}"/>
                        <zynap:param name="_parameter_save_command_.activeTab" value="${command.activeTab}"/>
                        <zynap:param name="command.node.id" value="${command.nodeId}"/>
                        <zynap:param name="<%= WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%>" value="${command.filter.questionnaireId}"/>
                        <zynap:param name="<%= ParameterConstants.NODE_ID_PARAM%>" value= "${subjecttable.id}" />
                        <zynap:param name="myPortfolio" value="false"/>
                    </zynap:artefactLink>

                    <zynap:artefactLink var="viewQuestionnaireUrl" url="viewsubjectquestionnaire.htm" tabName="activeTab" activeTab="${tabContent.key}" >
                        <zynap:param name="_parameter_save_command_.activeSearchTab" value="${activeSearchTab}"/>
                        <zynap:param name="_parameter_save_command_.activeTab" value="${command.activeTab}"/>
                        <zynap:param name="command.node.id" value="${command.nodeId}"/>
                        <zynap:param name="<%= WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%>" value="${command.filter.questionnaireId}"/>
                        <zynap:param name="<%= ParameterConstants.NODE_ID_PARAM%>" value= "${subjecttable.id}" />
                        <zynap:param name="myPortfolio" value="false"/>
                    </zynap:artefactLink>

                    <a href="<c:out value="${editQuestionnaireUrl}"/>"><fmt:message key="edit.questionnaire"/></a>&nbsp;|&nbsp;
                    <a href="<c:out value="${viewQuestionnaireUrl}"/>"><fmt:message key="view.questionnaire"/></a>
                    
                </display:column>
            </c:if>

        </display:table>

        <zynap:form  method="post" name="navigationResults" >
            <input id="hidden_node_id_results" type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>"/>
            <input id="nodeTarget_results" type="hidden" name="nodeTarget" value=""/>

            <%-- page number parameter required to maintain correct page for display tag --%>
            <c:set var="pageNumberParameter" value="${command.pageNumberParameter}"/>
            <c:if test="${pageNumberParameter != null}">
                <input id="pageNumber_results" type="hidden" name="<c:out value="${pageNumberParameter.key}"/>" value="<c:out value="${pageNumberParameter.value}"/>"/>
            </c:if>
        </zynap:form>

    </c:if>
</zynap:infobox>

