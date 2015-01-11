<%@ page import="com.zynap.talentstudio.analysis.populations.IPopulationEngine,
                 AccessType"%>

    <fmt:message key="analysis.population.label" var="label"/>

    <tr>
        <td class="infolabel"><c:out value="${label}"/>&nbsp;:&nbsp;*</td>
        <spring:bind path="command.population.label">
            <td class="infodata">
                <input type="text" maxlength="100" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                <%@include file="../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>

    <fmt:message key="analysis.population.source" var="subtype"/>


    <tr>
        <spring:bind path="command.population.type">
            <td class="infolabel"><c:out value="${subtype}"/>&nbsp;:&nbsp;*</td>
            <td class="infodata">
               <c:choose>
                <c:when test="${firstPage}">
                <select name="<c:out value="${status.expression}"/>">
                    <option value="" <c:if test="${command.population.type == null}">selected</c:if>><fmt:message key="please.select"/></option>
                    <option value="<%=IPopulationEngine.P_POS_TYPE_%>" <c:if test="${command.population.type == 'P'}"> selected</c:if>><fmt:message key="analysis.population.type.position"/></option>
                    <option value="<%=IPopulationEngine.P_SUB_TYPE_%>" <c:if test="${command.population.type == 'S'}"> selected</c:if>><fmt:message key="analysis.population.type.subject"/></option>
                </select>
                <%@include file="../includes/error_message.jsp" %>
                </c:when>
                <c:otherwise>
                     <fmt:message key="analysis.population.type.simple${command.population.type}"/>
                </c:otherwise>
              </c:choose>
            </td>
        </spring:bind>
    </tr>    

    <tr>
        <spring:bind path="command.population.scope">
            <td class="infolabel"><fmt:message key="analysis.scope"/>&nbsp;:&nbsp;*</td>
            <td class="infodata">
                <c:choose>
                    <c:when test="${command.scopeChangeable}">
                        <%@include file="population_scope_select.jsp"%>
                        <%@include file="../includes/error_message.jsp" %>
                     </c:when>
                     <c:otherwise>
                        <fmt:message key="scope.Public"/>
                        <input type="hidden" name="<c:out value="${status.expression}"/>" value="<%=AccessType.PUBLIC_ACCESS%>" />
                     </c:otherwise>
                </c:choose>
            </td>
        </spring:bind>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="analysis.population.description"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.population.description">
            <td class="infodata">
                <textarea name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textarea>
            </td>
        </spring:bind>
    </tr>

    <c:if test="${!empty command.groups}">        
        <tr>
            <td class="infolabel"><fmt:message key="report.groups"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <c:set var="selectSize" value="6"/>
                <c:if test="${command.groupsSize < 6}">
                    <c:set var="selectSize" value="${command.groupsSize}"/>
                </c:if>
                <spring:bind path="command.groupIds">
                    <select multiple="true" name="<c:out value="${status.expression}"/>" size="<c:out value="${selectSize}"/>" id="grpPopId">
                        <c:forEach var="group" items="${command.groups}">
                            <option value="<c:out value="${group.value.id}"/>" <c:if test="${group.selected}">selected</c:if> ><c:out value="${group.value.label}"/></option>
                        </c:forEach>
                    </select>
                    <%@include file="../includes/error_message.jsp" %>
                </spring:bind>
                <span><input type="button" name="clearSelect" class="inlinebutton" value="<fmt:message key="clear"/>" onclick="clearSelectionOptions('grpPopId')"/> </span>
            </td>
        </tr>
    </c:if>


