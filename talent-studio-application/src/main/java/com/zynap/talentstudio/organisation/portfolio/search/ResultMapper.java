/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ResultMapper implements IResultMapper {

    public ResultMapper(String xmlResults) {
        this.xmlResults = xmlResults;
    }

    public void setResults(Object result) {
        this.xmlResults = (String) result;
    }

    public Object getResults() {
        return xmlResults;
    }       

    public String getXMLResults() {
        return xmlResults;
    }

    public ISearchResult mapResults() {
        return new SearchResult();
    }

    private String xmlResults;
}
