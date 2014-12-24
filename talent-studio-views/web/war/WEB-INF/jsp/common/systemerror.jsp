<%@ include file="../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.web.utils.ZynapWebUtils"%>

<script type="text/javascript">
    <%-- NOTE: This script MUST NOT user functions from included file as we
         cannot guarantee that the files will have been included!
    --%>

    function exposeError()
    {
        var stk = document.getElementById('errorTag');
        stk.style.display = 'inline';
    }
</script>

<fmt:message key="systemerror.title" var="title"/>
<zynap:infobox title="${title}" >
    <div class="error">
        <fmt:message key="systemerror.message"/>

        <%-- 
            zynap:errortag only outputs the stack dump if displayErrors is true, but it always logs
            the error. So in the first case we wrap it in a friendly display and in the latter we
            just invoke it so that the logging is performed.
        --%>

        <% if (Boolean.valueOf(application.getInitParameter("displayErrors")).booleanValue()) { %>
            <br/><br/>
            Click <a href='javascript:exposeError();'>here</a> to view details of the error.
            <div id='errorTag' style='display:none'>
                <h1>Request</h1>
                <%= ZynapWebUtils.getCurrentURI(request)%>?<%= request.getQueryString() %>
                <br/><br/>
                <h1>Stacktrace</h1>
                <pre><zynap:errorTag/></pre>
            </div>
        <% } else { %>
            <pre><zynap:errorTag/></pre>
        <% } %>
    </div>
</zynap:infobox>
