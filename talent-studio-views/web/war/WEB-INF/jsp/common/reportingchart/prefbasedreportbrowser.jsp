<%@ include file="../../includes/include.jsp" %>

<zynap:evalBack>
    <zynap:actionbox>
        <zynap:actionEntry>
            <fmt:message key="back" var="buttonLabel" />
            <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
        </zynap:actionEntry>
    </zynap:actionbox>
</zynap:evalBack>

<%@include file="common.jsp" %>

<c:url value="/image/viewsubjectimage.htm" var="picUrl" scope="request"/>

<c:if test="${hasPosition}">
    <zynap:infobox title="${title}">

        <zynap:prefBasedReportingChartTag subjectUrl="${subUrl}" orgUrl="${ouUrl}"
                                          positionUrl="${posUrl}" viewUrl="${url}"
                                          preferences="${command.preferenceCollection}" target="${command.position}" imageUrl="${picUrl}" secure="${command.secure}"/>

    </zynap:infobox>
</c:if>

<script type="text/javascript">

    $(function() {
        $( "#myDialog" ).dialog({ autoOpen: false, modal: false });
    });

    function close_dialog() {
        $('#myDialog').dialog('close');
        //$('body').off('keydown', close_dialog);
        return true;
    }

    /*
     * positionig attributes:
     *
     */
    //function showHover(elem) {
    $('.OrgChartAttrLabel').hover(function () {
        //$('body').on('keydown', close_dialog);
        var titleAttr = $(this).attr('title');
        if(isBlank(titleAttr)) return false;

        $('#myDialog').html(titleAttr);
        $('#myDialog').dialog('open');
        var myDialogX = $(this).position().left - $(this).outerWidth();
        var myDialogY = $(this).position().top - ( $(document).scrollTop() + $('.ui-dialog').outerHeight() );
        $('#myDialog').dialog( 'option', 'position', [myDialogX, myDialogY]);
        $('#myDialog').dialog( 'option', 'title', $(this).html());
        //}, function () {
        //$('#myDialog').dialog('close');
        //$('body').off('keydown', close_dialog);

    });
    //}


</script>

<div id="myDialog" style="display:none">No Information available</div>