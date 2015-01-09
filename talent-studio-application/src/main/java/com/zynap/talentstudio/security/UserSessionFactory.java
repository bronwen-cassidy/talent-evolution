package com.zynap.talentstudio.security;

import com.zynap.domain.UserSession;

/**
 * Thread-local access to a users' session (renders a stateful Singleton).
 *
 * <p>
 * We can allow any class in our program to easily acquire a reference to a per-thread UserSession.
 * In this way, we can think of a ThreadLocal as allowing us to create a per-thread-singleton.</p>
 *
 * This class must be initialized by someone else (web component)
 * 
 * @author Andreas Andersson
 * @since 20 february 2004
 */

public final class UserSessionFactory {

    private static final ThreadLocal<UserSession> userSession = new ThreadLocal<UserSession>();

    /**
     * Get the UserSession from the ThreadLocal.
     * <br> Will throw an <code>IllegalStateException</code> if the UserSession is not set on the ThreadLocal.
     *
     * @return UserSession
     */
    public static UserSession getUserSession() {
        UserSession userSession = UserSessionFactory.userSession.get();
        if (userSession == null) {
            throw new IllegalStateException("UserSession not initialized.");
        }

        return userSession;
    }

    /**
     * Set UserSession ThreadLocal.
     * @param newSession The UserSession
     */
    public static void setUserSession(UserSession newSession) {
        userSession.set(newSession);
    }
}

