<%@ include file="../../includes/include.jsp" %>
<html>
<head>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="expires" content="-1"/>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>

    <title><fmt:message key="popup"/></title>

    <%@ include file="../../includes/stylesheets.jsp" %>
    <script src="<c:url value="/js/talent.studio.min.js"/>" type="text/javascript"></script>
</head>

<body class="serverPopup">
<script type="text/javascript">


    function goToPreviousSelected() {
        var frm = parent.document.forms['_view'];
        frm.submit();
    }
</script>


<%@ include file="subjectpicker.jsp" %>
<%@ include file="positionpicker.jsp" %>

<zynap:serverTree id="${command.popupId}" trees="${command.tree}" branchIcon="ClosedFolder.gif" emptyBranchIcon="ClosedFolder.gif"
                  leafIcon="${command.leafIcon}" branchSelectable="true" serverMode="true" selectedBranchId="${command.branchId}"/>

</body>
</html>













