<%@ page import="com.zynap.talentstudio.web.questionnaires.definition.ViewQuestionnaireDefinitionController"%>
<%@ include file="../../includes/include.jsp" %>

<zynap:historyLink var="viewQuestionnaireUrl" url="viewquestionnairedefinition.htm"/>

<zynap:saveUrl url="${viewQuestionnaireUrl}">
    <zynap:param name="<%=ParameterConstants.ACTIVE_TAB%>" value="<%=ViewQuestionnaireDefinitionController.QUESTIONNAIRES_TAB%>"/>
    <zynap:param name="<%=ParameterConstants.QUESTIONNAIRE_DEF_ID%>" value="${command.id}"/>
</zynap:saveUrl>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_back" method="get" action="listquestionnairedefinition.htm">
           <c:forEach var="entry" items="${command.displayTagValues}">
               <input type="hidden" name="<c:out value="${entry.key}"/>" value="<c:out value="${entry.value}"/>"/>
           </c:forEach>            
           <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="document.forms._back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<table class="artefact">
    <tr>
        <th class="artefact">
            <c:out value="${command.label}"/>
        </th>
    </tr>
    <tr>
        <td class="artefact">
            <zynap:tab defaultTab="${command.activeTab}" id="qd_info" url="javascript" tabParamName="activeTab">
                <fmt:message key="qdef.information" var="detailsTabLabel" />
                <zynap:tabName value="${detailsTabLabel}" name="definition"  />

                <fmt:message key="qdef.preview" var="previewTabLabel" />
                <zynap:tabName value="${previewTabLabel}" name="preview"/>

                <fmt:message key="qdef.questionnaires" var="queTabLabel" />
                <zynap:tabName value="${queTabLabel}" name="questionnaires"/>

                <c:set var="questionnaireDefinition" value="${command}" scope="request"/>
                <div id="definition_span" style="display:<c:choose><c:when test="${command.activeTab == 'definition'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
                    <c:import url="../questionnaires/admin/questionnairedefinitioninfo.jsp" />
                </div>

                <div id="preview_span" style="display:<c:choose><c:when test="${command.activeTab == 'preview'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
                    <c:set var="questionnaireLabel" value="${command.label}" scope="request"/>                    
                    <c:set var="questionnaireGroups" value="${command.questionnaireGroups}" scope="request"/>
                    <c:set var="editable" value="${not command.hasQuestionnaires}" scope="request"/>
                    <c:set var="openStyle" value="open" scope="request"/>
                    <c:set var="closedStyle" value="closed" scope="request"/>
                    <c:if test="${command.editing}">
                        <c:set var="openStyle" value="closed" scope="request"/>
                        <c:set var="closedStyle" value="open" scope="request"/>
                    </c:if>                    
                    <c:import url="../questionnaires/admin/preview/questionnairedefinitionpreview.jsp"/>
                </div>

                <div id="questionnaires_span" style="display:<c:choose><c:when test="${command.activeTab == 'questionnaires'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
                    <c:set var="questionnaires" value="${command.questionnaires}" scope="request"/>
                    <c:import url="../questionnaires/admin/listquestionnaireworkflows.jsp" />
                </div>

            </zynap:tab>
        </td>
    </tr>
</table>

<!-- this form is used when we edit the questionnaire definition to change things like labels -->
<zynap:form method="post" action="" name="frmCancel" htmlId="cancFrmId">
    <input type="hidden" name="_cancel" value="_cancel"/>    
</zynap:form>

<zynap:window elementId="helpText" closeFunction="closePopup()">
    <textarea style="width:100%; height:80%" id="helpTextAreaElementId" rows="12" cols="45"></textarea>
    <span><input class="actionbutton" type="button" name="test" value="Save" onclick="saveHelpText(); popupOK();"/></span>
</zynap:window>