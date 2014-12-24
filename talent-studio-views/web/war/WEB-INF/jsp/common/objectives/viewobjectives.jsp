<%@ page import="com.zynap.talentstudio.objectives.ObjectiveConstants" %>
<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>

<zynap:artefactLink var="pageUrl" url="${request.requestURI}" tabName="activeTab" activeTab="${activeTab}"
    commandAction="<%=ParameterConstants.UPDATE_COMMAND%>">
    <c:if test="${viewPageNum != null}"><zynap:param name="_pageNum" value="${viewPageNum}"/></c:if>
</zynap:artefactLink>

<zynap:artefactLink var="viewUrl" url="${baseViewUrl}" tabName="activeTab" activeTab="${activeTab}" >
    <zynap:param name="_parameter_save_command_.activeSearchTab" value="browse"/>
    <zynap:param name="command.node.id" value="${artefact.id}"/>
    <c:if test="${_formSubmission != null}"><zynap:param name="_formSubmission" value="${_formSubmission}"/></c:if>
    <c:if test="${_pageNum != null}"><zynap:param name="_pageNum" value="${_pageNum}"/></c:if>
</zynap:artefactLink>

<zynap:infobox id="objsinfobox" title="${boxtitle}">
    <fmt:message key="generic.label" var="headername" />
    <fmt:message key="objective.status" var="headerstatus" />    
    <fmt:message key="generic.description" var="headerdesc" />
    <fmt:message key="objective.date.approved" var="headerdateapp" />
    <fmt:message key="objective.dateupdated" var="headerdateupdated" />
    <fmt:message key="objective.updatedby" var="headerupdatedby" />
    <fmt:message key="objective.basedOn" var="headerbasedon" />
    <fmt:message key="manager.comments" var="comments" />

    <display:table name="${objectives}" id="objslist" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" excludedParams="*" defaultsort="1" htmlId="xyz903">

        <c:choose>
            <c:when test="${callJavascript != null}">
                <display:column title="${headername}" sortable="true" headerClass="sortable" class="pager">
                    <a href="javascript:submitViewObjective('<c:out value="${objslist.id}"/>');"><c:out value="${objslist.label}"/></a>
                </display:column>
            </c:when>
            <c:otherwise>
                <display:column property="label" title="${headername}" href="${viewUrl}" paramId="<%=ObjectiveConstants.OBJECTIVE_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
            </c:otherwise>
        </c:choose>
        
        <display:column title="${headerbasedon}" headerClass="sortable" sortable="true" class="pager" property="parent.label"/>
        <display:column title="${headerdateapp}" headerClass="sortable" sortable="true" class="pager" property="dateApproved" decorator="com.zynap.talentstudio.web.utils.displaytag.DateColumnDecorator"/>
        <display:column title="${headerdateupdated}" headerClass="sortable" sortable="true" class="pager" property="dateUpdated" decorator="com.zynap.talentstudio.web.utils.displaytag.DateColumnDecorator"/>
        <display:column title="${headerupdatedby}" headerClass="sortable" sortable="true" class="pager" property="updatedBy.label"/>

        <display:column title="${headerstatus}" headerClass="sortable" sortable="true" class="pager" sortProperty="status">
            <fmt:message key="${objslist.status}"/>
        </display:column>

        <display:column property="description" title="${headerdesc}" headerClass="sortable" sortable="true" class="pager"/>               
        <display:column property="comments" title="${comments}" headerClass="sortable" sortable="true" class="pager"/>

     </display:table>

</zynap:infobox>