<%@ include file="../../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.objectives.ObjectiveConstants" %>
<%@ page import="com.zynap.talentstudio.web.workflow.WorklistController" %>

<script type="text/javascript">

    loadUserName('<c:out value="${command.subjectId}"/>', '<zynap:message code="no.user.details" javaScriptEscape="true"/>');
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
                                <%@include file="lineitemsnippet.jsp"%>
                                <c:set var="dynamicIndex" value="-1" scope="request"/>
                            </c:when>

                            <c:otherwise>
                                <c:set var="question" value="${wrappedDynamicAttribute}" scope="request"/>
                                <c:set var="prefix" value="command.wrappedDynamicAttributes[${index}]" scope="request"/>
                                <c:set var="queDisabled" value="${question.disabled}" scope="request"/>
                                <c:set var="fieldId" scope="request"><zynap:id><c:out value="${question.label}"/><c:out value="${index}"/></zynap:id></c:set>
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
                                    <td style="<c:out value="${question.cellStyle}"/>" class="questiondata" <c:out value="${titleAttr}" escapeXml="false"/>>
                                        <%@include file="editquestionsnippet.jsp"%>
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
                                                <td class="infodata" style="width:50%"><c:out value="${manager.label}"/>&nbsp;:&nbsp;</td>
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
                            <td class="infolabel" align="right" style="width:50%"><span><fmt:message key="send.email"/>&nbsp;:&nbsp;</span></td>
                            <td><span><input id="sendEmailCBId" type="checkbox" name="sendEmail" id="sendEmlId" <c:if test="${managerSelection}">disabled="true"</c:if>/></span></td>
                        </tr>
                        <tr>
                            <td class="infolabel" style="width:50%" align="right">
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
                            <td class="infobutton" style="width:50%">&nbsp;</td>
                            <td class="infobutton" align="left" style="width:50%">
                                <span><input class="inlinebutton" type="submit" name="_target6" value="<fmt:message key="send.request"/>"/></span>
                                <c:if test="${command.sendSuccess}"><div class="infomessage"><fmt:message key="send.success"/></div></c:if>
                                <c:if test="${command.sendFail}"><div class="infomessage"><fmt:message key="send.fail"/></div></c:if>
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

    $(function () {

        window.selectMap = {};
        //$("div.test").closest("tr");
        $('option.linked').closest('select').each(function () {
            // get the opions and push them into the array
            var selId = $(this).attr('id');
            console.log("adding element with id " + selId);
            var options = $('#' + selId + ' option');
            selectMap[selId] = $('#' + selId + ' option').clone(true);
        });

        //console.log("the select map i have = " + selectMap);
        testLinkableOptions();

        $('td').on('change', 'select.linkable', function () {

            var pSelId = $(this).attr('id');
            var selectedOptionLinkId = $("option:selected", this).attr("linkid");

            // if the selected option linkId is blank then add all, remove all options that link to this selects options            
            if (!selectedOptionLinkId) {
                clearDependants($("option", this), selectMap);
                return;
            }

            $.each(selectMap, function (key, value) {
                if (key != pSelId) {
                    var add = [];

                    // key is the potential select to populate
                    $.each(value, function (index, v) {
                        // if the requires list contains this selects options link_id then we are good to go
                        var req = v.getAttribute("requires");
                        var targetId = v.getAttribute("id");

                        // if an option has a requires which needs that selectedOPtionLinkId add the select to the add, as we will rewrite the select out
                        // but there may 2 dependant selects so we will need to test n options in an and scenario
                        if (parseInt(index) == 0) {
                            add.push(v);
                        }

                        if (req) {
                            var dynamicIndex = '';
                            var newRequiresId = req;
                            // check to see if the string end with an _ we must remove this
                            if (req.indexOf('_') >= 0) {
                                // console.log("we have an _ therefore dynamic line item here ");
                                // grab this index and store it
                                dynamicIndex = req.substring(req.indexOf('_'), req.length);
                                newRequiresId = req.substring(0, req.indexOf('_'));
                            }
                            var reqArray = newRequiresId.split(',');
                            var manip = 0;
                            var process = false;

                            for (var q = 0; q < reqArray.length; q++) {

                                var requiresLinkId = reqArray[q];
                                var optionSelected = $('#pp_' + requiresLinkId + dynamicIndex).is(':selected');
                                if (!optionSelected) {
                                    optionSelected = $('#pp_' + requiresLinkId).is(':selected');
                                }
                                if (optionSelected) {
                                    manip++;
                                }
                                //var itemAlreadyVisible = $('#' + targetId)[0];

                                if (requiresLinkId == selectedOptionLinkId || requiresLinkId + dynamicIndex == selectedOptionLinkId) {
                                    process = true;
                                }
                            }
                            // all options valid
                            if (process && manip && parseInt(manip) == reqArray.length) {
                                add.push(v);
                            }
                        }
                    });

                    if (add.length > 1) {
                        // todo before we clear the select get the selected option and clear it's dependeants
                        var selOptLinkId = $('#' + key + ' option:selected').attr('linkid');
                        $('#' + key).html('');
                        for (var t = 0; t < add.length; t++) {
                            $('#' + key).append(add[t]);
                        }
                        // set selected option to 0
                        $('#' + key + ' option:selected').removeAttr('selected');
                        $("#" + key + " option:first").attr('selected', 'selected');
                        if(selOptLinkId) {
                            clearDependantsInternal(selOptLinkId);
                        }
                    }
                }
            });
        });
    });

    function clearDependants(options, optionMap) {

        $.each(optionMap, function (key, ops) {
            var removeOptions = [];
            var removeElements = false;

            $.each(ops, function (k, data) {
                var dataReq = data.getAttribute("requires");
                // split dataReq

                if (dataReq) {
                    var dynamicIndex = '';
                    var newRequiresId = dataReq;
                    // check to see if the string end with an _ we must remove this
                    if (dataReq.indexOf('_') >= 0) {
                        console.log("we have an _ therefore dynamic line item here ");
                        // grab this index and store it
                        dynamicIndex = dataReq.substring(dataReq.indexOf('_'), dataReq.length);
                        newRequiresId = dataReq.substring(0, dataReq.indexOf('_'));
                    }
                    var reqArray = newRequiresId.split(',');
                    $.each(options, function (index, op) {
                        var linkId = op.getAttribute("linkid");
                        for (var c = 0; c < reqArray.length; c++) {
                            if (reqArray[c] + dynamicIndex == linkId || reqArray[c] == linkId) {
                                // to remove
                                removeOptions.push(data);
                                removeElements = true;
                            }
                        }
                    });
                }
            });

            if (removeElements) {
                // replace all, remove what we have
                //$('#' + key).html(ops);
                for (var l = 0; l < removeOptions.length; l++) {                 
                    $('#' + removeOptions[l].getAttribute('id')).remove();
                    clearDependantsInternal(removeOptions[l].getAttribute('linkid'));
                }
            }
        });
    }
    function clearDependantsInternal(optionLinkId) {
        // todo do through all the options on the page if any has a requires with this linkId remove it
        $('option.linked').each(function() {
            var requiresLink = $(this).attr('requires');

            var removeOptions = [];
            var removeElements = false;
            var optionElem = $(this);

            if(requiresLink) {
                var dynamicIndex = '';
                var newRequiresId = requiresLink;
                // check to see if the string end with an _ we must remove this
                if(requiresLink.indexOf('_') >= 0) {
                    // grab this index and store it
                    dynamicIndex = requiresLink.substring(requiresLink.indexOf('_'), requiresLink.length);
                    newRequiresId = requiresLink.substring(0, requiresLink.indexOf('_') );
                }
                var reqArray = newRequiresId.split(',');
                for(var c = 0; c < reqArray.length; c++) {
                    if(reqArray[c]+dynamicIndex == optionLinkId || reqArray[c] == optionLinkId) {
                        // to remove
                        removeOptions.push(optionElem);
                        removeElements = true;
                    }
                }
                if(removeElements) {
                    for(var l = 0; l < removeOptions.length; l++) {                        
                        $('#' + removeOptions[l].attr('id')).remove();
                        clearDependantsInternal(removeOptions[l].attr('linkid'));
                    }
                }
            }
        });
    }
</script>
