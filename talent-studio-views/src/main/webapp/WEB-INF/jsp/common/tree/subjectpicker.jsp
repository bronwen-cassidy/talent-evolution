<c:if test="${command.filter.nodeType =='S'}">
    <table class="pager">
        <form:form commandName="command" method="post">
            <tr>
                <td class="pager" colspan="2"><h1><fmt:message key="picker.search.subject"/></h1></td>
            </tr>
            <tr>
                <td class="pager"><fmt:message key="admin.add.user.firstname"/></td>
                <td class="pager">
                    <form:input path="filter.firstName"/>
                    <form:errors path="filter.firstName" cssClass="error"/>
                </td>
            </tr>
            <tr>
                <td class="pager"><fmt:message key="admin.add.user.lastname"/></td>
                <td class="pager">
                    <form:input path="filter.secondName"/>
                    <form:errors path="filter.secondName" cssClass="error"/>
                </td>
            </tr>
            <tr>
                <td class="pager">&nbsp;</td>
                <td class="pager"><input type="submit" value="<fmt:message key="search"/>" name="_target1"/></td>
            </tr>
        </form:form>
    </table>
    <table class="pager">
        <c:if test="${(empty command.results) &&  command.searchRun}">
            <tr>
                <td class="pager" colspan="2"><h1><fmt:message key="no.results"/></h1></td>
            </tr>
        </c:if>

        <c:if test="${! empty command.results}">
            <tr>
                <td class="pager" colspan="2"><h1><fmt:message key="picker.search.subject.results"/></h1></td>
            </tr>

            <c:forEach items="${command.results}" var="item">
                <tr>


                    <c:set var="label" value="${item.label} - ${item.primaryPositionDisplay}"/>
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

    <h1><fmt:message key="picker.subject.select.title"/></h1>
</c:if>