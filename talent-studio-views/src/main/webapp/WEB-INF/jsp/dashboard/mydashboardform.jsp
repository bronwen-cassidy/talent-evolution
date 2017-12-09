<%@ include file="../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <form:form method="post" action="/talentarena/dashboards/savemydashboard" modelAttribute="command">
        <div id="workflows" class="row">	
            <span class="column col-3">
                <div class="infomessage">Please select the form containing the attributes you wish to chart</div>
                 <form:select path="workflowId" id="wrkflow-select">
                     <form:option value="-1">Please Select</form:option>
                     <c:forEach var="workflow" items="${workflows}">
                        <form:option value="${workflow.id}" label="${workflow.label}" />
                     </c:forEach>
                 </form:select>
           </span>
        </div>

        <div id="attributes" class="row">

        </div>

        <div id="submit-row" class="row">
            <input type="submit" value="<fmt:message key="save"/>"/>
        </div>

    </form:form>

</zynap:infobox>

<script type="text/javascript">

    $(function () {
        $('#wrkflow-select').on('change', function () {
            var workflowId = $('#wrkflow-select').val();
            $.get('/talentarena/dashboards/loadattributes.htm?ts=' + new Date().getTime(), {wfId: workflowId}, function (data) {
                $('#attributes').html(data);
            });
        });
    });

</script>