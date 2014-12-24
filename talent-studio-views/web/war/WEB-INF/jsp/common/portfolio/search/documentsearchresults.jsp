<%@ include file="../../../includes/include.jsp" %>

<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>

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
            <table class="infotable">
                <c:forEach var="entry" items="${command.mappedResults}">
                    <tr>
                        <td class="infodata">
                            <c:forEach var="result" items="${entry.value}" varStatus="varStatus">
                                <%-- The collection being iterated may contain multiple references to the same artefact - therefore only add 1 link to the artefact --%>
                                <c:if test="${varStatus.count == 1}">
                                    <strong><c:out value="${result.artefactTitle}"/></strong>
                                    <br/>
                                </c:if>

                                <div class="autonomyResults">
                                    <strong><c:out value="${result.resultWeight}"/>%</strong>&nbsp;|&nbsp;
                                    <a href="#" onclick="setHiddenFromButton('hidden_node_id_results', '<c:out value="${result.itemId}"/>', 'navigationResults', '_target3=3');">
                                        <c:out value="${result.contentTitle}"/>
                                    </a>
                                    <br/>
                                        <c:out value="${result.summary}" escapeXml="false"/>
                                    <br/>
                                    <%--<a href="#" onclick="setHiddenFromButton('document_target_id', '<c:out value="${result.documentId}"/>', 'searchAgain', '_target4=4');">--%>
                                        <%--<fmt:message key="more.like.this"/>--%>
                                    <%--</a>--%>
                                </div>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

</zynap:infobox>


<zynap:form method="post" name="navigationResults" >
     <input id="hidden_node_id_results" type="hidden" name="<%=ParameterConstants.ITEM_ID%>"/>
     <input type="hidden" name="_target3" value="_target3"/>
</zynap:form>

<zynap:form method="post" name="searchAgain" >
     <input id="document_target_id" type="hidden" name="docId" value=""/>
</zynap:form>
