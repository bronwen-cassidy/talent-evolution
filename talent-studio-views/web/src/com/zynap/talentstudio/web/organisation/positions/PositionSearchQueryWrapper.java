package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.talentstudio.web.organisation.NodeSearchQueryWrapper;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 11-Feb-2005
 * Time: 10:59:35
 * To change this template use File | Settings | File Templates.
 */
public class PositionSearchQueryWrapper extends NodeSearchQueryWrapper {

    public PositionSearchQueryWrapper(Long organisationId) {
        super(organisationId, new PositionSearchQuery());
    }

    public PositionSearchQueryWrapper(PositionSearchQuery searchAdaptor) {
        super(searchAdaptor);
    }

    public String getTitle() {
        return getPositionSearchQuery().getTitle();
    }

    public void setTitle(String queryText) {
        getPositionSearchQuery().setTitle(queryText);
    }



    public PositionSearchQuery getModifiedPositionSearchQuery() {
        return (PositionSearchQuery) getNodeSearchQuery();
    }

    private PositionSearchQuery getPositionSearchQuery() {
        return (PositionSearchQuery) this.searchAdaptor;
    }


    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }


    private String nodeType;
}

