/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search.oracle;

import oracle.search.query.webservice.client.CustomAttribute;
import oracle.search.query.webservice.client.OracleSearchResult;
import oracle.search.query.webservice.client.ResultElement;

import com.zynap.talentstudio.organisation.portfolio.search.IResultMapper;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchResult;
import com.zynap.talentstudio.organisation.portfolio.search.SearchResult;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 04-Oct-2007 11:09:49
 */
public class OracleResultMapper implements IResultMapper {

    public void setResults(Object result) {
        this.oracleSearchResult = (OracleSearchResult) result;
    }

    public Object getResults() {
        return oracleSearchResult;
    }

    public ISearchResult mapResults() {
        SearchResult searchResult = new SearchResult();
        if(oracleSearchResult == null) return searchResult;
        
        ResultElement[] resultElements = oracleSearchResult.getResultElements();        
        for (ResultElement resultElement : resultElements) {
            SearchResult hit = new SearchResult();

            hit.setArtefactTitle(resultElement.getAuthor());
            hit.setResultWeight(resultElement.getScore().toString());
            hit.setSummary(resultElement.getSnippet());
            Integer docId = resultElement.getDocID();
            if (docId != null) {
                hit.setDocumentId(docId.toString());
            }
            hit.setContentTitle(resultElement.getTitle());
            
            CustomAttribute[] attributes = resultElement.getCustomAttributes();
            for (CustomAttribute customAttribute : attributes) {
                String name = customAttribute.getName();
                String value = customAttribute.getValue();
                if(OWNER_ID.equalsIgnoreCase(name)) hit.setCreatedBy(value);
                else if(ARTETFACT_ID.equalsIgnoreCase(name)) hit.setArtefactId(value);
                else if(ITEM_ID.equalsIgnoreCase(name)) hit.setItemId(Long.valueOf(value));
                else if(ITEM_SCOPE.equalsIgnoreCase(name)) hit.setScope(value);
                else if(ARTEFACT_TYPE.equalsIgnoreCase(name)) hit.setArtefactType(value);
                else if(CONTENT_TYPE_ID.equalsIgnoreCase(name)) hit.setContentTypeId(value);
                else if(ITEM_FILESIZE.equalsIgnoreCase(name)) hit.setFileSize(value);
            }
            searchResult.addSingleResult(hit);
        }
        return searchResult;
    }

    private OracleSearchResult oracleSearchResult;

    private static final String OWNER_ID = "owner_id_NUMBER";
    private static final String ARTETFACT_ID = "artefact_id_NUMBER";
    public static final String CONTENT_TYPE_ID = "content_type_id_STRING";
    private static final String ITEM_ID = "ID_NUMBER";
    private static final String ITEM_SCOPE = "scope_STRING";
    private static final String ITEM_FILESIZE = "file_size_NUMBER";
    private static final String ARTEFACT_TYPE = "artefact_type_STRING";
}
