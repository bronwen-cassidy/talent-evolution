<%@ page errorPage="systemerror.jsp" %>
<%@ page buffer="64kb" %>
<%@ include file="../includes/include.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-type" content="text/html;charset=ISO-8859-1"/>
    <%--<meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>--%>

    <fmt:message key="${title}" var="title" scope="request"/>
    <title><fmt:message key="application.title"/> - <c:out value="${title}"/></title>

    <%
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache, must-revalidate, max-age=0");
        response.addIntHeader("Expires", -1);
    %>

    <link rel="shortcut icon" href="<c:url value="/images/tscope.ico"/>"/>

    <%@ include file="../includes/stylesheets.jsp" %>

    <script src="<c:url value="/js/jquery-1.7.2.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/jquery-ui-1.8.21.custom.min.js"/>" type="text/javascript"></script>

    <script src="<c:url value="/dwr/engine.js"/>" type="text/javascript"></script>

    <script src="<c:url value="/dwr/interface/navSessionBean.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/dwr/interface/objectivesBean.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/dwr/interface/questionnaireBean.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/dwr/interface/helpTextBean.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/dwr/interface/subjectBean.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/dwr/interface/messageItemBean.js"/>" type="text/javascript"></script>


    <script src="<c:url value="/js/functions.js"/>" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="<c:url value="/js/popups.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/questionnaires.js"/>" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="<c:url value="/js/talentstudio.min.js"/>" type="text/javascript" charset="ISO-8859-1"></script>
</head>

<zynap:message var="timeoutMsg" code="timeout.warning" javaScriptEscape="true"/>
<input type="hidden" id="timeoutWarningMsgIdz" value="<zynap:message code="warning" javaScriptEscape="true"/>"/>

<body OnLoad="placeFocus(self); <c:if test="${!nopoll}">pollTimeOut('<c:out value="${pageContext.session.maxInactiveInterval * 1000}"/>', '<c:out value="${timeoutMsg}"/>', '<c:url value="/logout.htm"/>');</c:if>">

<table style="height:100%;" width="100%" cellspacing="0" cellpadding="0">

    <c:if test="${top != null}">
        <c:import url="${top}"/>
    </c:if>

    <c:if test="${userinfo != null}">
        <c:import url="${userinfo}"/>
    </c:if>

    <c:if test="${arenanav != null}">
        <c:import url="${arenanav}"/>
    </c:if>

    <c:if test="${decoration != null}">
        <%@ include file="/help/topimage.html" %>
    </c:if>

    <tr style="height:100%;" valign="top">
        <td>
            <table style="height:100%;" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                    <c:set var="navflag" value="nonav"/>
                    <c:if test="${nav != null}">
                        <td valign="top" class="navigation noprint">
                            <div id="td_hideable" class="navigation"
                                <c:choose>
                                    <c:when test="${navigationVisible == null || navigationVisible}">style="display:block;" </c:when>
                                    <c:otherwise>style="display:none;"</c:otherwise>
                                </c:choose>
                            >
                                <c:import url="${nav}"/>
                            </div>
                        </td>
                        <c:set var="navflag" value="nav"/>
                    </c:if>
                    <td class="content" id="<c:out value="${navflag}"/>">
                        <c:if test="${sessionScope.userSession != null}"><input id="defaultNavOUId" type="hidden" name="default.navigator.ou.id" value="<c:out value="${sessionScope.userSession.organisationUnitId}"/>"/></c:if>
                        <c:if test="${content != null}"><c:import url="${content}"/></c:if>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <c:if test="${decoration != null}">
        <%@ include file="/help/bottomimage.html" %>
    </c:if>

    <c:if test="${footer != null}">
        <c:import url="${footer}"/>
    </c:if>

</table>

<c:if test="${not nopoll}">
    <zynap:popup id="sessionExpWarning" closeFunction="popupHideWarning('sessionExpWarning', 'sessionExpWarningIframe')">
        <%@include file="sessionwarning.jsp" %>
    </zynap:popup>
</c:if>

</body>
</html>
