<%@ include file="../includes/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
    </zynap:actionEntry>
</zynap:actionbox>


<zynap:infobox id="viewObj" title="${title}">
    <c:set var="subject" value="${command.nodeInfo.subject}"/>
    <c:set var="organisationUnits" value="${command.nodeInfo.organisationUnits}"/>
    <c:set var="positions" value="${command.nodeInfo.positions}"/>
    <%@include file="nodeinfo.jsp"%>
</zynap:infobox>

<zynap:tab defaultTab="${command.activeTab}" tabParamName="activeTab" url="javascript">

    <fmt:message key="goals.page" var="objectiveLabel" />
    <zynap:tabName value="${objectiveLabel}" name="objectives"/>
    <c:if test="${command.approvedOrComplete}">
        <fmt:message key="goals.assessment" var="assessmentLabel" />
        <zynap:tabName value="${assessmentLabel}" name="assessments"/>
        <c:if test="${command.hasOtherAssessments}">
            <fmt:message key="other.assessments" var="assessorLabel" />
            <zynap:tabName value="${assessorLabel}" name="assessors"/>
        </c:if>
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

            <c:set var="assessmentsApproved" value="${command.assessmentsApproved}" scope="request"/>
            <c:forEach var="obj" items="${command.objectives}" varStatus="indexer">

                <c:set var="objectivesMsg" value="${obj.label}" scope="request"/>
                <c:set var="objectiveIndex" value="${indexer.index}" scope="request"/>
                <c:set var="assessment" value="${obj.assessment}" scope="request"/>
                <c:set var="manager" value="true" scope="request"/>
                <c:set var="prefix" value="objectives[${indexer.index}].assessment" scope="request"/>
                <c:set var="objective" value="${obj}" scope="request"/>

                <zynap:infobox title="${objectivesMsg}">
                    <table class="infotable" cellpadding="0" cellspacing="0">
                        <c:import url="objectiveassessmentform.jsp"/>
                        <c:if test="${indexer.last && !assessmentsApproved}" >
                            <tr>
                                <td class="infobutton" colspan="3" align="center">
                                    <span><fmt:message key="send.email"/></span>
                                    <span>
                                        <spring:bind path="command.sendEmail">
                                            <input type="checkbox" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" <c:if test="${status.value}">checked="true"</c:if> />
                                        </spring:bind>
                                    </span>
                                    <spring:bind path="command.assessmentsApproved">
                                        <input id="assessmentsApproved" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                    </spring:bind>
                                </td>
                            </tr>
                            <tr>
                                <td class="infobutton" colspan="3" align="center">
                                    <input type="hidden" id="finIdFld" name="nix" value="nix"/>
                                    <input class="inlinebutton" type="submit" id="finish" name="_finish" value="<fmt:message key="save"/>"/>

                                    <input class="inlinebutton" type="button" id="fin2" name="appAsse" value="<fmt:message key="complete.all"/>"
                                           onclick="confirmSetValueAndSubmit('assFrmNme', '<zynap:message code="confirm.complete" javaScriptEscape="true"/>', 'assessmentsApproved', true, 'finIdFld');"
                                           title="<fmt:message key="helptext.approve"/>"/>
                                </td>
                            </tr>
                        </c:if>
                    </table>
                </zynap:infobox>

            </c:forEach>

        </zynap:form>

    </div>

    <c:if test="${command.hasOtherAssessments}">

        <div id="assessors_span" style="display:<c:choose><c:when test="${command.activeTab == 'assessors'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">

            <c:set var="rowClass" value="odd"/>
            <fmt:message key="assessments" var="assessorsTitle"/>
            <zynap:infobox title="${assessorsTitle}">
                <c:url value="worklistviewobjectives.htm" var="pageUrl"><c:param name="activeTab" value="assessors"/><c:param name="command.node.id" value="${subject.id}"/></c:url>

                <fmt:message key="objective" var="headername"/>
                <fmt:message key="assessor" var="headerassessor"/>
                <fmt:message key="generic.comments" var="headercomment"/>
                <fmt:message key="generic.rating" var="headerrating"/>
                
                <display:table name="${command.assessments}" id="assessmentBean" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1" excludedParams="*">
                    <display:column property="objective.label" title="${headername}" sortable="true" headerClass="sortable" class="pager" group="0"/>
                    <display:column property="user.label" title="${headerassessor}" sortable="true" headerClass="sortable" class="pager"/>
                    <display:column property="comment" title="${headercomment}" sortable="true" headerClass="sortable" class="pager"/>
                    <display:column property="ratingValue.label" title="${headerrating}" sortable="true" headerClass="sortable" class="pager"/>
                </display:table>

            </zynap:infobox>
            
        </div>

    </c:if>

</c:if>

</zynap:tab>