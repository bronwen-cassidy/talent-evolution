<%@ include file="../../includes/include.jsp" %>

<c:set var="styleClassName" value="verticalOptions" scope="request"/>

<c:if test="${showHorizontal}">
    <c:set var="styleClassName" value="horizontalOptions" scope="request"/>
</c:if>

<c:set var="disbledVar" value="${question.managerWrite && !managerAccessView}"/>

<c:choose>

<c:when test="${question.type == 'TEXT'}">
    <spring:bind path="${prefix}.value">

        <input id="<c:out value="${fieldId}"/>" type="text" name="<c:out value="${status.expression}"/>"
        value="<c:out value="${status.value}"/>" <c:out value="${titleAttr}" escapeXml="false"/>
        <c:if test="${question.length != 0}">size="<c:out value="${question.length}"/>"</c:if>
        <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>
        <%--onmouseout="javascript:saveUpdateDeleteQuestionnaire('<c:out value="${fieldId}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}_attid"/>','<c:out value="${fieldId}"/>_error');"--%>
        onblur="javascript:saveUpdateDeleteQuestionnaire('<c:out value="${fieldId}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}_attid"/>','<c:out value="${fieldId}"/>_error');"

        />
        <!-- on mouse out required google spell check has issues otherwise-->
        <%@ include file="../../includes/error_message.jsp" %>
        <span style="color:red; display:none;" id="<c:out value="${fieldId}"/>_error">&nbsp;</span>
        <input id="<c:out value="${fieldId}_attid"/>" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="1"/>
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'INTEGER' || question.type == 'POSITIVEINTEGER'}">
    <spring:bind path="${prefix}.value">
        <input id="<c:out value="${fieldId}"/>" type="text" class="input_number" name="<c:out value="${status.expression}"/>"
        value="<c:out value="${status.value}"/>" <c:out value="${titleAttr}" escapeXml="false"/>
        <c:if test="${question.length != 0}">size="<c:out value="${question.length}"/>"
        </c:if>
        <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>
        <%--onmouseout="javascript:saveUpdateDeleteQuestionnaire('<c:out value="${fieldId}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}_attid"/>','<c:out value="${fieldId}"/>_error');"--%>
        onblur="javascript:saveUpdateDeleteQuestionnaire('<c:out value="${fieldId}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}_attid"/>','<c:out value="${fieldId}"/>_error');"
        />
        <!-- on mouse out required google spell check has issues otherwise-->
        <%@ include file="../../includes/error_message.jsp" %>
        <div style="color:red;display:none;" id="<c:out value="${fieldId}"/>_error">&nbsp;</div>
        <input id="<c:out value="${fieldId}_attid"/>" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="2"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'TEXTAREA' || question.type == 'TEXTBOX'}">
    <spring:bind path="${prefix}.value">
        <textarea
        <%--onmouseout="javascript:saveUpdateDeleteQuestionnaire('<c:out value="${fieldId}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}_attid"/>','<c:out value="${fieldId}"/>_error');"--%>
                onblur="javascript:saveUpdateDeleteQuestionnaire('<c:out value="${fieldId}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}_attid"/>','<c:out value="${fieldId}"/>_error');"
        <c:if test="${question.length != 0}">style="display:block; width:<c:out value="${question.length}"/>em;"</c:if>
        id="<c:out value="${fieldId}"/>"
        name="<c:out value="${status.expression}"/>" rows="4"
        <c:if test="${question.length != 0}">cols="<c:out value="${question.length}"/>"</c:if>
        <c:out value="${titleAttr}" escapeXml="false"/>
        <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>><c:out value="${status.value}"/></textArea>
        <!-- on mouse out required google spell check has issues otherwise-->
        <%@ include file="../../includes/error_message.jsp" %>
        <div style="color:red;display:none;" id="<c:out value="${fieldId}"/>_error">&nbsp;</div>
        <input id="<c:out value="${fieldId}_attid"/>" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="3"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'DATE'}">
    <c:set var="dispId" value="${fieldId}_disp"/>
    <c:set var="isoId" value="${fieldId}_iso"/>

    <spring:bind path="${prefix}.date">
        <%-- DONT CHANGE THE LAYOUT BELOW. MUST NOT BE A SPACE BEFORE THE BUTTON --%>
            <span style="white-space: nowrap;">
                <input id="<c:out value="${dispId}"/>"
                       class="input_date" name="ignoredValue<c:out value="${dispId}"/>"
                       type="text" value="<c:out value='${question.displayValue}'/>" readonly="true"
                       <c:out value="${titleAttr}" escapeXml="false"/>
                /><input type="button" class="partnerbutton" value="..."  id="<c:out value="${fieldId}"/>_date" name="<c:out value="${fieldId}"/>_date"
                         <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>
                         onclick="popupShowCalendarX('<zynap:message code="select.date" javaScriptEscape="true"/>', '<c:out value="${isoId}"/>_partbtn', 'calendarPopup', '<c:out value="${dispId}"/>', '<c:out value="${isoId}"/>',
                     'saveUpdateDeleteQuestionnaireDate(\'<c:out value="${isoId}"/>\', \'<c:out value="${command.questionnaireId}"/>\', \'<c:out value="${question.daId}"/>\', \'<c:out value="${dispId}_attid"/>\', \'<c:out value="${dispId}_error"/>\')', true);"/></span>
        <input id="<c:out value="${isoId}"/>" name="<c:out value="${status.expression}"/>" type="hidden" value="<c:out value="${question.date}"/>"/>
        <%@include file="../../includes/error_message.jsp" %>
        <div style="color:red;display:none;" id="<c:out value="${dispId}"/>_error">&nbsp;</div>
        <input id="<c:out value="${dispId}_attid"/>" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="4"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'SELECT' || question.type == 'STRUCT'}">
    <%-- if there is a dynamicIndex here we need to post_fix the link_id with it and the requires--%>
    <spring:bind path="${prefix}.value">
        <select id="<c:out value="${fieldId}"/>"
        name="<c:out value="${status.expression}"/>" <c:out value="${titleAttr}" escapeXml="false"/>
        class="linkable"
        onchange="javascript:saveUpdateDeleteQuestionnaireList('<c:out value="${fieldId}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}_attid"/>','<c:out value="${fieldId}"/>_error');"
        <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>>
        <c:if test="${!question.hasBlank}">
            <c:choose>
                <c:when test="${question.attributeDefinition.mandatory}">
                    <option value="" <c:if test="${question.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                </c:when>
                <c:otherwise>
                    <option value="" <c:if test="${question.value == null}">selected</c:if>></option>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:forEach var="vals" items="${question.attributeDefinition.refersToType.lookupValues}">
            <c:set var="cssClass" value="not_linked"/>
            <c:if test="${vals.requires != null && vals.requires != ''}"><c:set var="cssClass" value="linked"/></c:if>
            <c:set var="ll_id" value="${vals.linkId}"/>
            <c:set var="ll_req" value="${vals.requires}"/>
            <c:if test="${dynamicIndex != -1}">
                    <c:set var="ll_id" value="${vals.linkId}_${dynamicIndex}"/>
                    <c:set var="ll_req" value="${vals.requires}_${dynamicIndex}"/>
            </c:if>
            <option id="pp_<c:out value="${ll_id}"/>" linkid="<c:out value="${vals.linkId}"/>" requires="<c:out value="${ll_req}"/>"
            class="<c:out value="${cssClass}"/>" value="<c:out value="${vals.id}"/>" <c:if test="${question.value == vals.id}">selected</c:if>>
            <c:if test="${!vals.blank}"><c:out value="${vals.label}"/></c:if>
            </option>
        </c:forEach>
        </select>
        <%@ include file="../../includes/error_message.jsp" %>
        <span style="color:red;display:none;" id="<c:out value="${fieldId}"/>_error">&nbsp;</span>
        <input id="<c:out value="${fieldId}_attid"/>" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="5"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>
