<%@ include file="../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <div class="infomessage"><fmt:message key="select.workflow.to.chart"/></div>
    
    <spring:bind path="command">
        <%@ include file="../includes/error_messages.jsp" %>
    </spring:bind>
    
     <div id="workflows" class="row">
         <select name="workflow-select" class="wrk-flow">
            <c:forEach var="workflow" items="${workflows}">
                <option value="<c:out value="${workflow.id}"/>"><c:out value="${workflow.label}"/></option>    
            </c:forEach>                     
         </select>
     </div>
    
    <div id="attributes" class="row">
        
    </div>
    
</zynap:infobox>

<script type="text/javascript">
    
    
    
</script>