<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<script type="text/javascript">
	$(function()
    {
        $("#questionnaireitemtable").tablesorter({widthFixed: true, widgets: ['zebra']})
        .tablesorterPager({container: $("#questionnaireitemtablepg"), positionFixed: false, size: 25 });
    });

    function actionQuestionnaire(btnSpanId, questionnaireId, action, spanId, newstatus) {
        questionnaireBean.setQuestionnaireStatus(questionnaireId, action, { callback : function(status) {
            if(status == 'SUCCESS') {
                $("#" + spanId).show();
                $("#" + btnSpanId).hide();
                $("#staq" + questionnaireId).html(newstatus);
            } else {
                alert(status);
            }
        }});
    }
</script>

<c:set var="command" value="${model.questionnaire}"/>
<c:set var="questionnaires" value="${model.questionnaires}"/>
<fmt:message key="date.format" var="datePattern"/>

<zynap:actionbox>
    
    <zynap:actionEntry>
        <fmt:message key="back" var="backLabel"/>
        <zynap:backButton label="${backLabel}" cssClass="actionbutton" method="get"/>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <zynap:form method="get" name="_delete" action="/admin/confirmdeletequestionnaireworkflow.htm">
            <input type="hidden" name="<%=ParameterConstants.QUESTIONNAIRE_ID%>" value="<c:out value="${command.id}"/>"/>
            <input class="actionbutton" type="button" name="_delete" value="<fmt:message key="delete"/>" onclick="document.forms._delete.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="edit" name="editQuest" action="/admin/editquestionnaireworkflow.htm">
            <input type="hidden" name="<%=ParameterConstants.QUESTIONNAIRE_ID%>" value="<c:out value="${command.id}"/>"/>
            <input class="actionbutton" type="button" name="editQ" value="<fmt:message key="edit"/>" onclick="document.forms.editQuest.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <c:if test="${command.status == 'COMPLETED'}">
        <zynap:actionEntry>
            <zynap:form method="get" name="_archive" action="/admin/archivequestionnaireworkflow.htm">
                <input type="hidden" name="<%=ParameterConstants.QUESTIONNAIRE_ID%>" value="<c:out value="${command.id}"/>"/>
                <input class="actionbutton" type="button" name="_close" value="<fmt:message key="archive"/>" onclick="document.forms._archive.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
    </c:if>
    <c:if test="${command.status == 'ARCHIVED'}">
        <zynap:actionEntry>
            <zynap:form method="get" name="_unarchive" action="/admin/archivequestionnaireworkflow.htm">
                <input type="hidden" name="<%=ParameterConstants.QUESTIONNAIRE_ID%>" value="<c:out value="${command.id}"/>"/>
                <input class="actionbutton" type="button" name="_close" value="<fmt:message key="unarchive"/>" onclick="document.forms._unarchive.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
    </c:if>
    <%-- only display close button if not completed already --%>
    <c:if test="${command.status != 'COMPLETED' && command.status != 'ARCHIVED'}">
        <zynap:actionEntry>
            <zynap:form method="get" name="_close" action="/admin/confirmclosequestionnaireworkflow.htm">
                <input type="hidden" name="<%=ParameterConstants.QUESTIONNAIRE_ID%>" value="<c:out value="${command.id}"/>"/>
                <input class="actionbutton" type="button" name="_close" value="<fmt:message key="worklist.complete"/>" onclick="document.forms._close.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
    </c:if>
</zynap:actionbox>

<zynap:infobox title="${title}" id="viewQWF">
    <table class="infotable" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${command.description}"/></zynap:desc></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.definition"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.questionnaireDefinition.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.group"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:choose><c:when test="${command.group != null}"><c:out value="${command.group.label}"/></c:when><c:otherwise><fmt:message key="none"/></c:otherwise></c:choose></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.end.date"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:formatDate value="${command.expiryDate}" pattern="${datePattern}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.access_permissions"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <table class="crosstabdetail">
                    <tr>
                        <th>&nbsp;</th>
                        <th><fmt:message key="questionnaire.read.permission"/></th>
                        <th><fmt:message key="questionnaire.write.permission"/></th>
                    </tr>
                    <tr>
                        <td><fmt:message key="questionnaire.individual_permissions"/>&nbsp;:&nbsp;</td>
                        <td><fmt:message key="${command.individualRead}"/></td>
                        <td><fmt:message key="${command.individualWrite}"/></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="questionnaire.manager_permissions"/>&nbsp;:&nbsp;</td>
                        <td><fmt:message key="${command.managerRead}"/></td>
                        <td><fmt:message key="${command.managerWrite}"/></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.population"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.population.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="questionnaire.status"/>&nbsp;:&nbsp;</td>
            <td class="infodata" id="queStatusId"><fmt:message key="${command.status}"/></td>
        </tr>
    </table>

<c:if test="${!empty questionnaires}">
    <p>&nbsp;</p>
    <fmt:message key="questionnaire.actions" var="performancetitle"/>
    <zynap:infobox title="${performancetitle}" id="viewPR">


        <fmt:message key="person.name" var="personheader"/>
        <fmt:message key="appraisal.completed.action" var="statusheader"/>

        <span id="questionnaireitemtablepg" class="pagelinks">
            <form>
                <img src="../images/icons/first.png" class="first"/>
                <img src="../images/icons/prev.png" class="prev"/>
                <select class="pagesize">
                    <option value="10">10&nbsp;<fmt:message key="num.items"/></option>
                    <option value="25" selected="selected">25&nbsp;<fmt:message key="num.items"/></option>
                    <option value="35">35&nbsp;<fmt:message key="num.items"/></option>
                    <option value="45">45&nbsp;<fmt:message key="num.items"/></option>
                </select>
                <img src="../images/icons/next.png" class="next"/>
                <img src="../images/icons/last.png" class="last"/>
            </form>
            <span><fmt:message key="total.num.items"><fmt:param value="${model.numItems}"/></fmt:message></span>
        </span>

        <c:url var="subUrl" value="viewsubject.htm">
            <c:param name="backURL" value="${request.requestURI}"/>
        </c:url>
        <%--<zynap:historyLink var="subUrl" url="viewsubject.htm"/>--%>
        <c:url value="${request.requestURI}" var="pageUrl"/>

        <display:table name="${questionnaires}" class="pager" sort="list" requestURI="${pageUrl}" htmlId="questionnaireitemtable" export="true" id="questionnaire" defaultorder="ascending">
            <display:column property="subject.label" sortable="true" class="pager" title="${personheader}" headerClass="sortable" paramId="command.node.id" href="${subUrl}" paramProperty="subject.id"/>
            <display:column sortable="true" class="pager" title="${statusheader}" headerClass="sortable">
                <span id="staq<c:out value="${questionnaire.id}"/>"><fmt:message key="${questionnaire.completed}"/></span>
            </display:column>
        </display:table>
    </zynap:infobox>
</c:if>
    
</zynap:infobox>
