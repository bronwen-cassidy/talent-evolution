<%@ include file="../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.analysis.populations.IPopulationEngine"%>

<fmt:message key="analysis.population.view" var="msg"/>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_back" method="get" action="listpopulations.htm">
           <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="document.forms._back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <input class="actionbutton" type="button" name="_preview" value="<fmt:message key="preview"/>" onclick="document.forms._preview.submit();"/>
    </zynap:actionEntry>
    <c:if test="${!command.allPopulation}">
        <zynap:evalButton userId="${command.population.userId}">
            <zynap:actionEntry>
                <input class="actionbutton" type="button" name="_edit" value="<fmt:message key="edit"/>" onclick="javascript:document.forms._edit.submit();"/>
            </zynap:actionEntry>
            <zynap:actionEntry>
                <zynap:form name="_delete" method="get" action="deletepopulation.htm">
                   <input type="hidden" name="<%=ParameterConstants.POPULATION_ID%>" value="<c:out value="${command.population.id}"/>"/>
                   <input class="actionbutton" type="button" name="_delete" value="<fmt:message key="delete"/>" onclick="javascript:document.forms._delete.submit();"/>
                </zynap:form>
             </zynap:actionEntry>
        </zynap:evalButton>
    </c:if>
</zynap:actionbox>

<zynap:form method="get" name="_edit" action="editpopulation.htm">
       <input type="hidden" name="<%= ParameterConstants.POPULATION_ID %>" value="<c:out value="${command.population.id}"/>">
</zynap:form>

<zynap:form method="post" name="_preview">
       <input type="hidden" name="_target1" value="1">
</zynap:form>

<zynap:infobox title="${msg}">
    <zynap:form name="population" method="post">
        <table class="infotable" cellspacing="0">

            <fmt:message key="analysis.population.label" var="label"/>
            <spring:bind path="command">
                <%@include file="../includes/error_messages.jsp" %>
            </spring:bind>

            <tr>
                <td class="infolabel"><c:out value="${label}"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <c:out value="${command.population.label}"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="analysis.population.source"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <fmt:message key="analysis.population.type.simple${command.population.type}"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="analysis.scope"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <fmt:message key="scope.${command.population.scope}"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="analysis.population.description"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <zynap:desc><c:out value="${command.population.description}"/></zynap:desc>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="subject.search.active"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <fmt:message key="analysis.population.active${command.population.activeCriteria}"/>   
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="assigned.groups"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <c:forEach var="assignedGroup" items="${command.assignedGroups}">
                        <div><c:out value="${assignedGroup.label}"/></div>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td class="infolabel">
                    <fmt:message key="analysis.population.criteria"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:forEach var="criteria" items="${command.populationCriterias}" varStatus="count" >
                        <c:choose>
                            <c:when test="${criteria.invalid}">
                                <fmt:message key="invalid.criteria"/>
                                <br/>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${criteria.leftBracket}"/>
                                <c:if test="${criteria.inverse}"><fmt:message key="analysis.population.criteria.not"/></c:if>
                                <c:out value="${criteria.attributeLabel}"/>
                                <c:out value="${criteria.comparator}"/>

                                <c:if test="${criteria.attributeDefinition != null}">
                                    <c:choose>
                                        <c:when test="${criteria.node}">
                                            <c:out value="${criteria.nodeLabel}"/>
                                        </c:when>
                                        <c:when test="${criteria.derivedAttribute}">
                                            <c:out value="${criteria.refValue}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="attrType" scope="request" value="${criteria.attrType}"/>
                                            <%@ include file="populations/viewcriteriasnippet.jsp" %>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>

                                <c:out value="${criteria.rightBracket}"/>

                                <c:if test="${criteria.operator != null}">
                                    <c:out value="${criteria.operator}"/>
                                    <br/>
                                </c:if>

                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </td>
            </tr>
        </table>
    </zynap:form>

    <c:if test="${command.resultsetPreview !=null}">
        <%@ include file="reports/loading.jsp" %>
        <div id="results_span">
            <c:set var="resultset" value="${command.resultsetPreview}"/>
            <c:set var="jasperPrint" value="${resultset.filledReport.jasperPrint}"/>
            <c:set var="targetPage" value="1" scope="request"/>
            <%@ include file="reports/tabular/results.jsp" %>
        </div>
    </c:if>

</zynap:infobox>


