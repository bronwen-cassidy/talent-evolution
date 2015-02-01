<%@ include file="../../includes/include.jsp" %>

<zynap:evalBack>
    <zynap:actionbox>
        <zynap:actionEntry>
            <fmt:message key="back" var="buttonLabel"/>
            <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
        </zynap:actionEntry>
    </zynap:actionbox>
</zynap:evalBack>

<zynap:infobox title="${title}">
    <div class="infomessage">
        <fmt:message key="error.invalidsubmit"/>        
    </div>
</zynap:infobox>
