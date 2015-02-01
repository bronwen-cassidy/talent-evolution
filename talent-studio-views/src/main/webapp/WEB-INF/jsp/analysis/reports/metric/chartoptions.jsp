<zynap:form method="post" name="genBarCt">
    <table class="infotable" id="chrtoptions">
        <input type="hidden" name="_target4" value="4"/>
        <tr>
            <td class="infolabel"><fmt:message key="select.metrics"/>&nbsp:&nbsp;*</td>
            <td class="infodata">
                <spring:bind path="command.selectedMetrics">
                    <c:forEach var="column" items="${command.columns}" varStatus="columnIndex">
                        <div style="white-space:nowrap">
                            <input type="checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${column.id}"/>" <c:if test="${column.metricSelected}">checked</c:if>/>
                            &nbsp;
                            <c:out value="${column.label}"/>
                        </div>
                    </c:forEach>
                    <%@include file="../../../includes/error_message.jsp" %>
                </spring:bind>
            </td>
        </tr>
        <%@ include file="../common/chartselect.jsp"%>
    </table>
</zynap:form>