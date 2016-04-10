/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.SecurityHelper;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermit;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Dao that queries for portfolio information such as content types for the portfolio.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HibernatePortfolioDao extends HibernateCrudAdaptor implements IPortfolioDao {

    public Class getDomainObjectClass() {
        return PortfolioItem.class;
    }

    public Collection findAllByNodeType(String nodeType, String[] contentTypes) {

        final StringBuffer query = new StringBuffer("from PortfolioItem item");

        final boolean hasNodeType = StringUtils.hasText(nodeType);
        if (hasNodeType) {
            query.append(" where item.node.nodeType = '").append(nodeType).append("'");
        }

        if (contentTypes != null && contentTypes.length > 0) {
            if (hasNodeType) {
                query.append(" and");
            } else {
                query.append(" where");
            }

            query.append(" item.contentType.id in (");

            final int numContentTypes = contentTypes.length;
            for (int i = 0; i < numContentTypes; i++) {
                String contentType = contentTypes[i];
                query.append("'").append(contentType).append("'");

                if (i != numContentTypes - 1) {
                    query.append(",");
                }
            }

            query.append(")");
        }

        query.append(" order by item.id");

        return getHibernateTemplate().find(query.toString());
    }

    public Collection getContentTypes(String nodeType) {
        String query = "from ContentType contentType where contentType.type=:type order by upper(contentType.label)";
        return getHibernateTemplate().findByNamedParam(query, "type", nodeType);
    }

    public Collection getContentTypes(String nodeType, Collection contentTypePermits) {
        if (contentTypePermits.isEmpty()) return new ArrayList();
        StringBuffer contentTypes = buildPermitList(contentTypePermits);
        String query = "from ContentType type where type.type=:type and type.id IN (" + contentTypes.toString() + ") order by upper(type.label)";
        return getHibernateTemplate().findByNamedParam(query, "type", nodeType);
    }

    public Collection getAllContentTypes(String action, Collection contentTypePermits) {
        if (contentTypePermits.isEmpty()) return new ArrayList();
        StringBuffer contentTypePermitString = buildPermitList(contentTypePermits);
        String query = "from ContentType type where type.id IN (" + contentTypePermitString.toString() + ") order by upper(type.label)";
        return getHibernateTemplate().find(query);
    }

    public Collection getAllContentTypes() {
        String query = "from ContentType type order by upper(type.label)";
        return getHibernateTemplate().find(query);
    }

    public ContentType getContentType(String contentTypeId) {
        return (ContentType) getHibernateTemplate().load(ContentType.class, contentTypeId);
    }

    public IDomainObject findAndCheckArtefactAccess(Long portfolioItemId, Long viewSubjectPermitId, Long viewPositionPermitId) throws TalentStudioException {
        final PortfolioItem portfolioItem = (PortfolioItem) super.findById(portfolioItemId);

        Long principalId = UserSessionFactory.getUserSession().getId();
        final Node node = portfolioItem.getNode();
        if (node instanceof Position) {
            SecurityHelper.checkNodeAccess(node, principalId, viewPositionPermitId, getHibernateTemplate());
        } else if (node instanceof Subject) {
            SecurityHelper.checkNodeAccess(node, principalId, viewSubjectPermitId, getHibernateTemplate());
        } else {
            throw new IllegalArgumentException("Portfolio item " + portfolioItemId + " belongs to invalid node - type is " + node.getNodeType());
        }


        return portfolioItem;
    }

    public PortfolioItemFile findItemFile(Long portfolioItemId) {
        return (PortfolioItemFile) getHibernateTemplate().load(PortfolioItemFile.class, portfolioItemId);
    }

    public void createFile(PortfolioItemFile file) {
        getHibernateTemplate().save(file);
    }

    public void updateFile(PortfolioItemFile file) {
        getHibernateTemplate().update(file);
    }

    public void deleteFile(PortfolioItemFile itemFile) {
        getHibernateTemplate().delete(itemFile);
    }

    /**
     * Builds a comma separated string of content type permits.
     *
     * @param contentTypePermits
     * @return string as a comma separated values each surrounded with ' '
     */
    private StringBuffer buildPermitList(Collection contentTypePermits) {
        StringBuffer contentTypes = new StringBuffer();
        for (Iterator iterator = contentTypePermits.iterator(); iterator.hasNext();) {
            IPermit permit = (IPermit) iterator.next();
            contentTypes.append("'").append(permit.getContent()).append("'");
            if (iterator.hasNext()) {
                contentTypes.append(",");
            }
        }
        return contentTypes;
    }
}
