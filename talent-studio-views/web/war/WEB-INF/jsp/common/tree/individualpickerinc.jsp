<%@ include file="../../includes/include.jsp" %>

<script type="text/javascript">

    $(document).ready(function() {
        $("select[multiple]").asmSelect({
            addItemTarget: 'bottom',
            animate: false,
            highlight: true,
            sortable: false
        });

    });

</script>

<spring:bind path="command.userIds">
    <label for="people"><fmt:message key="securitydomain.assign.users"/>&nbsp;:&nbsp;</label>
    <select id="people" multiple="multiple" name="<c:out value="${status.expression}"/>" title="Click to Select Users">
        <c:forEach var="us" items="${command.users}" varStatus="usIndex">
            <option id="asm0option<c:out value="${usIndex.index}"/>" value="<c:out value="${us.value.id}"/>" <c:if test="${us.selected}">selected="selected"</c:if> > <c:out value="${us.value.loginInfo.username}"/> </option>
        </c:forEach>
    </select>
</spring:bind>