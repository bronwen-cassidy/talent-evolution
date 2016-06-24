<%@ page import="com.zynap.talentstudio.web.perfomance.PerformanceReviewMultiController" %>
<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="bck" method="get" action="listperformancereviews.htm">
            <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="document.forms.bck.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form name="dlt" method="get" action="deleteperformancereview.htm">
            <input type="hidden" name="<%=PerformanceReviewMultiController.REVIEW_ID%>" value="<c:out value="${command.id}"/>"/>
            <input class="actionbutton" type="button" name="_delete" value="<fmt:message key="delete"/>" onclick="document.forms.dlt.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <c:if test="${command.status != 'COMPLETED'}">
        <zynap:actionEntry>
            <zynap:message var="completeName" code="confirm.complete.review"/>
            <form name="cperfrev" method="get" action="viewcompleteperformancereview.htm">
                <input type="hidden" name="<%=PerformanceReviewMultiController.REVIEW_ID%>" value="<c:out value="${command.id}"/>"/>
                <input class="actionbutton" type="button" name="arcve" value="<fmt:message key="complete"/>" onclick="confirmActionAndPost('cperfrev', '<c:out value="${completeName}"/>')"/>
            </form>
        </zynap:actionEntry>
    </c:if>
</zynap:actionbox>

<c:set var="managerWorkflow" value="${command.managerWorkflow}"/>
<c:set var="evaluatorWorkflow" value="${command.evaluatorWorkflow}"/>
<c:set var="hrUser" value="${evaluatorWorkflow.hrUser}"/>

<fmt:message key="date.format" var="datePattern"/>

<zynap:infobox title="${title}" id="viewPR">
    <table class="infotable" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.label}"/></td>
        </tr>
        <tr>  `
            <td class="infolabel"><fmt:message key="questionnaire.end.date"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:formatDate value="${managerWorkflow.expiryDate}" pattern="${datePattern}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="send.email"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${command.notifiable}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.managerdefinition"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${managerWorkflow.questionnaireDefinition.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.generaldefinition"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${evaluatorWorkflow.questionnaireDefinition.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.hr"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${hrUser.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.population"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${managerWorkflow.population.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.status"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${command.status}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="user.managed.process"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${command.userManaged}"/></td>
        </tr>
    </table>

    <p/>
    <fmt:message key="appraisal.performance.review.title" var="performancetitle"/>

    <zynap:infobox title="${performancetitle}" id="viewPR">

        <c:url value="viewperformancereview.htm" var="pageUrl"><c:param name="reviewId" value="${command.id}"/></c:url>

        <fmt:message key="appraisal.evaluatee.title" var="evaluatee"/>
        <fmt:message key="appraisal.evaluator.title" var="evaluator"/>
        <fmt:message key="appraisal.role.name" var="rolename"/>
        <fmt:message key="appraisal.next.action" var="nextaction"/>
        <fmt:message key="appraisal.status.action" var="statusaction"/>
        <fmt:message key="worklist.actions" var="headeractions"/>

        <display:table name="${notifications}" class="pager" sort="list" export="true" id="notification" pagesize="25" defaultorder="ascending"
                       requestURI="${pageUrl}"
                       defaultsort="1" excludedParams="*">

            <display:column property="subject.coreDetail.name" title="${evaluatee}" group="0" headerClass="sortable" sortable="true"
                            sortProperty="subject.coreDetail.name" comparator="org.displaytag.model.RowSorter" class="pager"/>
            <display:column property="recipient.coreDetail.name" title="${evaluator}" group="1" headerClass="sortable" sortable="true"
                            sortProperty="recipient.coreDetail.name" comparator="org.displaytag.model.RowSorter" class="pager"/>
            <c:choose>
                <c:when test="${role == null && (empty role.label)}">
                    <display:column property="roleName" title="${rolename}" class="pager" headerClass="sortable" sortable="true"/>
                </c:when>
                <c:otherwise>
                    <display:column property="role.label" title="${rolename}" class="pager" headerClass="sortable" sortable="true"/>
                </c:otherwise>
            </c:choose>
            <display:column sortProperty="status" title="${statusaction}" class="pager" headerClass="sortable" sortable="true">
                <fmt:message key="${notification.status}"/>
            </display:column>
            <display:column sortProperty="action" title="${nextaction}" class="pager" headerClass="sortable" sortable="true">
                <fmt:message key="${notification.action}"/>
            </display:column>

            <c:if test="${command.status != 'COMPLETED'}">
                <display:column title="${headeractions}" class="pager" headerClass="sorted" sortable="false">
                    <%-- we cannot complete appraisals in these states as if they get re-opened we have no idea at what point of the workflow they were in --%>
                    <c:if test="${notification.actionable && notification.action != 'ANSWER'}">
                        <c:choose>
                            <c:when test="${notification.status == 'COMPLETED'}">
                                <%-- the ability to reopen the appraisal --%>
                                <a href="<c:url value="/perfman/viewreopenperformancereview.htm"><c:param name="reviewId" value="${command.id}"/><c:param name="notifId" value="${notification.id}"/></c:url>"><fmt:message key="reopen"/></a>
                            </c:when>
                            <c:otherwise>
                                <%-- the ability to complete the appraisal --%>
                                <a href="<c:url value="/perfman/viewcompletenotificationreview.htm"><c:param name="reviewId" value="${command.id}"/><c:param name="notifId" value="${notification.id}"/></c:url>"><fmt:message key="complete"/></a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </display:column>
            </c:if>

        </display:table>

    </zynap:infobox>

</zynap:infobox>
