<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants" %>
<%@ page import="com.zynap.talentstudio.web.questionnaires.definition.ViewQuestionnaireDefinitionController" %>
<%@ page import="com.zynap.talentstudio.web.utils.ZynapWebUtils" %>

<zynap:historyLink var="pageUrl" url="${request.requestURI}">
    <zynap:param name="<%=ParameterConstants.ACTIVE_TAB%>" value="<%=ViewQuestionnaireDefinitionController.QUESTIONNAIRES_TAB%>"/>
</zynap:historyLink>

<zynap:historyLink var="viewUrl" url="viewquestionnaireworkflow.htm">
    <zynap:param name="<%=ParameterConstants.ACTIVE_TAB%>" value="<%=ViewQuestionnaireDefinitionController.QUESTIONNAIRES_TAB%>"/>
</zynap:historyLink>

<zynap:saveUrl url="${pageUrl}"/>

<zynap:actionbox id="qnairActions">
    <zynap:actionEntry>
        <zynap:form method="get" name="_add" action="/admin/addquestionnairewf.htm">
            <input class="actionbutton" type="submit" name="_add" value="<fmt:message key="add"/>"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="generic.name" var="headername"/>
<fmt:message key="questionnaire.status" var="headerstatus"/>
<fmt:message key="questionnaire.start.date" var="headerstartdate"/>
<fmt:message key="questionnaire.end.date" var="headerenddate"/>
<fmt:message key="worklist.actions" var="headeraction"/>
<fmt:message key="worklist.action.results" var="headerresults"/>
<fmt:message key="generic.group" var="headergroup"/>
<fmt:message key="report.population" var="headerpopulation"/>
<fmt:message key="questionnaire.headerrepublisheddate" var="headerrepublisheddate"/>

<fmt:message key="qdef.questionnaires" var="msg"/>
<zynap:infobox title="${msg}" id="listWF">

    <display:table name="${command.questionnaires}" id="que" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">
        <display:column property="label" title="${headername}" href="${viewUrl}"
                        paramId="<%=ParameterConstants.QUESTIONNAIRE_ID%>" paramProperty="id" sortable="true"
                        headerClass="sortable" class="pager"/>
        <display:column sortProperty="groupLabel" title="${headergroup}" headerClass="sortable" sortable="true" class="pager">
            <c:choose><c:when test="${que.groupLabel == null}"><fmt:message key="none"/></c:when><c:otherwise><c:out value="${que.groupLabel}"/></c:otherwise></c:choose>
        </display:column>

        <display:column property="population" title="${headerpopulation}" sortable="true" headerClass="sortable" class="pager"/>
        
        <display:column decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator" property="expiryDate" title="${headerenddate}" sortable="true" headerClass="sortable" class="pager"/>
        <display:column decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator" property="lastRepublishedDate" title="${headerrepublisheddate}" sortable="true" headerClass="sortable" class="pager"/>
        <display:column title="${headerstatus}" headerClass="sortable" sortable="true" class="pager">
            <fmt:message key="${que.status}"/>
        </display:column>

        <display:column title="${headeraction}" class="pager" headerClass="sorted">

            <zynap:historyLink var="editqueurl" url="editquestionnairewf.htm">
                <zynap:param name="<%= ParameterConstants.QUESTIONNAIRE_ID%>" value="${que.id}"/>
            </zynap:historyLink>
            
            <span style="white-space: nowrap;">
                <input type="button" value="<fmt:message key="edit"/>" onclick="location.href='<c:out value="${editqueurl}"/>'" />
                <c:if test="${que.infoForm && !que.completed}">
                    &nbsp;|&nbsp;<input type="button" value="<fmt:message key="republish"/>"
                           onclick="republishWorkflow('<c:out value="${que.id}"/>', '<%= ZynapWebUtils.getUserId(request)%>', '<zynap:message code="republishing" javaScriptEscape="true"/>',
                                '<zynap:message code="republished.at" javaScriptEscape="true"/>', '<zynap:message code="users.added" javaScriptEscape="true"/>',
                                '<zynap:message code="users.removed" javaScriptEscape="true"/>');"/>
                </c:if>
            </span>
        </display:column>

        <display:column title="${headerresults}" class="pager" headerClass="sorted">
            <c:if test="${que.infoForm && !que.completed}">
                <div id="republishSummary_<c:out value="${que.id}"/>"></div>
            </c:if>
        </display:column>
    </display:table>
</zynap:infobox>
