<%@ page import="IPopulationEngine"%>
<%@ page import="ISearchConstants" %>
<%@ include file="../includes/include.jsp" %>

<c:set var="hasOuTree" value="${orgUnitTree != null && !empty orgUnitTree}"/>
<c:set var="hasPositionTree" value="${positionTree != null && !empty positionTree}"/>
<c:set var="hasSubjectTree" value="${subjectTree != null && !empty subjectTree}"/>

<%@ include file="../questionnaires/questionnairepopupinclude.jsp" %>

<fmt:message key="analysis.population.add.1" var="msg"/>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<zynap:infobox title="${msg}">
    <zynap:form name="population" method="post" encType="multipart/form-data">
        <spring:bind path="command.population.id">
            <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.population.id}"/>">
        </spring:bind>

        <table class="infotable" id="popDetails">
            <!-- Add data here -->

            <c:set var="firstPage" value="false"/>
            <%@ include file="populationheaderinputs.jsp" %>
            <tr>
                <td class="infolabel"><fmt:message key="criteria.active"/></td>
                <spring:bind path="command.population.activeCriteria">
                    <td class="infodata">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="1" <c:if test="${command.population.activeNodesOnly}">selected</c:if>><fmt:message key="subject.search.active.option.active"/></option>    
                            <option value="2" <c:if test="${command.population.inactiveNodesOnly}">selected</c:if>><fmt:message key="subject.search.active.option.inactive"/></option>
                            <option value="3" <c:if test="${command.population.activeCriteria == 3}">selected</c:if>><fmt:message key="subject.search.active.option.both"/></option>
                        </select>
                        <%@include file="../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infoheading" colspan="2"><fmt:message key="analysis.population.criteria"/></td>
            </tr>
            <tr>
                <td class="infodata" colspan="2">
                    <table class="infotable" id="popCriteria">
                       <c:if test="${command.populationCriterias!=null  && not empty command.populationCriterias}">
                        <tr>
                            <td class="infolabel">
                                <fmt:message key="analysis.population.criteria.bkt"/>
                            </td>
                            <td class="infolabel">
                                <fmt:message key="analysis.population.criteria.not"/>
                            </td>
                            <td class="infolabel">
                                <fmt:message key="analysis.population.criteria.attribute"/>
                            </td>
                            <td class="infolabel">
                                <fmt:message key="analysis.population.criteria.comparator"/>
                            </td>
                            <td class="infolabel">
                                <fmt:message key="analysis.population.criteria.value"/>
                            </td>
                            <td class="infolabel">
                                <fmt:message key="analysis.population.criteria.bkt"/>
                            </td>
                            <td class="infolabel">
                                <fmt:message key="analysis.population.criteria.operator"/>
                            </td>
                        </tr>
                        </c:if>
                        <c:forEach var="criteria" items="${command.populationCriterias}" varStatus="count" >
                            <tr>
                                <spring:bind path="command.populationCriterias[${count.index}].leftBracket">
                                    <td class="infodata">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <option value="" <c:if test="${criteria.leftBracket == ''}">selected="selected"</c:if>>&nbsp;</option>
                                            <option value="<%=IPopulationEngine.LEFT_BRCKT_%>" <c:if test="${criteria.leftBracket == '('}">selected="selected"</c:if>> ( </option>
                                        </select>
                                        <%@include file="../includes/error_message.jsp" %>                                        
                                    </td>
                                </spring:bind>
                                <spring:bind path="command.populationCriterias[${count.index}].inverse">
                                    <td class="infodata">
                                        <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="true"
                                                <c:if test="${criteria.inverse}">checked="checked"</c:if>>
                                    </td>
                                </spring:bind>
                                <spring:bind path="command.populationCriterias[${count.index}].attribute">
                                    <td class="infodata">
                                        <c:set var="fieldId"><zynap:id><c:out value="${status.expression}"/></zynap:id></c:set>
                                        <c:set var="btnAction">javascript:showCriteriaTree('<c:out value="${count.index}"/>', '<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'criteriaTree', '<c:out value="${fieldId}_label"/>', '<c:out value="${fieldId}"/>')</c:set>
                                        <%-- determine the correct label --%>
                                        <fmt:message key="please.select" var="label"/>
                                        <c:if test="${criteria.attributeSet}">
                                            <c:set var="label" value="${criteria.attributeLabel}"/>
                                        </c:if>

                                        <span style="white-space:nowrap;"><input id="<c:out value="${fieldId}"/>_label" type="text" class="input_text"
                                            value="<c:out value="${label}"/>"
                                                name="<c:out value="${status.expression}_label"/>"
                                                readonly="true"
                                        /><input type="button"
                                                class="partnerbutton"
                                                value="..." id="navOUPopup"
                                                onclick="<c:out value="${btnAction}"/>"/></span>
                                        <input id="<c:out value="${fieldId}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />

                                        <%@include file="../includes/error_message.jsp" %>
                                    </td>
                                </spring:bind>
                                <spring:bind path="command.populationCriterias[${count.index}].comparator">
                                    <td class="infodata">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <c:forEach var="comparator" items="${criteria.comparators}">
                                                <option value="<c:out value="${comparator}"/>" <c:if test="${criteria.comparator == comparator}">selected="selected"</c:if>><c:out value="${comparator}"/></option>
                                            </c:forEach>
                                        </select>
                                        <%@include file="../includes/error_message.jsp" %>
                                    </td>
                                </spring:bind>
                                <td class="infodata" width="100%">
                                    <c:choose>
                                        <c:when test="${criteria.attributeDefinition != null}">
                                            <c:choose>
                                                <c:when test="${criteria.organisationUnit}">
                                                    <spring:bind path="command.populationCriterias[${count.index}].attributeDefinition.value">
                                                        <zynap:message code="organisation.unit" var="ouMsg" javaScriptEscape="true"/>
                                                        <span style="white-space:nowrap"><input id="oufld4<c:out value="${count.index}"/>" type="text" class="input_text" value="<c:out value="${criteria.nodeLabel}"/>" name="populationCriterias[<c:out value="${count.index}"/>].nodeLabel" readonly="true"
                                                                /><input type="button" id="pick_ou0" <c:if test="${!hasOuTree}">disabled</c:if> class="partnerbutton" value="..." onclick="popupShowTree('<c:out value="${ouMsg}"/>', this, 'orgTree', 'oufld4<c:out value="${count.index}"/>', 'oufld5<c:out value="${count.index}"/>');"/></span>
                                                        <input id="oufld5<c:out value="${count.index}"/>" type="hidden" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.value" value="<c:out value="${command.populationCriterias[count.index].attributeDefinition.value}"/>"/>
                                                        <%@include file="../includes/error_message.jsp" %>
                                                    </spring:bind>
                                                </c:when>
                                                <c:when test="${criteria.position}">
                                                    <spring:bind path="command.populationCriterias[${count.index}].attributeDefinition.value">
                                                        <zynap:message code="positions" var="posMsg" javaScriptEscape="true"/>
                                                            <span style="white-space:nowrap"><input id="oufld4<c:out value="${count.index}"/>" type="text" class="input_text" value="<c:out value="${criteria.nodeLabel}"/>" name="populationCriterias[<c:out value="${count.index}"/>].nodeLabel" readonly="true"
                                                                    /><input type="button" id="pick_ou1"  class="partnerbutton" value="..." onclick="popupShowServerTree('<c:out value="${posMsg}"/>', this, 'positionTree', 'oufld4<c:out value="${count.index}"/>', 'oufld5<c:out value="${count.index}"/>');"/></span>
                                                            <input id="oufld5<c:out value="${count.index}"/>" type="hidden" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.value" value="<c:out value="${command.populationCriterias[count.index].attributeDefinition.value}"/>"/>
                                                        <%@include file="../includes/error_message.jsp" %>
                                                    </spring:bind>
                                                </c:when>
                                                <c:when test="${criteria.subject}">
                                                    <spring:bind path="command.populationCriterias[${count.index}].attributeDefinition.value">
                                                        <zynap:message code="subjects" var="personMsg" javaScriptEscape="true"/>
                                                            <span style="white-space:nowrap"><input id="oufld4<c:out value="${count.index}"/>" type="text" class="input_text" value="<c:out value="${criteria.nodeLabel}"/>" name="populationCriterias[<c:out value="${count.index}"/>].nodeLabel" readonly="true"
                                                                    /><input type="button" id="pick_ou2" class="partnerbutton" value="..." onclick="popupShowServerTree('<c:out value="${personMsg}"/>', this, 'subjectTree', 'oufld4<c:out value="${count.index}"/>', 'oufld5<c:out value="${count.index}"/>');"/></span>
                                                            <input id="oufld5<c:out value="${count.index}"/>" type="hidden" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.value" value="<c:out value="${command.populationCriterias[count.index].attributeDefinition.value}"/>"/>
                                                        <%@include file="../includes/error_message.jsp" %>
                                                    </spring:bind>
                                                </c:when>
                                                <c:when test="${criteria.derivedAttribute}">
                                                    <spring:bind path="command.populationCriterias[${count.index}].attributeDefinition.value">
                                                        <input type="text" class="input_text" name="populationCriterias[<c:out value="${count.index}"/>].attributeDefinition.value" value="<c:out value="${command.populationCriterias[count.index].attributeDefinition.value}"/>"/>
                                                        <%@include file="../includes/error_message.jsp" %>
                                                    </spring:bind>
                                                </c:when>                                                
                                                <c:otherwise>
                                                    <c:set var="prefix" value="command.populationCriterias[${count.index}].attributeDefinition" scope="request"/>
                                                    <%@ include file="populations/editcriteriasnippet.jsp" %>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" class="input_text" name="dummy" value="" disabled>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <spring:bind path="command.populationCriterias[${count.index}].rightBracket">
                                    <td class="infodata">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <option value="" <c:if test="${criteria.rightBracket == ''}">selected="selected"</c:if>>&nbsp;</option>
                                            <option value="<%=IPopulationEngine.RIGHT_BRCKT_%>" <c:if test="${criteria.rightBracket ==')'}">selected="selected"</c:if>> ) </option>
                                        </select>
                                        <%@include file="../includes/error_message.jsp" %>
                                    </td>
                                </spring:bind>

                                <spring:bind path="command.populationCriterias[${count.index}].operator">
                                    <td class="infodata">
                                        <select name="<c:out value="${status.expression}"/>">
                                            <option value="" <c:if test="${criteria.operator == null}">selected="selected"</c:if>><fmt:message key="please.select"/></option>
                                            <c:forEach var="operator" items="${operators}">
                                                <option value="<c:out value="${operator}"/>" <c:if test="${criteria.operator == operator}">selected="selected"</c:if>><c:out value="${operator}"/></option>
                                            </c:forEach>
                                        </select>
                                        <%@include file="../includes/error_message.jsp" %>
                                    </td>
                                </spring:bind>
                                <td class="infodata">
                                    <input class="inlinebutton" type="button" name="" value="<fmt:message key="delete"/>" onclick="javascript:removeLine('<c:out value="${count.index}"/>');"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <input class="inlinebutton" type="button" name="addCriteria" value="<fmt:message key="add"/>" onclick="javascript:addLine();"/>
                </td>
            </tr>

            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onClick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>

        </table>

        <input type="hidden" name="index" value="">

    </zynap:form>
</zynap:infobox>

<c:url value="/picker/populationpicker.htm" var="pickerUrl">
    <c:param name="populationType" value="${command.type}"/> 
</c:url>
 
<zynap:window elementId="criteriaTree" src="${pickerUrl}"/>

<%-- separate form to hold hidden parameters used by javascript --%>
<form name="temp">
    <input type="hidden" name="index" value="">
</form>

