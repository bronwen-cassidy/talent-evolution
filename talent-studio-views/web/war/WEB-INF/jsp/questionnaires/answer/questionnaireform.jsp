<%@ include file="../../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.objectives.ObjectiveConstants" %>
<%@ page import="com.zynap.talentstudio.web.workflow.WorklistController" %>

<script type="text/javascript">

    loadUserName('<c:out value="${command.subjectId}"/>', '<zynap:message code="no.user.details" javaScriptEscape="true"/>');
    removeTimer();
    function clearTextField(paragraphElemId, textFieldElemId) {
        var txtFld = getElemById(textFieldElemId);
        txtFld.value = '';
        var paragraphElem = getElemById(paragraphElemId);
        paragraphElem.innerHTML = '';
    }
    function checkForm(errorMessage, theFrm) {
        if(verifyErrors(errorMessage)) {
            theFrm.submit();
        } else {
            alert(errorMessage);
        }
    }
</script>

<h1>
    <div id="infomsgquestupdate"><fmt:message key="questionnaire.isautoupdating"/></div>
</h1>
<p/>

<fmt:message key="answer.questionnaire" var="qTabLabel">
    <fmt:param><c:out value="${questionnaireLabel}"/></fmt:param>
</fmt:message>

<zynap:infobox title="${qTabLabel}" id="questionnaire">

    <c:set var="index" value="0" scope="request"/>

    <c:if test="${message != null}">
        <div class="infomessage"><fmt:message key="${message}"/></div>
    </c:if>

    <fmt:message key="questionnaire.hasfatalerrors" var="errMsg"/>
    <zynap:message text="${errMsg}" javaScriptEscape="true" var="errMsgJ"/>

    <table class="infotable" id="qnaireForm">

        <tr>
            <td class="infoheading" colspan="2">
                <fmt:message key="username.questionnaire"/> :
             <span id="username">
                <img src="<c:url value="/images/loading.gif"/>" alt="loading..."/> <fmt:message key="loading.username.questionnaire"/>
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

            <c:forEach var="wrappedDynamicAttribute" items="${group.wrappedDynamicAttributes}">
                <c:set var="dynamicIndex" value="-1" scope="request"/>
                <c:choose>
                    <c:when test="${!wrappedDynamicAttribute.editable}">
                        <tr>
                            <td class="infonarrative" colspan="2">
                                <c:out value="${wrappedDynamicAttribute.label}"/>
                            </td>
                        </tr>
                        <c:set var="index" value="${index + 1}" scope="request"/>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${wrappedDynamicAttribute.lineItem}">
                                <c:set var="lineItem" value="${wrappedDynamicAttribute}" scope="request"/>
                                <c:import url="../questionnaires/answer/lineitemsnippet.jsp"/>
                                <c:set var="dynamicIndex" value="-1" scope="request"/>
                            </c:when>

                            <c:otherwise>
                                <c:set var="question" value="${wrappedDynamicAttribute}" scope="request"/>
                                <c:set var="prefix" value="command.wrappedDynamicAttributes[${index}]" scope="request"/>
                                <c:set var="queDisabled" value="${question.disabled}" scope="request"/>
                                <c:set var="fieldId" scope="request"><zynap:id><c:out value="${question.label}"/><c:out value="${index}"/></zynap:id></c:set>
                                <c:if test="${question.description != null && !question.description == ''}">
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
                                <td class="questiondata" <c:out value="${titleAttr}" escapeXml="false"/>>
                                <c:import url="../questionnaires/answer/editquestionsnippet.jsp"/>
                                </td>
                                </tr>
                                <c:set var="index" value="${index + 1}" scope="request"/>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:forEach>

        <zynap:form action="" method="post" name="questionnaireForm">

            <c:set var="tablesIndex" value="1" scope="request"/>
            <c:set var="managerSelection" value="${command.myPortfolio == true && command.userManagersCount > 1}" scope="request"/>

            <input id="actId" type="hidden" name="action" value=""/>
            <input id="tarId" type="hidden" name="" value=""/>
            <input id="subjId" type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${command.subjectId}"/>"/>
            <input id="selObjectiveId" type="hidden" name="<%=ObjectiveConstants.OBJECTIVE_ID%>" value="-1"/>
            <input id="selGroupId" type="hidden" name="<%=WorklistController.SELECTED_GROUP_PARAM%>" value=""/>
            <input id="queDefIdxx" type="hidden" name="QUE_DEF_ID" value="<c:out value="${command.definitionId}"/>"/>
            <c:choose>
                <c:when test="${infoForm}">
                    <c:if test="${command.showInboxInfo}">
                        <c:if test="${managerSelection}">
                            <tr>
                                <td class="infolabel" align="right"><span><fmt:message key="please.select.managers"/>&nbsp;:&nbsp;</span></td>
                                <td class="infodata">
                                    <table cellpadding="0" cellspacing="0">
                                        <c:forEach var="manager" items="${command.userManagers}">
                                            <tr>
                                                <td class="infodata" width="50%"><c:out value="${manager.label}"/>&nbsp;:&nbsp;</td>
                                                <spring:bind path="command.selectedManagerIds">
                                                    <td class="infodata" ${colsp}>
                                                        <input type="checkbox" class="input_checkbox" value="<c:out value="${manager.id}"/>" name="selectedManagerIds" onclick="enableSelection('sendIbx','sendEmailCBId')"/>
                                                    </td>
                                                </spring:bind>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="infolabel" align="right" width="50%"><span><fmt:message key="send.email"/>&nbsp;:&nbsp;</span></td>
                            <td><span><input id="sendEmailCBId" type="checkbox" name="sendEmail" id="sendEmlId" <c:if test="${managerSelection}">disabled="true"</c:if>/></span></td>
                        </tr>
                        <tr>
                            <td class="infolabel" width="50%" align="right">
                    <span>
                        <c:choose>
                            <c:when test="${command.managerView}"><fmt:message key="send.to.individual"/>&nbsp;:&nbsp;</c:when>
                            <c:otherwise><fmt:message key="send.to.manager"/>&nbsp;:&nbsp;</c:otherwise>
                        </c:choose>
                    </span>
                            </td>
                            <td>
                                <span><input type="checkbox" name="sendToInbox" id="sendIbx" <c:if test="${managerSelection}">disabled="true"</c:if>/></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="infobutton" width="50%">&nbsp;</td>
                            <td class="infobutton" align="left" width="50%">
                                <span><input class="inlinebutton" type="submit" name="_target6" value="<fmt:message key="send.request"/>"/></span>
                                <c:if test="${command.sendSuccess}"><div class="infomessage"><fmt:message key="send.success"/></div></c:if>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td class="infobutton" colspan="2" align="center">
                            <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="close"/>" onclick="checkForm('<c:out value="${errMsgJ}"/>', document.forms.qCancelEdit);"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td class="infobutton" colspan="2" align="center">
                            <input type="submit" class="inlinebutton" name="_target3" value="<fmt:message key="done"/>"/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </zynap:form>
    </table>
