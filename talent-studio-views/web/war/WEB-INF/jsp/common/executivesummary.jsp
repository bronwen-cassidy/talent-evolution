<zynap:artefactLink var="viewSubjectUrl" url="viewsubject.htm" tabName="activeTab" activeTab="${command.activeTab}" />

<zynap:artefactLink var="viewPositionUrl" url="viewposition.htm" tabName="activeTab" activeTab="${command.activeTab}"/>


<fieldset>
    <legend><c:out value="${displayConfigView.executiveSummaryReport.label}"/> <c:out value="${artefact.label}"/></legend>
    <table class="infotable" id="execsummary">

        <zynap:artefactViewTag cellCount="2" report="${displayConfigView.executiveSummaryReport}" node="${artefact.node}" checkNodeAccess="true" viewSubjectUrl="${viewSubjectUrl}" viewPositionUrl="${viewPositionUrl}" imageUrl="${imageUrl}" displayHelper="${displayHelper}" viewType="${viewType}" executiveSummary="true"/>

    </table>
</fieldset>

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
    $('.attrLabel').click(function () {
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