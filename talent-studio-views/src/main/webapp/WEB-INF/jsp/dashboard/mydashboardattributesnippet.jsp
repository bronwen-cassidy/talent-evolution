<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="row">
    <span class="column col-3">
        <div class="infomessage"><fmt:message key="please.select.x.axis.attribute"/></div>
        <form:select path="command.xAxisAttributeId" id="x-attrs">
            <form:option value="-1"><fmt:message key="please.select"/></form:option>
            <form:option value="-222">Published Date</form:option>
            <c:forEach var="attr" items="${attributes}">
                <form:option value="${attr.id}" label="${attr.label}"/>
            </c:forEach>                       
         </form:select>
    </span>
    <span class="column col-3">
        <div class="infomessage"><fmt:message key="please.select.y.axis.attribute"/></div>	
        <form:select path="command.yAxisAttributeId" id="y-attrs">
            <form:option value="-1"><fmt:message key="please.select"/></form:option>
            <c:forEach var="attr" items="${series}">
                <form:option value="${attr.id}" label="${attr.label}"/>
            </c:forEach>
        </form:select>
    </span>
    
</div>
<div class="row">

    <span class="column col-3">
        <div class="infomessage"><fmt:message key="please.select.x.axis.label"/></div>
        <form:input path="command.xAxisLabel" id="x-axis-label"/>
    </span>

    <span class="column col-3">
        <div class="infomessage"><fmt:message key="please.select.y.axis.label"/></div>
        <form:input path="command.yAxisLabel" id="y-axis-label"/>
    </span>
</div>

<script type="text/javascript">

    $(function () {
        $('#x-attrs').on('change', function() {
            var txt = $('#x-attrs').find('option:selected').text();
            $('#x-axis-label').val(txt);
        });

        $('#y-attrs').on('change', function() {
            var txt = $('#y-attrs').find('option:selected').text();
            $('#y-axis-label').val(txt);
        });
    });

</script>