<%@ include file="../../../includes/include.jsp" %>

<c:if test="${editable}">
    <zynap:actionbox>
        <zynap:actionEntry>
            <input class="actionbutton" type="button" name="editDef" value="<fmt:message key="edit"/>" onclick="showHideDivs('questionnaireForm', 'ig', true);"/>
        </zynap:actionEntry>
    </zynap:actionbox>
</c:if>

<fmt:message key="answer.questionnaire" var="qTabLabel"><fmt:param><c:out value="${questionnaireLabel}"/></fmt:param></fmt:message>
<zynap:infobox title="${qTabLabel}" id="questionnaire">

    <c:set var="index" value="0" scope="request"/>
    <zynap:form action="" method="post" name="questionnaireForm">

        <table class="infotable" id="qnaireForm">
            <c:forEach var="group" items="${questionnaireGroups}" varStatus="indexer">
                <tr>
                    <td class="infoheading" colspan="2">
                        <div class="<c:out value="${openStyle}"/>"><c:out value="${group.label}"/></div>
                        <div class=<c:out value="${closedStyle}"/>>
                            <spring:bind path="command.questionnaireGroups[${indexer.index}].label">
                                <input class="label_input_text" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                <%@include file="../../../includes/error_message.jsp"%>
                            </spring:bind>
                        </div>
                    </td>
                </tr>
                <c:forEach var="wrappedDynamicAttribute" items="${group.wrappedDynamicAttributes}" varStatus="questionIndexer">
                    <c:choose>
                        <c:when test="${!wrappedDynamicAttribute.editable}">
                            <%-- the info narrative (a narrative question) --%>
                            <tr>
                                <td class="infonarrative" colspan="2">
                                    <div class=<c:out value="${openStyle}"/>><c:out value="${wrappedDynamicAttribute.label}"/></div>
                                    <div class=<c:out value="${closedStyle}"/>>
                                        <spring:bind path="command.questionnaireGroups[${indexer.index}].wrappedDynamicAttributes[${questionIndexer.index}].label">
                                            <input class="label_input_text" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                            <%@include file="../../../includes/error_message.jsp"%>
                                        </spring:bind>
                                    </div>
                                </td>
                            </tr>
                            <c:set var="index" value="${index + 1}" scope="request"/>                            
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <%-- line item --%>
                                <c:when test="${wrappedDynamicAttribute.lineItem}">
                                    <c:set var="lineItem" value="${wrappedDynamicAttribute}" scope="request"/>
                                    <c:set var="indexer" value="${indexer}" scope="request"/>
                                    <c:set var="questionIndexer" value="${questionIndexer}" scope="request"/>
                                    <c:import url="../questionnaires/admin/preview/lineitemsnippetpreview.jsp"/>
                                </c:when>

                                <c:otherwise>
                                    <c:set var="question" value="${wrappedDynamicAttribute}" scope="request"/>
                                    <c:set var="prefix" value="command.wrappedDynamicAttributes[${index}]" scope="request"/>
                                    <%-- from the question header jsp --%>
                                    <c:if test="${question.description != null && !question.description == ''}">
                                        <tr>
                                            <td class="infolabel" colspan="2">
                                                <div class=<c:out value="${openStyle}"/>><c:out value="${question.description}"/></div>
                                                <div class=<c:out value="${closedStyle}"/>>
                                                    <spring:bind path="command.questionnaireGroups[${indexer.index}].wrappedDynamicAttributes[${questionIndexer.index}].description">
                                                        <input class="label_input_text" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                                    </spring:bind>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
                                    
                                    <c:set var="titleAttr" scope="request"><c:if test="${question.hasTitle}">title="<c:out value="${question.title}"/>"</c:if></c:set>
                                    <tr>
                                        <td class="questionlabel" <c:out value="${titleAttr}" escapeXml="false"/>>

                                            <div class=<c:out value="${openStyle}"/>>
                                                <c:out value="${question.label}"/>&nbsp;:&nbsp;
                                            </div>
                                            <div class=<c:out value="${closedStyle}"/>>
                                                <spring:bind path="command.questionnaireGroups[${indexer.index}].wrappedDynamicAttributes[${questionIndexer.index}].label">
                                                    <input class="label_input_text" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                                    <%@include file="../../../includes/error_message.jsp"%>
                                                </spring:bind>
                                            </div>
                                            <c:if test="${question.mandatory}">*</c:if>&nbsp;
                                            <c:set var="editable" value="true" scope="request"/>
                                            <c:import url="../questionnaires/helptextinclude.jsp"/>
                                        </td>
                                        <td style="<c:out value="${question.cellStyle}"/>" class="infodata" <c:out value="${titleAttr}" escapeXml="false"/>>
                                            <c:import url="../questionnaires/admin/preview/viewquestionpreviewsnippet.jsp"/>
                                        </td>
                                    </tr>
                                    <c:set var="index" value="${index + 1}" scope="request"/>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:forEach>

            <tr>
                <td class="infobutton" colspan="2">
                    <div class=<c:out value="${closedStyle}"/>><input id="saveQDEditId" type="submit" class="inlinebutton" name="_save" value="<fmt:message key="save"/>"/></div>
                    <div class=<c:out value="${closedStyle}"/>>
                        <input class="inlinebutton" type="button" name="hideQEEdit" value="<fmt:message key="cancel"/>" onclick="document.forms.frmCancel.submit();"/>
                    </div>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>
