<%@ include file="../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <form:form method="post" action="savemydashboard.htm" modelAttribute="command">
        <div id="workflows" class="row">	
            <span class="column col-6">
                <div class="infomessage">Please select the form containing the attributes you wish to chart</div>
                 <form:select path="workflowId" id="wrkflow-select">
                     <form:option value="-1">Please Select</form:option>
                     <c:forEach var="workflow" items="${workflows}">
                        <form:option value="${workflow.id}" label="${workflow.label}" />
                     </c:forEach>
                 </form:select>
           </span>
        </div>

        <div id="chart-label" class="row">	
            <span class="column col-6">
                <div class="infomessage">Please enter a label for the chart</div>
                <form:input path="chartLabel"/>
           </span>
        </div>

        <div id="attributes">
            
        </div>

        <div id="series">

        </div>

        <div id="submit-row" class="row">
            <input type="submit" value="<fmt:message key="save"/>"/>
            <input type="button" id="frmadd" value="<fmt:message key="add"/>"/>
            <input type="button" id="frmcancel" value="<fmt:message key="cancel"/>"/>
        </div>

    </form:form>

</zynap:infobox>

<script type="text/javascript">

    $(function () {
        $('#wrkflow-select').on('change', function () {
            var workflowId = $('#wrkflow-select').val();
            $.get('loadattributes.htm?ts=' + new Date().getTime(), {wfId: workflowId}, function (data) {
                $('#attributes').html(data);
            });
        });
        
        $('#frmcancel').on('click', function() {
            $.get('cancelview.htm?ts=' + new Date().getTime());   
        });

        $('#frmadd').on('click', function() {
            var workflowId = $('#wrkflow-select').val();
            $.get('addseries.htm?ts=' + new Date().getTime(), {wfId: workflowId}, function(data) {
                $('#series').append(data);    
            });
        });
    });

</script>