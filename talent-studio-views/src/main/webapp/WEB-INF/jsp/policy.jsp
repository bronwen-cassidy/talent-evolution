<%@ include file="includes/include.jsp" %>

<fmt:message key="policy.title" var="title" scope="request"/>

<form name="policyform" action="<c:url value="/acceptpolicy.htm"/>" method="post" onSubmit="return buildUserPermits('<c:out value="${userId}"/>');">
    <fmt:message key="policy.infobox.title" var="msg"/>

    <zynap:infobox title="${msg}" id="policy">

        <c:import url="/help/policy.html"/>

        <c:if test="${failure != null && failure == true}">
            <div class="error">
                <fmt:message key="policy.mustagree.text"/>
            </div>
        </c:if>
        <br/>        
        <input id="disagree" class="inlinebutton" type="button" name="Disagree" value="<fmt:message key="policy.disagree.button.text"/>" onclick="document.forms.fm_disagree.submit();"/>
        <input id="accept" class="inlinebutton" type="submit" name="Agree" value="<fmt:message key="policy.agree.button.text"/>"/>
    </zynap:infobox>

</form>

<zynap:form method="get" name="fm_disagree" action="/logout.htm"/>



