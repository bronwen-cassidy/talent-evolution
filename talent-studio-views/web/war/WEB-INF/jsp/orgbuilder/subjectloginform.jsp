<%@ include file="../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>

<zynap:infobox title="${msg}">
    <zynap:form name="addinfo" method="post">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>

        <table class="infotable" cellspacing="0">
            <%@ include file="../common/subjects/userform.jsp"%>
			<tr>
				<td class="infobutton"/>
				<td class="infobutton">
					<input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
					<input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('addinfo', 'pgTarget', '1', 'backId');"/>
					<input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
				</td>
			</tr>
		</table>
	</zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
	<input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
