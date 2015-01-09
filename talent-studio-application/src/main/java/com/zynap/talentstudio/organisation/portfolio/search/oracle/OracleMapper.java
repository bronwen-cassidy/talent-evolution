/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search.oracle;

import com.zynap.talentstudio.organisation.portfolio.search.ExternalSearchException;
import com.zynap.talentstudio.organisation.portfolio.search.IMapper;
import com.zynap.talentstudio.organisation.portfolio.search.IResultMapper;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchResult;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-Aug-2010 16:45:11
 */
public class OracleMapper implements IMapper {

    public ISearchResult map(IResultMapper resultMapper) throws ExternalSearchException {
        return resultMapper.mapResults();
    }
}