</c:when>


<c:when test="${question.type == 'RADIO'}">
    <spring:bind path="${prefix}.value">
        <c:set var="rbn" value="0"/>
        <c:forEach var="vals" items="${question.attributeDefinition.refersToType.lookupValues}">
            <c:set var="rbn" value="${rbn + 1}"/>
            <span class="<c:out value="${styleClassName}"/>"><input
            <c:out value="${titleAttr}" escapeXml="false"/>
            type="radio" class="input_radio"
            id="<c:out value="${fieldId}"/>_<c:out value="${rbn}"/>"
            name="<c:out value="${status.expression}"/>"
            value="<c:out value="${vals.id}"/>"
            onclick="javascript:saveUpdateDeleteQuestionnaire('<c:out value="${fieldId}"/>_<c:out value="${rbn}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}"/>_attid','<c:out value="${fieldId}"/>_error');"
            <c:if test="${question.value == vals.id}">checked</c:if>
            <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>><c:out value="${vals.label}"/></span>
            <input name="daId" id="<c:out value="${fieldId}"/>_<c:out value="${rbn}"/>_daId" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        </c:forEach>
        <div style="color:red;display:none;" id="<c:out value="${fieldId}"/>_error">&nbsp;</div>
        <input id="<c:out value="${fieldId}"/>_attid" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="6"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>

    </spring:bind>
