package com.zynap.talentstudio.integration.dto.processors;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.SecurityAttribute;
import com.zynap.talentstudio.util.AuditUtils;

import java.util.Iterator;

/**
 * User: jsueiras
 * Date: 31-Oct-2005
 * Time: 11:33:02
 */
public class NodeProccesor {

    public void process(IDomainObject domainObject, User user) {
        Node node = (Node) domainObject;
        if (node.getPortfolioItems() != null) {
            for (Iterator iterator = node.getPortfolioItems().iterator(); iterator.hasNext();) {
                PortfolioItem portfolio = (PortfolioItem) iterator.next();
                portfolio.setCreatedById(UserSessionFactory.getUserSession().getId());
                portfolio.setLastModifiedById(UserSessionFactory.getUserSession().getId());
                portfolio.setNode(node);
                portfolio.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
            }
        }

        AuditUtils.setNodeAudit(node, user);
    }
}
