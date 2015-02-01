package com.zynap.talentstudio.web.portfolio;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItemFile;

import java.io.Serializable;

/**
 * User: amark
 * Date: 19-May-2005
 * Time: 16:31:29
 */
public class PortfolioItemWrapperBean implements Serializable {

    public PortfolioItemWrapperBean(PortfolioItem portfolioItem, PortfolioItemFile itemFile) {
        this.portfolioItem = portfolioItem;
        this.itemFile = itemFile;
    }

    public Node getNode() {
        return getItem().getNode();
    }

    public String getLabel() {
        return getItem().getLabel();
    }

    public PortfolioItem getItem() {
        return portfolioItem;
    }

    public String getTextContent() {
        return itemFile != null ? new String(itemFile.getBlobValue()) : null;
    }

    public String getUrl() {
        return itemFile != null ? new String(itemFile.getBlobValue()) : null;   
    }

    private PortfolioItem portfolioItem;
    private PortfolioItemFile itemFile;
}