</zynap:infobox>

<form method="post" name="qCancelEdit">
    <input type="hidden" name="_cancel" value="_cancel"/>
</form>
<form method="post" name="qClose">
    <input type="hidden" name="_target4" value="4"/>
</form>

<script type="text/javascript">

    $(function() {

        // todo create an array of selects which contains the full set of options then copy and remove the copy
        var selectMap = new Object();

        $('.linkable').each(function() {
            // get the opions and push them into the array
            var selId = $(this).attr('id');
            var options = $('#' + selId + ' option');
            selectMap[selId] = options;
        });

        console.log("the select map i have = " + selectMap);
        testLinkableOptions();

        $('.linkable').change(function() {
            // show all linked options please to start off with
            $('.linked').show();
            console.log("show all");
            // now hide the one not allowed to be seen
            testLinkableOptions();
        });
    });

    function testLinkableOptions() {
        // on first load all options are shown none are hidden so now we just need to hide the ones that do not have a selected parent
        $('select' + ' option.linked').each(function () {
            var requiresIds = $(this).attr('requires');
            console.log("option requires " + requiresIds);

            // handle dynamic line items
            var dynamicIndex = '';
            var newRequiresId = requiresIds;
            // check to see if the string end with an _ we must remove this
            if(requiresIds.indexOf('_') >= 0) {
                console.log("we have an _ therefore dynamic line item here ");
                // grab this index and store it
                dynamicIndex = requiresIds.substring(requiresIds.indexOf('_'), requiresIds.length);
                newRequiresId = requiresIds.substring(0, requiresIds.indexOf('_') );
            }
            console.log("newRequiresIds is now " + newRequiresId);
            var requiresArray = newRequiresId.split(',');
            var show = true;
            for (var i = 0; i < requiresArray.length; i++) {

                if(!$("option[linkId='" + requiresArray[i] + dynamicIndex +"']").is(":selected")) {
                    show = false;
                    break;
                }
            }

            if(!show) {
                console.log("hiding the option with linkId " + $(this).attr('linkId'));
                $(this).hide();
            }
        });
    }

</script>