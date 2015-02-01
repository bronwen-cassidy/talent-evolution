<%@ include file="../../../includes/include.jsp" %>

<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ page import="com.zynap.talentstudio.web.portfolio.DocumentSearchController"%>
 <%--
Almost an exact copy of common/portfolio/search/documentsearchresults.jsp.
The only difference it includes checkboxes and the subsequent search button and has no "more like this" link beneath each portfolio item
--%>

<fmt:message key="search.results" var="resultsLabel" /> 

<zynap:infobox title="${resultsLabel}" id="docresults">

    <spring:bind path="command">
        <%@include file="../../../includes/error_message.jsp" %>
    </spring:bind>

    <c:choose>
        <c:when test="${empty command.mappedResults}">
            <div class="infomessage" id="no_results"><fmt:message key="no.results"/></div>
        </c:when>
        <c:otherwise>
            <zynap:form method="post" name="_subsearch" >
                <input type="hidden" name="<%=DocumentSearchController.SUB_SEARCH_PARAM%>" value="true"/>

                <table class="infotable">
                    <c:forEach var="entry" items="${command.mappedResults}">
                        <tr>
                            <td class="infodata">
                                <spring:bind path="command.selectedResults">
                                        <c:forEach var="result" items="${entry.value}" varStatus="varStatus">
                                            <%-- The collection being iterated may contain multiple references to the same artefact - therefore only add 1 link to the artefact --%>
                                            <c:if test="${varStatus.count == 1}">
                                                <strong><c:out value="${result.artefactTitle}"/></strong>
                                                <br/>
                                            </c:if>
                                            <div class="autonomyResults">
                                                <%--<input type="checkbox" class="checkbox" name="selectedResults" value="<c:out value="${result.itemId}"/>"/>&nbsp;--%>
                                                <strong><c:out value="${result.resultWeight}"/>%</strong>&nbsp;|&nbsp;
                                                <a href="#" onclick="setHiddenFromButton('hidden_node_id_results', '<c:out value="${result.itemId}"/>', 'navigationResults', '_target3=3');">
                                                    <c:out value="${result.contentTitle}"/>
                                                </a>
                                                <br/>
                                                <c:out value="${result.summary}" escapeXml="false"/>
                                            </div>
                                        </c:forEach>
                                </spring:bind>
                            </td>
                        </tr>
                    </c:forEach>
                    <%--<tr>--%>
                        <%--<td class="infobutton">--%>
                            <%--<input class="inlinebutton" type="button" name="_subsearch" value="<fmt:message key="documentsearch.subsearch"/>" onclick="setHiddenFromButton('null', 'null', '_subsearch', '_target0=0');"/>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                </table>
            </zynap:form>
        </c:otherwise>
    </c:choose>

</zynap:infobox>


<zynap:form method="post" name="navigationResults" >
     <input id="hidden_node_id_results" type="hidden" name="<%=ParameterConstants.ITEM_ID%>"/>
     <input type="hidden" name="_target3" value="_target3"/>
</zynap:form>
