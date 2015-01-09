/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

import com.zynap.talentstudio.organisation.portfolio.search.ExternalSearchException;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchResult;
import com.zynap.talentstudio.organisation.portfolio.search.IResultMapper;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IMapper {

    /**
     * Map results.
     * @param resultMapper
     * @return ISearchResult
     * @throws ExternalSearchException
     */
    ISearchResult map(IResultMapper resultMapper) throws ExternalSearchException;
}
