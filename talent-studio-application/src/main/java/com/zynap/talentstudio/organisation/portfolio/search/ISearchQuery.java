/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

import com.zynap.domain.QueryParameter;

import java.io.Serializable;
import java.util.Map;

/**
 * Class that represents query used by {@link com.zynap.talentstudio.organisation.portfolio.search.ISearchManager}.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface ISearchQuery extends Serializable {

    String getQueryText();

    void setQueryText(String queryText);

    Map<String, QueryParameter> getParameters();

    void setSources(String sources);

    String getSources();

    String getExampleDocumentIds();
}
