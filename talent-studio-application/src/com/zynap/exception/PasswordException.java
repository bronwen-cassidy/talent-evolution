package com.zynap.exception;

import com.zynap.domain.admin.LoginInfo;

/**
 * Exception that indicates that the user must change their password.
 *
 * User: amark
 * Date: 24-Mar-2005
 * Time: 12:50:26
 */
public abstract class PasswordException extends TalentStudioException {

    public PasswordException(String message, LoginInfo currentInfo) {
        this(message, (Long) currentInfo.getUser().getId(), currentInfo.getUsername());
    }

    public PasswordException(String message, Long userId, String username) {
        super(message);
        this.userId = userId;
        this.username = username;
    }

    public PasswordException(String message, String userInfo, Throwable cause) {
        super(message, cause);

        // split user name and password as returned by sproc
        char sep = (char) 31;
        int delimiter = userInfo.indexOf(sep);
        if (delimiter > 0) {
            userId = Long.valueOf(userInfo.substring(0, delimiter));
            username = userInfo.substring(delimiter + 1);
        }
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    private Long userId;
    private String username;
}
