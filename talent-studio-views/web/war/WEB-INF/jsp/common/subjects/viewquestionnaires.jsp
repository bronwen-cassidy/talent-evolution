<%@ include file="../../includes/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<%@ page import="com.zynap.talentstudio.web.workflow.WorkflowConstants"%>
<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants" %>

<script type="text/javascript">	
	$(function() 
    {     	
        $("#qustrxe12").tablesorter({widthFixed: true, widgets: ['zebra']})
        .tablesorterPager({container: $("#qustrxe12pg"), positionFixed: false, size: 5 });                 
    });    
</script>

<fmt:message key="generic.name" var="headerlabel" />
<fmt:message key="appraisal.role" var="headerrole" />
<fmt:message key="questionnaire.status" var="headerstatus" />
<fmt:message key="questionnaire.date.completed" var="headerdatecompleted" />
<fmt:message key="worklist.actions" var="actionHeader"/>

<fmt:message key="questionnaires.items" var="msg"/>
<zynap:infobox title="${msg}" id="questionnaires">
	<c:if test="${not empty appraisals}">
		<span id="qustrxe12pg" class="pagelinks">
			<form>
                <img src="../images/icons/first.png" class="first"/>
                <img src="../images/icons/prev.png" class="prev"/>
                <select class="pagesize">
                    <option value="2">2&nbsp;<fmt:message key="num.items"/></option>
                    <option value="5" selected="selected">5&nbsp;<fmt:message key="num.items"/></option>
                    <option value="10">10&nbsp;<fmt:message key="num.items"/></option>
                    <option value="15">15&nbsp;<fmt:message key="num.items"/></option>
                </select>
                <img src="../images/icons/next.png" class="next"/>
                <img src="../images/icons/last.png" class="last"/>
            </form>
            <span><fmt:message key="num.appraisal.items"><fmt:param value="${artefact.numAppraisals}"/></fmt:message></span>
		</span>
	</c:if>
    <display:table name="${appraisals}" htmlId="qustrxe12" id="questionnaire" sort="list" class="pager4" defaultsort="1">
        <display:column title="${headerlabel}" sortable="true" headerClass="sortable" class="pager4" property="label" comparator="org.displaytag.model.RowSorter"/>
         <display:column sortProperty="role.label" title="${headerrole}" sortable="true" headerClass="sortable" class="pager4">
             <c:choose>
                <c:when test="${questionnaire.managerAppraisal}">
                    <fmt:message key="worklist.appraisal.role.manager"/>
                </c:when>
                <c:otherwise>
                    <c:out value="${questionnaire.role.label}"/>
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator" property="completedDate" title="${headerdatecompleted}" sortable="true" headerClass="sortable" class="pager4"/>
        <display:column title="${actionHeader}" sortable="false" class="pager4" headerClass="sorted">
            <c:if test="${questionnaire.viewable}">
                <zynap:artefactLink var="viewQuestionnaireUrl" url="${questionnaireUrl}" tabName="activeTab" activeTab="${portfolioActiveTab}" >
                    <zynap:param name="_parameter_save_command_.activeSearchTab" value="browse"/>
                    <zynap:param name="command.node.id" value="${artefact.id}"/>
                    <zynap:param name="<%= ParameterConstants.QUESTIONNAIRE_ID%>" value="${questionnaire.id}"/>
                    <zynap:param name="<%= WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%>" value="${questionnaire.workflowId}"/>
                    <zynap:param name="myPortfolio" value="${myPortfolio}"/>
                </zynap:artefactLink>
                <a href="<c:out value="${viewQuestionnaireUrl}"/>"><fmt:message key="view"/></a>
            </c:if>
        </display:column>
    </display:table>

</zynap:infobox>

<fmt:message key="worklist.questionnaire.list" var="queMsg"/>
<zynap:infobox title="${queMsg}" id="questionnaires">

<c:if test="${empty infoforms}">
    <span class="noinfo" id="pg_count"><fmt:message key="nothing.found"/></span>
</c:if>

