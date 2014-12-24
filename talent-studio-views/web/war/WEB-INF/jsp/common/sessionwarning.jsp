<!-- needs to display a message with a timer count down somehow which when timer = 0 the javascript calls logout we also need a hide/OK button -->

<table class="infotable" cellpadding="0" cellspacing="0">
    <tr>
        <td class="infomessage">
            <fmt:message key="timeout.warning"/>
        </td>
    </tr>
    <tr>
        <td class="infomessage">
            <fmt message key="seconds.to.expiry"/>
            <span class="error" id="cntdwn">.</span>
        </td>
    </tr>
    <tr>
        <td class="actionButton">
            <input type="button" name="hdeBtn" value="<fmt:message key="dismiss"/>" onclick="popupHideWarning('sessionExpWarning', 'sessionExpWarningIframe');"/>
        </td>
    </tr>
</table>

