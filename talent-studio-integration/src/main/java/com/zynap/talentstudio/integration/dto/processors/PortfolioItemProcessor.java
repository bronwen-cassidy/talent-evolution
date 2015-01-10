package com.zynap.talentstudio.integration.dto.processors;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 26-Oct-2005
 * Time: 15:44:08
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioItemProcessor implements IPostProcessor {

    public void process(IDomainObject domainObject, User user) {
        PortfolioItem portfolioItem = (PortfolioItem) domainObject;
        portfolioItem.setCreatedById(user.getId());
        portfolioItem.setLastModifiedById(user.getId());
        portfolioItem.setLastModified(new Date());
        portfolioItem.setFileSize(portfolioItem.getFileSize());
    }
}
