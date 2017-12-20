<%@ include file="../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <form:form method="post" action="savemydashboard.htm" modelAttribute="command" id="mydashboardchartform">
        <div id="workflows" class="row">	
            <span class="column col-6">
                <div class="infomessage">Please select the form containing the attributes you wish to chart</div>
                 <form:select path="workflowId" id="wrkflow-select" class="validate[required]">
                     <form:option value=""><fmt:message key="please.select"/></form:option>
                     <c:forEach var="workflow" items="${workflows}">
                        <form:option value="${workflow.id}" label="${workflow.label}" />
                     </c:forEach>
                 </form:select>
           </span>
        </div>

        <div id="chart-label" class="row">	
            <span class="column col-6">
                <div class="infomessage"><fmt:message key="label.for.chart"/></div>
                <form:input path="chartLabel" required="yes"/>
           </span>
        </div>

        <div id="attributes">
            
        </div>

        <div id="series">

        </div>

        <div id="submit-row" class="row">
            <input type="submit" value="<fmt:message key="save"/>"/>
            <input type="button" id="frmadd" value="<fmt:message key="add"/>"/>
            <input type="button" id="frmcancel" value="<fmt:message key="cancel"/>" onclick="document.forms.cncl.submit()"/>
        </div>

    </form:form>

    <form name="cncl" action="canceladddashboard.htm" method="get"></form>

</zynap:infobox>

<script type="text/javascript">

    $(function () {
        
        $('#frmadd').hide();
        
        $('#wrkflow-select').on('change', function () {
            var workflowId = $('#wrkflow-select').val();
            if (workflowId === '') {
                $('#attributes').html("");
                $('#series').html("");
            } else {
                $.get('loadattributes.htm?ts=' + new Date().getTime(), {wfId: workflowId}, function (data) {
                    $('#attributes').html(data);
                    $('#frmadd').show();
                });
            }
        });

        $('#frmadd').on('click', function() {
            var workflowId = $('#wrkflow-select').val();
            $.get('addseries.htm?ts=' + new Date().getTime(), {wfId: workflowId}, function(data) {
                $('#series').append(data);    
            });
        });

        $("#mydashboardchartform").validate();
    });

</script>