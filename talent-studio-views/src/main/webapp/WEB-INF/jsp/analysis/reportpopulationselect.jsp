<c:choose>
    <c:when test="${command.drillDown}">
        <tr>
            <td class="infolabel"><fmt:message key="report.population"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <c:out value="${command.report.defaultPopulation.label}"/>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="report.horizontal.criteria"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                 <c:out value="${command.horizontalCriteriaLabel}"/>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="report.vertical.criteria"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                 <c:out value="${command.verticalCriteriaLabel}"/>
            </td>
        </tr>
    </c:when>
    <c:otherwise>
        <tr>
           <td class="infolabel"><fmt:message key="report.selected.population"/>&nbsp;:&nbsp;</td>
           <td class="infodata"><c:out value="${command.population.label}"/></td>
        </tr>
        <tr>
           <td class="infolabel"><fmt:message key="report.population"/>&nbsp;:&nbsp;</td>
           <td class="infodata">
               <c:set var="opSize" value="8"/>
               <c:if test="${popCount < 8}"><c:set var="opSize" value="${popCount}"/> </c:if> 
                <spring:bind path="command.populationIds">
                    <select id="pop2" name="<c:out value="${status.expression}"/>" multiple size="<c:out value="${opSize}"/>">
                         <c:forEach var="pop" items="${populations}">
                            <c:set var="found" value="false"/>
                            <c:forEach var="selectedPop" items="${command.populationIds}">
                                <c:if test="${selectedPop == pop.id}">
                                    <c:set var="found" value="true"/>                                    
                                </c:if>
                            </c:forEach>
                            <option value="<c:out value="${pop.id}"/>" <c:if test="${found}">selected</c:if>><c:out value="${pop.label}"/></option>
                        </c:forEach>                        
                    </select>
                    <%@include file="../includes/error_message.jsp" %>
                </spring:bind>
            </td>
        </tr>
    </c:otherwise>
</c:choose>


