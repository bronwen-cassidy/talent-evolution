package com.zynap.talentstudio.util;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.NodeAudit;

import java.util.Date;

/**
 * User: amark
 * Date: 05-Apr-2006
 * Time: 14:51:45
 */
public final class AuditUtils {

    /**
     * @param node
     * @param user
     */
    public static void setNodeAudit(Node node, User user) {

        final Date date = new Date();

        if (node.getId() == null) {
            // if new node set node audit
            node.setNodeAudit(new NodeAudit(date, node, user.getId()));
        } else {
            // otherwise get node audit if it is null add a new one, otherwise update the existing one
            final NodeAudit mostRecentNodeAudit = node.getNodeAudit();
            if (mostRecentNodeAudit != null) {
                mostRecentNodeAudit.setUpdatedById(user.getId());
                mostRecentNodeAudit.setUpdated(date);
            } else {
                node.setNodeAudit(new NodeAudit(date, node, user.getId()));
            }
        }
    }
}
