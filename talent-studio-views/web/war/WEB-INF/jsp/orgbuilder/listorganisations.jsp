<%@ include file="../includes/include.jsp" %>

<script type="text/javascript">

    // check to see if at least 1 checkbox has been ticked
    function checkMerge(formName)
    {
        var chkTotal = 0;
        var elem1Value;

        try
        {
            var form = document.forms[formName];
            var elements = form.elements;

            for (counter = 0; counter < elements.length ; counter++)
            {
                var element = elements[counter];
                if (element.type == 'checkbox' && element.checked)
                {
                    if(elem1Value == null) {
                        elem1Value = element.value;
                        var elem1 = getElemById("id0");
                        elem1.value = elem1Value;
                    }
                    else {
                        var elem2 = getElemById("id1");
                        elem2.value = element.value;
                    }
                    chkTotal++;
                }
            }
        }
        catch (e)
        {
            alert("Error in checkMerge(): " + e.message);
        }        
        return (chkTotal > 0);
    }

</script>

<zynap:saveUrl/>

<fmt:message key="list.organisations" var="msg"/>
<zynap:infobox title="${msg}">

    <c:url var="url" value="listorganisations.htm"/>
    <zynap:historyLink var="viewUrl" url="vieworganisation.htm"/>

    <zynap:form name="_merge" method="post" action="/orgbuilder/mergeorganisation.htm" onSubmit="return checkMerge('_merge')">
        <input type="hidden" name="_mergedList" value="_merge"/>
        <zynap:tree url="${url}" composite="${model.organisation}" viewLink="${viewUrl}" viewParamName="<%= ParameterConstants.ORG_UNIT_ID_PARAM %>"/>
        <br/><input class="inlinebutton" type="submit" name="_merge" value="<fmt:message key="merge"/>" <c:if test="${empty model.organisation.children}">disabled</c:if>/>
        <input type="hidden" id="id0" name="c_id" value=""/>
        <input type="hidden" id="id1" name="c_id" value=""/>
    </zynap:form>
</zynap:infobox>
