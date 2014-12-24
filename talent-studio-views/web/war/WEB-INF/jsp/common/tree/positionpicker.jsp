<c:if test="${command.filter.nodeType =='P'}">
    <table class="pager">
        <form:form commandName="command" method="post">
            <tr>
                <td class="pager" colspan="2"><h1><fmt:message key="picker.search.positions"/></h1></td>
            </tr>
            <tr>
                <td class="pager"><fmt:message key="position.title"/></td>
                <td class="pager">
                    <form:input path="filter.title"/>
                    <form:errors path="filter.title" cssClass="error"/>
                </td>

            </tr>
            <tr>
                <td class="pager">&nbsp;</td>
                <td class="pager"><input type="submit" value="<fmt:message key="search"/>" name="_target1"/></td>
            </tr>
        </form:form>
    </table>
    <table class="pager">

       <c:if test="${(empty command.results) && command.searchRun}">
            <tr>
                <td class="pager" colspan="2"><h1><fmt:message key="no.results"/></h1></td>
            </tr>
        </c:if>
        <c:if test="${! empty command.results}">
            <tr>
                <td class="pager" colspan="2"><h1><fmt:message key="picker.search.positions.results"/></h1></td>
            </tr>
            <c:forEach items="${command.results}" var="item">
                <tr>

                    <c:set var="label" value="${item.label} - ${item.currentHolderInfo}"/>
                    <td class="pager"><c:out value="${label}"/></td>
                    <td class="pager">
                         <a href="javascript:popupServerOK('<c:out value="${command.popupId}"/>','<c:out value="${label}"/>','<c:out value="${item.id}"/>');">
                            <strong><fmt:message key="select"/></strong>
                        </a>
                    </td>

                </tr>

            </c:forEach>
        </c:if>
    </table>

    <p/>

    <h1><fmt:message key="picker.position.select.title"/></h1>
</c:if>
