<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="row">
    
    <span class="column col-3">
        <div class="infomessage"><fmt:message key="please.select.series.attribute"/></div>	
        <form:select path="command.series[${index}].key" class="series-y-attrs" id="seriesid-<c:out value='${index}'/>">
            <form:option value="-1"><fmt:message key="please.select"/></form:option>
            <c:forEach var="attr" items="${series}">
                <form:option value="${attr.id}" label="${attr.label}"/>
            </c:forEach>
        </form:select>
    </span>
    
    <span class="column col-3">
        <div class="infomessage"><fmt:message key="please.select.series.label"/></div>
        <form:input path="command.series[${index}].value" id="serieslabel-<c:out value='${index}'/>"/>
    </span>
</div>

<script type="text/javascript">

    $(function () {

        $('.series-y-attrs').on('change', function() {
            var txt = $(this).find('option:selected').text();
            var elemId = $(this).attr('id');
            var postfix = elemId.substring(elemId.indexOf('-') + 1, elemId.length);
            $('#serieslabel-' + postfix).val(txt);
        });
    });

</script>