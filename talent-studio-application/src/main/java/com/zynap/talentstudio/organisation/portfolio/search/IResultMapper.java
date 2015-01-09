/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 04-Oct-2007 11:04:20
 */
public interface IResultMapper {

    void setResults(Object result);
    Object getResults();

    ISearchResult mapResults();
}
