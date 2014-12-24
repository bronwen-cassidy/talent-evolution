<%@ include file="../includes/include.jsp"%>

<fmt:message key="artefact.view.settings" var="msg"/>
<zynap:infobox id="dcl_items" title="${msg}">

	<table class="infotable" id="dcl_tblitems">
		<c:forEach var="display" items="${command.displayConfigs}" varStatus="index" >
			<tr>
				<td class="pager">
					<form method="post" action="" name="edit<c:out value="${index.count}"/>">
						<a id="dis<c:out value="${index.count}"/>" href="#" onclick="javascript:document.forms.edit<c:out value="${index.count}"/>.submit();"><c:out value="${display.label}"/></a>
						<input type="hidden" id="hidd<c:out value="${index.count}"/>" name="dc" value="<c:out value="${display.id}"/>"/>
						<input type="hidden" id="tard<c:out value="${index.count}"/>" name="_target1" value="1"/>
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>

</zynap:infobox>
