<%@ include file="../../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.web.workflow.WorkflowConstants" %>

<%-- this uses DWR to load the username. Since we don't want to delay loading the page we defer the loading of the username --%>
<script type="text/javascript">
    loadUserName('<c:out value="${command.subjectId}"/>', '<zynap:message code="no.user.details" javaScriptEscape="true"/>');
</script>


<c:set var="questionnaireDefinition" value="${command.questionnaireDefinition}"/>
<c:set var="questionnaireGroups" value="${command.questionnaireGroups}"/>

<zynap:infobox title="${title}" id="viewQ">

    <c:if test="${questionnaireDefinition.description != null && !questionnaireDefinition.description == ''}">
        <div class="infomessage" colspan="2"><c:out value="${questionnaireDefinition.description}"/></div>
    </c:if>

    <table class="infotable" id="qnaireForm">
        <tr>
            <td class="infoheading" colspan="2">
                <fmt:message key="username.questionnaire"/> :
                     <span id="username">
                         <img src="<c:url value="/images/loading.gif"/>" alt="loading.."/><fmt:message key="loading.username.questionnaire"/>
                    </span>
            </td>
        </tr>
        <c:if test="${command.workflow.description != null}">
            <tr>
                <td class="infomessage" colspan="2">
                    <zynap:desc><c:out value="${command.workflow.description}"/></zynap:desc>
                </td>
            </tr>
        </c:if>
        <c:forEach var="group" items="${questionnaireGroups}" varStatus="indexer">
            <tr>
                <td class="infoheading" colspan="2">
                    <c:out value="${group.label}"/>
                </td>
            </tr>
            <c:forEach var="wrappedDynamicAttribute" items="${group.wrappedDynamicAttributes}" varStatus="countVar">
                <c:choose>
                    <c:when test="${!wrappedDynamicAttribute.editable}">
                        <tr>
                            <td class="infonarrative" colspan="2">
                                <c:out value="${wrappedDynamicAttribute.label}"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${wrappedDynamicAttribute.lineItem}">
                                <c:set var="lineItem" value="${wrappedDynamicAttribute}" scope="request"/>
                                <c:import url="../questionnaires/view/lineitemsnippet.jsp"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="question" value="${wrappedDynamicAttribute}" scope="request"/>
                                <c:if test="${question.description != null && !question.description == '' && !question.hidden}">
                                    <tr>
                                        <td class="infolabel" colspan="2"><c:out value="${question.description}"/></td>
                                    </tr>
                                </c:if>
                                <c:set var="titleAttr" scope="request"><c:if test="${question.hasTitle}">title="<c:out value="${question.title}"/>"</c:if></c:set>
                                <tr<c:if test="${question.hidden}"> style="display:none;"</c:if>>
                                    <td class="questionlabel" <c:out value="${titleAttr}" escapeXml="false"/>>
                                        <c:set var="editable" value="false" scope="request"/>
                                        <c:out value="${question.label}"/>&nbsp;:&nbsp;
                                        <c:if test="${question.mandatory}">*</c:if>&nbsp;
                                        <c:if test="${question.hasHelpText}"><c:import url="../questionnaires/helptextinclude.jsp"/></c:if>
                                    </td>
                                    <td style="<c:out value="${question.cellStyle}"/>" class="infodata" <c:out value="${titleAttr}" escapeXml="false"/>>
                                        <c:import url="../questionnaires/view/viewquestionsnippet.jsp"/>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:forEach>
    </table>

</zynap:infobox>