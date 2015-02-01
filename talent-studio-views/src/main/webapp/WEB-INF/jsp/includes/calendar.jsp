<%
    // get the first day of the week
    int FirstDayOfWeek = 0; // 0 - sunday ; 1 - monday
%>

<script type="text/javascript">
    var weekStartAt = <%= FirstDayOfWeek %>; // 0 - sunday ; 1 - monday

    var VisibleDateFormat = "<fmt:message key='date.display.format'/>";

    var fullMonthName = new Array(
            "<fmt:message key='January'/>",
            "<fmt:message key='February'/>",
            "<fmt:message key='March'/>",
            "<fmt:message key='April'/>",
            "<fmt:message key='May_'/>",
            "<fmt:message key='June'/>",
            "<fmt:message key='July'/>",
            "<fmt:message key='August'/>",
            "<fmt:message key='September'/>",
            "<fmt:message key='October'/>",
            "<fmt:message key='November'/>",
            "<fmt:message key='December'/>");

    var shortMonthName = new Array(
            "<fmt:message key='Jan'/>",
            "<fmt:message key='Feb'/>",
            "<fmt:message key='Mar'/>",
            "<fmt:message key='Apr'/>",
            "<fmt:message key='May'/>",
            "<fmt:message key='Jun'/>",
            "<fmt:message key='Jul'/>",
            "<fmt:message key='Aug'/>",
            "<fmt:message key='Sept'/>",
            "<fmt:message key='Oct'/>",
            "<fmt:message key='Nov'/>",
            "<fmt:message key='Dec'/>");

    dayName = new Array (
        <% if (FirstDayOfWeek == 0) { %>
            "<fmt:message key='Sun'/>",
            "<fmt:message key='Mon'/>",
            "<fmt:message key='Tue'/>",
            "<fmt:message key='Wed'/>",
            "<fmt:message key='Thu'/>",
            "<fmt:message key='Fri'/>",
            "<fmt:message key='Sat'/>"
        <% } else { %>
            "<fmt:message key='Mon'/>",
            "<fmt:message key='Tue'/>",
            "<fmt:message key='Wed'/>",
            "<fmt:message key='Thu'/>",
            "<fmt:message key='Fri'/>",
            "<fmt:message key='Sat'/>",
            "<fmt:message key='Sun'/>"
    <% } %>
    );
</script>

<!-- Body of calendar popup -->

<table cellspacing="0" >
	<tr class="CalControls">
		<td class="CalControls">
			<span class="CalControl" 
					onmouseover="this.className='CalControlOver'"
					onmouseout="this.className='CalControl'"	 
					onclick="popUpYear()" >
				<span id="spanYear"></span>&nbsp;<img alt="select year" src="../images/calendar/down.gif" />
			</span>
			<span class="CalControl"
					onmouseover="this.className='CalControlOver'" 
					onmouseout="this.className='CalControl'" 
					onclick="popUpMonth()">
				<span id="spanMonth"></span>&nbsp;<img alt="select month" src="../images/calendar/down.gif" />
			</span>
		</td>
		<td class="CalControls" align="right">
			<span class="CalControl" 
					onmouseover="this.className='CalControlOver'"
					onclick="decMonth()"
					onmouseout="this.className='CalControl'" >
				<img alt="Previous month" src="../images/calendar/left.gif" />
			</span>
			<span class="CalControl"
					onmouseover="this.className='CalControlOver'"
					onmouseout="this.className='CalControl'"
					onclick="incMonth()" >
				<img alt="Next month" src="../images/calendar/right.gif" />
			</span>
		</td>
	</tr>
	<tr>
		<td class="CalBody" colspan="2">
			<span id="CalBody"></span>
		</td>
	</tr>
	<tr class="CalToday">
		<td class="CalToday" colspan="2">
			Today is <a id="CalToday" class="CalToday" href="javascript:gotoToday();" />
		</td>
	</tr>
</table>


<!-- Month dropdown "popup" -->

<div id="datePickerMonth" class="CalPopup"></div>

<!-- Year dropdown "popup" -->

<div id="datePickerYear" class="CalPopup">
	<table
			onmouseover="clearTimeout(CalTimeout)"
			onmouseout="clearTimeout(CalTimeout);CalTimeout=setTimeout('popDownYear()',100);event.cancelBubble=true"
			cellspacing="0" >

		<!-- back years button -->

		<tr>
			<td class="CalDropdownNormal" align="center" 
					onmouseover="this.className='CalDropdownSelect'"
					onmouseout="clearInterval(CalInterval);this.className='CalDropdownNormal'"
					onmousedown="clearInterval(CalInterval);CalInterval=setInterval('decYear()',30)" 
					onmouseup="clearInterval(CalInterval)" >
				<img alt="earlier" src="../images/calendar/up.gif" />
			</td>
		</tr>

		<!-- 7 years -->

		<% for (int y = 0; y < 7; y++) { %>
			<tr>
				<td class="CalDropdownNormal" id="y<%= y %>"
						onmouseover="this.className='CalDropdownSelect'"
						onmouseout="this.className='CalDropdownNormal'"
						onclick="selectYear(<%= y%>);event.cancelBubble=true" >
					200<%= y%>
				</td>
			</tr>
		<% } %>

		<!-- advance years button -->

		<tr>
			<td class="CalDropdownNormal" align="center"
					onmouseover="this.className='CalDropdownSelect'" 
					onmouseout="clearInterval(CalInterval);this.className='CalDropdownNormal'"
					onmousedown="clearInterval(CalInterval);CalInterval=setInterval('incYear()',30)"
					onmouseup="clearInterval(CalInterval)" >
				<img alt="later" src="../images/calendar/down.gif" />
			</td>
		</tr>
	</table>
</div>


