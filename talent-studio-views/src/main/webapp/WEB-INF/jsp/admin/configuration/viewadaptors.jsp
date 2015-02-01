<%@ include file="../../includes/include.jsp" %>
<script type="text/javascript" charset="utf-8">
    function syncSubjects() {
        $('#syncresults').html('<div class=\"infomessage\"><img src=\"/../images/loading.gif\"/></div>');
        $.get("viewsyncresults.htm?ts=" + new Date().getTime(), {id: 12}, function(data) {
            $('#syncresults').html(data);
        });
    }

    function importSubjects() {
        var popId = $('#impSelect').find("option:selected").val();
        if(popId == '---') {
            $('#impError').show();
            return;
        } else $('#impError').hide();

        $('#importresults').html('<div class=\"infomessage\"><img src=\"../images/loading.gif\"/></div>');
        $.get("importsubjects.htm?ts=" + new Date().getTime(), {populationId: popId}, function(data) {
                $('#importresults').html(data);
            });
        }

    function exportSubjects() {
        var popId = $('#expSelect').find("option:selected").val();
        if(popId == '---') {
            $('#expError').show();
            return;
        } else $('#expError').hide();

        $('#exportresults').html('<div class=\"infomessage\"><img src=\"../images/loading.gif\"/></div>');
        $.get("exportsubjects.htm?ts=" + new Date().getTime(), {populationId: popId}, function(data) {
            $('#exportresults').html(data);
        });
    }

</script>

<zynap:infobox title="${title}">
    <table class="infotable" cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th><fmt:message key="function"/></th>
                <th><fmt:message key="analysis.population.view"/></th>
                <th><fmt:message key="action"/></th>
                <th><fmt:message key="results"/></th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td class="infodata"><fmt:message key="taapi.adaptor.info"/></td>
                <td class="infodata">&nbsp;</td>
                <td class="infodata"><input id="syncbtnid" type="button" name="syncme" value="<fmt:message key="adaptor.sync"/>" onclick="syncSubjects();"/></td>
                <td class="infodata"><div id="syncresults"></div></td>
            </tr>
            <tr>
                <td class="infodata"><label for="impSelect"><fmt:message key="adaptor.import"/></label></td>
                <td class="infodata">
                    <select id="impSelect" name="model.importPopulation">
                        <option value="---"><fmt:message key="please.select"/></option>
                        <c:forEach var="pop" items="${model.populations}">
                            <option value="<c:out value="${pop.id}"/>"><c:out value="${pop.label}"/></option>
                        </c:forEach>
                    </select>
                    <div class="error" style="display:none" id="impError"><fmt:message key="population.required"/></div>
                </td>
                <td class="infodata"><input id="impbtnid" type="button" name="syncme" value="<fmt:message key="import"/>" onclick="importSubjects();"/></td>
                <td class="infodata"><div id="importresults"></div></td>
            </tr>
            <tr>
                <td class="infodata"><label for="expSelect"><fmt:message key="adaptor.export"/></label></td>
                <td class="infodata">
                    <select id="expSelect" name="model.exportPopulation">
                        <option value="---"><fmt:message key="please.select"/></option>
                        <c:forEach var="pop" items="${model.populations}">
                            <option value="<c:out value="${pop.id}"/>"><c:out value="${pop.label}"/></option>
                        </c:forEach>
                    </select>
                    <div class="error" style="display:none" id="expError"><fmt:message key="population.required"/></div>
                </td>
                <td class="infodata"><input id="expbtnid" type="button" name="syncme" value="<fmt:message key="export"/>" onclick="exportSubjects();"/></td>
                <td class="infodata"><div id="exportresults"></div></td>
            </tr>
        </tbody>
    </table>
</zynap:infobox>