</c:when>

<c:when test="${question.type == 'CHECKBOX'}">
    <spring:bind path="${prefix}.value">
        <c:set var="cbn" value="0"/>
        <c:forEach var="vals" items="${question.attributeDefinition.refersToType.lookupValues}">

            <c:set var="cbn" value="${cbn + 1}"/>
            <c:set var="found" value="false"/>
            <c:set var="xanado" value=""/>

            <c:forEach var="msVal" items="${question.multiSelectValues}">
                <c:if test="${msVal.value == vals.id}">
                    <c:set var="found" value="true"/>
                    <c:set var="xanado" value="${msVal.id}"/>
                </c:if>
            </c:forEach>

            <span class="<c:out value="${styleClassName}"/>"><input
                type="checkbox"
                class="input_checkbox"
                id="<c:out value="${fieldId}"/>_<c:out value="${cbn}"/>"
            onclick="saveDeleteQuestionnaireCheckBox('<c:out value="${fieldId}"/>_<c:out value="${cbn}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}"/>_<c:out value="${cbn}"/>_attid','<c:out value="${fieldId}"/>_error');"
            name="<c:out value="${status.expression}"/>"
            value="<c:out value="${vals.id}"/>"
            <c:out value="${titleAttr}" escapeXml="false"/>
            <c:if test="${found}">checked</c:if>
            <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if> ><c:out value="${vals.label}"/></span>
            <input id="<c:out value="${fieldId}"/>_<c:out value="${cbn}"/>_attid" type="hidden" value="<c:out value="${xanado}"/>"/>
            <input name="daId" id="<c:out value="${fieldId}"/>_<c:out value="${cbn}"/>_daId" type="hidden"
            value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
            <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="7"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
            <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
        </c:forEach>
        <div style="color:red;display:none;" id="<c:out value="${fieldId}"/>_error">&nbsp;</div>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'MULTISELECT'}">
    <spring:bind path="${prefix}.value">
        <select class="multi-linkable"
                onblur="javascript:saveUpdateDeleteQuestionnaireMultiSelect('<c:out value="${fieldId}"/>','<c:out value="${command.questionnaireId}"/>','<c:out value="${question.daId}"/>','<c:out value="${fieldId}_attid"/>','<c:out value="${fieldId}"/>_error');"
        <c:out value="${titleAttr}" escapeXml="false"/> id="<c:out value="${fieldId}"/>" name="<c:out value="${status.expression}"/>"
        multiple size="<c:out value="${question.rowCount}"/>"
        <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if> >
        <c:forEach var="vals" items="${question.attributeDefinition.refersToType.lookupValues}">
            <c:set var="found" value="false"/>
            <c:set var="xanado" value=""/>
            <c:forEach var="msVal" items="${question.multiSelectValues}">
                <c:if test="${msVal.value == vals.id}">
                    <c:set var="found" value="true"/>
                    <c:set var="xanado" value="${msVal.id}"/>
                </c:if>
            </c:forEach>
            <c:set var="cssClass" value="not_linked"/>
            <c:if test="${vals.requires != null && vals.requires != ''}"><c:set var="cssClass" value="linked"/></c:if>
            <c:set var="ll_id" value="${vals.linkId}"/>
            <c:set var="ll_req" value="${vals.requires}"/>
            <c:if test="${dynamicIndex != -1}">
                <c:set var="ll_id" value="${vals.linkId}_${dynamicIndex}"/>
                <c:set var="ll_req" value="${vals.requires}_${dynamicIndex}"/>
            </c:if>

            <option id="pp_<c:out value="${vals.linkId}"/>" linkid="<c:out value="${ll_id}"/>" requires="<c:out value="${ll_req}"/>"
            class="<c:out value="${cssClass}"/>" value="<c:out value="${vals.id}"/>" <c:if test="${found}">selected</c:if>><c:out value="${vals.label}"/></option>
        </c:forEach>
        </select>
        <%@ include file="../../includes/error_message.jsp" %>
        <div style="color:red;display:none;" id="<c:out value="${fieldId}"/>_error">&nbsp;</div>
        <input id="<c:out value="${fieldId}_attid"/>" type="hidden" value="<c:out value="${xanado}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="8"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>

    </spring:bind>