<div id="parentDvContainer">

    <c:forEach var="entry" items="${infoforms}" varStatus="ind">
    
        <c:set var="groupMsg" value="${entry.key.groupName}"/>
        <c:if test="${groupMsg == 'default.questionnaire.group'}">
            <fmt:message key="default.questionnaire.group" var="groupMsg"/>
        </c:if>

        <c:set var="tbleId" value="quest${ind.index}"/>
        <c:set var="grpId" value="queGrp${ind.index}"/>
		<c:set var="pgId" value="pgr${ind.index}"/>
		<script type="text/javascript">	
			$(function() 
		    {     	
		        $("#<%=pageContext.getAttribute("grpId")%>").tablesorter({widthFixed: true, widgets: ['zebra']})
		        .tablesorterPager({container: $("#<%=pageContext.getAttribute("pgId")%>"), positionFixed: false, size: 5 });                 
		    });    
		</script>
        <div id="parentQueContainer<c:out value="${ind.index}"/>" class="bordered">
            <div id="btnControls<c:out value="${grpId}"/>" class="table_infobox">
                <span class="top_buttons"><c:out value="${groupMsg}"/>&nbsp;&nbsp;(<c:out value="${entry.key.numValues}"/>)&nbsp;<fmt:message key="items"/></span>
                <span id="expandTble<c:out value="${grpId}"/>" class="controls closeButton closed"><img src="../images/plus.gif" width="16px" height="16px" alt="maximize" onclick="showTable('aa<c:out value="${grpId}"/>', 'expandTble<c:out value="${grpId}"/>', 'minimizeTble<c:out value="${grpId}"/>');"/></span>
                <span id="minimizeTble<c:out value="${grpId}"/>" class="controls closeButton open"><img src="../images/minimize.gif" width="16px" height="16px" alt="mininimize" onclick="hideTable('aa<c:out value="${grpId}"/>', 'expandTble<c:out value="${grpId}"/>', 'minimizeTble<c:out value="${grpId}"/>');"/></span>
            </div>
            <span id="<c:out value="${pgId}"/>" class="pagelinks">
				<form>
                    <img src="../images/icons/first.png" class="first"/>
                    <img src="../images/icons/prev.png" class="prev"/>
                    <select class="pagesize">
                        <option value="2">2&nbsp;<fmt:message key="num.items"/></option>
                        <option value="5" selected="selected">5&nbsp;<fmt:message key="num.items"/></option>
                        <option value="10">10&nbsp;<fmt:message key="num.items"/></option>
                        <option value="15">15&nbsp;<fmt:message key="num.items"/></option>
                    </select>
                    <img src="../images/icons/next.png" class="next"/>
                    <img src="../images/icons/last.png" class="last"/>
                </form>                
			</span>
            <div id="aa<c:out value="${grpId}"/>" class="open">            	
                <display:table name="${entry.value}" htmlId="${grpId}" uid="${tbleId}" id="questionnairedto" sort="list" class="pager4" defaultsort="1" excludedParams="*">
                    <display:column title="${headerlabel}" sortable="true" headerClass="sortable" class="pager4" property="label" comparator="org.displaytag.model.RowSorter"/>
                    <display:column title="${headerstatus}" sortable="true" headerClass="sortable" class="pager4">
                        <fmt:message key="questionnaire.${questionnairedto.progress}"/>
                    </display:column>
                    <display:column decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator" property="completedDate" title="${headerdatecompleted}" sortable="true" headerClass="sortable" class="pager4"/>
                    <display:column title="${actionHeader}" sortable="false" class="pager4" headerClass="sorted">

                        <c:if test="${questionnairedto.viewable}">
                            <zynap:artefactLink var="viewQuestionnaireUrl" url="${questionnaireUrl}" tabName="activeTab" activeTab="${portfolioActiveTab}" >
                                <zynap:param name="_parameter_save_command_.activeSearchTab" value="browse"/>
                                <zynap:param name="command.node.id" value="${artefact.id}"/>
                                <zynap:param name="<%= ParameterConstants.QUESTIONNAIRE_ID%>" value="${questionnairedto.id}"/>
                                <zynap:param name="<%= WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%>" value="${questionnairedto.workflowId}"/>
                                <zynap:param name="myPortfolio" value="${myPortfolio}"/>
                            </zynap:artefactLink>
                            <a href="<c:out value="${viewQuestionnaireUrl}"/>"><fmt:message key="view"/></a>
                        </c:if>

                        <c:if test="${questionnairedto.editable}">
                            <c:set var="locEditable" value="false"/>
                            <c:if test ="${myPortfolio}"><c:set var="locEditable" value="${questionnairedto.individualWrite}"/></c:if>
                            <c:if test="${!myPortfolio}"><c:set var="locEditable" value="${questionnairedto.managerWrite}"/></c:if>
                            <c:if test="${locEditable}">
                                <zynap:artefactLink var="editQuestionnaireUrl" url="${editUrl}" tabName="activeTab" activeTab="${portfolioActiveTab}" >
                                    <zynap:param name="_parameter_save_command_.activeSearchTab" value="browse"/>
                                    <zynap:param name="command.node.id" value="${artefact.id}"/>
                                    <zynap:param name="<%= com.zynap.talentstudio.web.common.ParameterConstants.QUESTIONNAIRE_ID%>" value="${questionnairedto.id}"/>
                                    <zynap:param name="<%= WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX%>" value="${questionnairedto.workflowId}"/>
                                    <zynap:param name="myPortfolio" value="${myPortfolio}"/>
                                </zynap:artefactLink>
                                &nbsp; | &nbsp;<a href="<c:out value="${editQuestionnaireUrl}"/>"><fmt:message key="edit"/></a>
                            </c:if>
                        </c:if>
                    </display:column>
                </display:table>
            </div>
        </div>                
    </c:forEach>
</div>
</zynap:infobox>

<script defer="defer" type="text/javascript">
    checkHiddenDivs('parentDvContainer', 'btnControls', 'aa');
</script>
