package com.zynap.talentstudio.web.security;

import com.zynap.domain.admin.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NotPredicate;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * User: amark
 * Date: 29-Apr-2005
 * Time: 11:32:47
 */
public final class UserHelper {

    private UserHelper() {
    }

    /**
     * Remove root users from list of users.
     *
     * @param users The list of users
     * @return The list of non-root users
     */
    public static Collection<User> removeRootUsers(Collection users) {
        //noinspection unchecked
        return CollectionUtils.select(users, new AppUserPredicate());
    }

    /**
     * Add any root users in the current Collection of users to the new Collection of users.
     *
     * @param currentUsers the current Collection of users
     * @param newUsers The new Collection of users
     */
    public static Set<User> retainRootUsers(Collection<User> currentUsers, Collection<User> newUsers) {
        CollectionUtils.filter(currentUsers, new NotPredicate(new AppUserPredicate()));
        newUsers.addAll(currentUsers);

        return new HashSet<User>(newUsers);
    }

    /**
     * Predicate that selects only non-root users.
     */
    private static class AppUserPredicate implements Predicate {

        public boolean evaluate(Object object) {
            User user = (User) object;
            return !user.isRoot();
        }
    }
}
