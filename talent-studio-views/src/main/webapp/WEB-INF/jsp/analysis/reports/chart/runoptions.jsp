<%-- include for common run options for metrics --%>
	<%@ include file="../../reportpopulationselect.jsp"%>
	<tr>
		<td class="infobutton">&nbsp;</td>
		<td class="infobutton">
			<input type="hidden" name="_redisplay" value="_redisplay"/>
            <input id="actTbRs1" type="hidden" name="activeTab" value="results"/>
			<input class="inlinebutton" type="submit" name="_target0" value="<fmt:message key="run"/>" onclick = "tabLoading('runoptions_span', 'loading');"/>
		</td>
	</tr>
