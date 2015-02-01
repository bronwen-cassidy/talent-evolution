<%@ include file="../includes/include.jsp" %>

<fmt:message key="unauth.title" var="title"/>
<zynap:infobox title="${title}" >
    <div class="error">
        <fmt:message key="unauth.message"/>
    </div>
</zynap:infobox>
