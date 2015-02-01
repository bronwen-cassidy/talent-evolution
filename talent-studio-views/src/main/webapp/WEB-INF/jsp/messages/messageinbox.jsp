<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:saveUrl/>

<zynap:message code="delete.message.confirm" var="deletename"/>
<fmt:message key="message.subject" var="subjectname"/>
<fmt:message key="message.link" var="linkname"/>
<fmt:message key="message.from" var="fromname"/>
<fmt:message key="worklist.actions" var="actionname"/>
<fmt:message key="message.date.recieved" var="headerdatereceived"/>

<zynap:infobox title="${title}" id="listInboxIms">

    <c:url var="pageUrl" value="${request.requestURI}"/>        
    <c:set var="managerEditUrl" value="/talentarena/editmystaffinboxquestionnaire.htm"/>
    <c:set var="myEditUrl" value="/talentarena/editmyinboxquestionnaire.htm"/>
    <c:url var="deleteSelectedUrl" value="/talentarena/worklistdeletemessages.htm">
        <c:if test="${command.displayTagParams != null}">
            <c:forEach var="p" items="${command.displayTagParams}">
                <c:param name="${p.key}" value="${p.value}"/>
            </c:forEach>
        </c:if>    
    </c:url>

    <c:if test="${not empty command.inboxItems}">
        <table class="infotable">
            <tr>
                <td class="actionButton" align="center">
                    <input class="inlinebutton" type="button" name="SetAll" value="<fmt:message key="selectall"/>" onClick="checkAll(document.deleteMessages.m_ids)"/>
                    <input class="inlinebutton" type="button" name="ClearAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.deleteMessages.m_ids)"/>
                    <input class="inlinebutton" type="button" name="DelAll" value="<fmt:message key="deleteall"/>" onClick="confirmDeleteMessages('deleteMessages', '<c:out value="${deletename}"/>')"/>
                </td>
            </tr>
        </table>
    </c:if>

    <form action="<c:out value="${deleteSelectedUrl}"/>" name="deleteMessages">
        <display:table name="${command.inboxItems}" id="message" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1">

            <display:column>
                <input type="checkbox" class="input_checkbox" name="m_ids" value="<c:out value="${message.id}"/>"/>
            </display:column>

            <c:set var="pClass" value="pager unread" scope="request"/>
            <c:if test="${message.status == 'READ'}"><c:set var="pClass" value="pager read" scope="request"/></c:if>
            <display:column property="fromUser.label" sortable="true" headerClass="sortable" class="${pClass}" title="${fromname}"/>

            <display:column sortable="false" headerClass="sorted" class="${pClass}" title="${subjectname}">
                <fmt:message key="message.info"><fmt:param value="${message.label}"/></fmt:message>
            </display:column>

            <display:column sortProperty="label" title="${linkname}" sortable="true" headerClass="sortable" class="${pClass}">
                <c:set var="myPortfolio" value="${message.individualView}"/>
                <c:set var="editUrl" value="${managerEditUrl}"/>
                <c:if test="${myPortfolio}"><c:set var="editUrl" value="${myEditUrl}"/></c:if>

                <c:url var="editQuestionnaireUrl" value="${editUrl}">
                    <c:param name="q_id" value="${message.questionnaireId}"/>
                    <c:param name="myPortfolio" value="${myPortfolio}"/>
                </c:url>
                <a href="javascript:markMessageAsRead('<c:out value="${editQuestionnaireUrl}"/>', '<c:out value="${message.id}"/>');"><c:out value="${message.label}"/></a>
            </display:column>

            <display:column decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator" property="dateReceived" title="${headerdatereceived}" sortable="true" headerClass="sortable" class="${pClass}"/>

            <display:column headerClass="sorted" class="${pClass}" title="${actionname}">
                
                <c:url value="/talentarena/worklistdeletemessage.htm" var="deleteUrl" scope="request">
                    <c:param name="m_id" value="${message.id}"/>
                    <c:if test="${command.displayTagParams != null}">
                        <c:forEach var="p" items="${command.displayTagParams}">
                            <c:param name="${p.key}" value="${p.value}"/>
                        </c:forEach>
                    </c:if>
                </c:url>

                <img class="popupControl" src="../images/popupClose.gif" alt="delete" title="delete message item" onclick="confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deletename}"/>');"/>
            </display:column>

        </display:table>
    </form>
</zynap:infobox>