</c:when>

<c:when test="${question.type == 'ORGANISATION'}">
    <spring:bind path="${prefix}.value">


            <span style="white-space: nowrap;"><input id="nav_ou_disp<c:out value="${index}"/>" type="text" class="input_text"
                                                      value="<c:out value="${question.displayValue}"/>"
                                                      name="<c:out value="wrappedDynamicAttributes[${index}].nodeLabel"/>"
                                                      readonly="true"/><input type="button"
                                                                              class="partnerbutton"
                                                                              value="..." id="navOUPopup"
                                                                              <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>
                    <c:out value="${titleAttr}" escapeXml="false"/>
                                                                              onclick="showOrgainisationServerTree('<zynap:message code="select.organisation.unit" javaScriptEscape="true"/>', this, 'orgTree', 'nav_ou_disp<c:out value="${index}"/>', 'nav_ou_id<c:out value="${index}"/>', true);
                    registerCloseFunc('saveUpdateDeleteQuestionnaireOrganisation(\'nav_ou_id<c:out value="${index}"/>\', \'<c:out value="${command.questionnaireId}"/>\', \'<c:out value="${question.daId}"/>\', \'nav_ou_id<c:out value="${index}_attid"/>\', \'nav_ou_disp<c:out value="${index}_error"/>\')');"/></span>
        <input id="nav_ou_id<c:out value="${index}"/>" type="hidden" name="<c:out value="${status.expression}"/>"
        value="<c:out value="${status.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>
        <div style="color:red;display:none;" id="nav_ou_disp<c:out value="${index}"/>_error">&nbsp;</div>
        <input id="nav_ou_id<c:out value="${index}"/>_attid" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="9"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'SUBJECT'}">
    <spring:bind path="${prefix}.value">
                <span style="white-space: nowrap;"><input id="nav_ou_disp<c:out value="${index}"/>" type="text" class="input_text"
                                                          value="<c:out value="${question.displayValue}"/>"
                                                          name="<c:out value="wrappedDynamicAttributes[${index}].nodeLabel"/>"
                                                          readonly="true"/><input type="button"
                                                                                  class="partnerbutton"
                                                                                  value="..." id="navOUPopup"
                                                                                  <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>
                        <c:out value="${titleAttr}" escapeXml="false"/>
                                                                                  onclick="showSubjectServerTree('<zynap:message code="select.subject" javaScriptEscape="true"/>', this, 'subjectTree', 'nav_ou_disp<c:out value="${index}"/>','nav_ou_id<c:out value="${index}"/>', true);
                    registerCloseFunc('saveUpdateDeleteQuestionnaireSubject(\'nav_ou_id<c:out value="${index}"/>\', \'<c:out value="${command.questionnaireId}"/>\', \'<c:out value="${question.daId}"/>\', \'nav_ou_id<c:out value="${index}_attid"/>\', \'nav_ou_disp<c:out value="${index}_error"/>\')');"
                        />
            </span>
        <input id="nav_ou_id<c:out value="${index}"/>" type="hidden" name="<c:out value="${status.expression}"/>"
        value="<c:out value="${status.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>
        <div style="color:red;display:none;" id="nav_ou_disp<c:out value="${index}"/>_error">&nbsp;</div>
        <input id="nav_ou_id<c:out value="${index}"/>_attid" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="10"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'POSITION'}">
    <spring:bind path="${prefix}.value">
        <zynap:message code="select.position" var="ouMsg" javaScriptEscape="true"/>

            <span style="white-space: nowrap;"><input id="nav_ou_disp<c:out value="${index}"/>" type="text" class="input_text"

                                                      value="<c:out value="${question.displayValue}"/>"
                                                      name="<c:out value="wrappedDynamicAttributes[${index}].nodeLabel"/>"
                                                      readonly="true"/><input type="button"
                                                                              class="partnerbutton"
                                                                              value="..." id="navOUPopup"
                                                                              <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>
                                                                              <c:out value="${titleAttr}" escapeXml="false"/>
                                                                              onclick="showPositionServerTree('<c:out value="${ouMsg}"/>', this, 'positionTree', 'nav_ou_disp<c:out value="${index}"/>', 'nav_ou_id<c:out value="${index}"/>',true);
                    registerCloseFunc('saveUpdateDeleteQuestionnairePosition(\'nav_ou_id<c:out value="${index}"/>\', \'<c:out value="${command.questionnaireId}"/>\', \'<c:out value="${question.daId}"/>\', \'nav_ou_id<c:out value="${index}_attid"/>\', \'nav_ou_disp<c:out value="${index}_error"/>\')');"/></span>
        <input id="nav_ou_id<c:out value="${index}"/>" type="hidden" name="<c:out value="${status.expression}"/>"
        value="<c:out value="${status.value}"/>"/>
        <%@ include file="../../includes/error_message.jsp" %>
        <div style="color:red;display:none;" id="nav_ou_disp<c:out value="${index}"/>_error">&nbsp;</div>
        <input id="nav_ou_id<c:out value="${index}"/>_attid" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="11"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'COMMENTS'}">
    <spring:bind path="command.wrappedDynamicAttributes[${index}].lastComment">

        <div class="blogContent" id="blogX<c:out value="${index}"/>">
        <p id="bcpartnerval<c:out value="${index}"/>"></p>
        <c:forEach var="commentBean" items="${question.blogComments}">
            <p>
                <i><c:out value="${commentBean.displayDate}"/>&nbsp;<c:out value="${commentBean.addedBy.label}"/>&nbsp;<fmt:message key="generic.wrote"/></i>
                <br/>
                <c:out value="${commentBean.comment}"/>
            </p>
        </c:forEach>
        </div>
        <input type="button" id="btnBlogX<c:out value="${index}"/>" class="partnerbutton" value="..."
        <c:if test="${disbledVar || queDisabled}">disabled="true"</c:if>
        onclick="showBlogPopup('<zynap:message code="add.question.blog.comment" javaScriptEscape="true"/>', 'btnBlogX<c:out value="${index}"/>', 'blogComment<c:out value="${index}"/>', 'bcpartnerval<c:out value="${index}"/>');
        registerCloseFunc('saveUpdateDeleteQuestionnaireBlogComments(\'tappx<c:out value="${index}"/>\', \'<c:out value="${command.questionnaireId}"/>\', \'<c:out value="${question.daId}"/>\', \'blogX<c:out value="${index}_attid"/>\', \'<c:out value="${command.userId}"/>\', \'blogX<c:out value="${index}_error"/>\')');"/>
        <%@ include file="blogcommentsnippet.jsp" %>
        <div style="color:red;display:none;" id="blogX<c:out value="${index}"/>_error">&nbsp;</div>
        <input name="daId" id="<c:out value="${fieldId}_daId"/>" type="hidden" value="<c:out value="${question.daId}"/>"/> <!-- needed - clonning-->
        <input name="eventJsId" id="<c:out value="${fieldId}_eventJsId"/>" type="hidden" value="12"/> <!-- determine javascript function to attach - uses clonning, calculated top to bottom see questionnaire.js-->
        <input name="managerWriteOnly" id="<c:out value="${fieldId}_manwriteonly"/>" type="hidden" value="<c:out value="${disbledVar}"/>"/>
    </spring:bind>

    <spring:bind path="command.wrappedDynamicAttributes[${index}].attributeValueId">
        <input id="blogX<c:out value="${index}_attid"/>" name="<c:out value="${status.expression}"/>" type="hidden" value="<c:out value="${question.attributeValueId}"/>"/>
    </spring:bind>
</c:when>

<c:when test="${question.type == 'ENUMMAPPING'}">
    <span id="SUM_ENUM<c:out value="${question.daId}"/>">
    <c:out value="${question.displayValue}"/>
    </span>
</c:when>

<c:when test="${question.type == 'SUM'}">
    <span id="SUM_ENUM<c:out value="${question.daId}"/>">
    <c:out value="${question.displayValue}"/>
    </span>
</c:when>

<c:when test="${question.type == 'LASTUPDATED'}">
        <span id="LASTUP">
            <c:out value="${question.displayValue}"/>
        </span>
</c:when>

<c:when test="${question.type == 'LASTUPDATEDBY'}">
        <span id="LASTUPBY">
            <c:out value="${question.displayValue}"/>
        </span>
</c:when>

<c:otherwise>
    <c:out value="${question.displayValue}"/>
</c:otherwise>

</c:choose>