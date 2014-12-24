<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%--<c:url value="/talentarena/home.htm" var="home"/>--%>
<!--<script defer="defer" language="JavaScript" type="text/javascript">-->

<%--window.open('<c:out value="${home}"/>','TheNewpop','toolbar=no, location=no,directories=1,status=1,menubar=1, scrollbars=1,resizable=1');--%>

<!--</script>-->

<%-- Redirected because we can't set the welcome page to a virtual URL. --%>
<c:redirect url="/talentarena/home.htm"/>