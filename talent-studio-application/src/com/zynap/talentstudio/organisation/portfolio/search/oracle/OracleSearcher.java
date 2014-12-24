/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search.oracle;

import oracle.search.query.webservice.client.Attribute;
import oracle.search.query.webservice.client.DataGroup;
import oracle.search.query.webservice.client.Filter;
import oracle.search.query.webservice.client.OracleSearchResult;
import oracle.search.query.webservice.client.OracleSearchService;

import com.zynap.domain.QueryParameter;
import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import com.zynap.talentstudio.organisation.portfolio.search.AbstractConnector;
import com.zynap.talentstudio.organisation.portfolio.search.ConnectionDownException;
import com.zynap.talentstudio.organisation.portfolio.search.ExternalSearchException;
import com.zynap.talentstudio.organisation.portfolio.search.IField;
import com.zynap.talentstudio.organisation.portfolio.search.IResultMapper;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchQuery;
import com.zynap.talentstudio.organisation.portfolio.search.ISearcher;

import org.apache.soap.SOAPException;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Sep-2007 10:58:26
 */
public class OracleSearcher extends AbstractConnector implements ISearcher {

    /**
     * Executes a Text query on the oracle secure search.
     *
     * @param query  the query object wrapping all optional and required query fields
     * @param fields the fields that the results must contain in order to be returned
     */
    public IResultMapper executeTextQuery(ISearchQuery query, IField[] fields) throws ExternalSearchException {
        OracleResultMapper result = new OracleResultMapper();

        //used during query time filtering of the results
        try {
            OracleSearchService oracleSearchService = new OracleSearchService();
            oracleSearchService.setSoapURL("http://" + getHost() + ":" + getPort() + "/search/query/OracleSearch");
            DataGroup[] dataGroups = oracleSearchService.getDataGroups("en");
            Attribute[] allAttributes = oracleSearchService.getAllAttributes("en");

            Integer[] fetchAttributes = new Integer[allAttributes.length];
            Attribute filterAttribute = null;
            for (int i = 0; i < allAttributes.length; i++) {
                Attribute currentAttribute = allAttributes[i];
                fetchAttributes[i] = currentAttribute.getId();
                if (OracleResultMapper.CONTENT_TYPE_ID.equalsIgnoreCase(currentAttribute.getName())) {
                    filterAttribute = currentAttribute;
                }
            }

            Filter[] fieldFilters = applyFilters(fields, filterAttribute);

            String[] sources = StringUtils.delimitedListToStringArray(query.getSources(), "+");
            DataGroup[] groupSources = getDataSources(sources, dataGroups);

            Map<String, QueryParameter> parameters = query.getParameters();
            QueryParameter maxResults = parameters.get(DocumentSearchQuery.MAXIMUM_RESULTS);
            Integer numResults = (Integer) maxResults.getValue();

            OracleSearchResult oracleSearchResult = oracleSearchService.doOracleSearch(
                    query.getQueryText(), new Integer(1), numResults, Boolean.TRUE, Boolean.FALSE,
                    groupSources, "en", null, Boolean.TRUE, FILTER_CONNECTOR, fieldFilters, fetchAttributes
            );

            result.setResults(oracleSearchResult);

        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SOAPException) {
                Throwable throwable = ((SOAPException) e).getTargetException();
                if (throwable instanceof IOException) {
                    String errorMessage = "Unable to connect to " + getHost() + ":" + getPort();
                    throw new ConnectionDownException(errorMessage, "autonomy.connection.down");
                } else if ("NullPointerException".indexOf(e.getMessage()) != -1) {
                    String errorMessage = "Invalid parameter sent to search engine, please check configuration";
                    throw new ConnectionDownException(errorMessage, "unknown.autonomy.error");
                }
            }
            throw new ExternalSearchException(e.getMessage(), "unknown.autonomy.error");
        }
        return result;
    }

    /**
     * Builds an array of the groups which need to be searched
     *
     * @param sources
     * @param dataGroups
     * @return groups that match the given query sources
     */
    private DataGroup[] getDataSources(String[] sources, DataGroup[] dataGroups) {
        DataGroup[] groupSources = new DataGroup[sources.length];
        int index = 0;
        for (int i = 0; i < sources.length; i++) {
            String source = sources[i];
            for (int j = 0; j < dataGroups.length; j++) {
                DataGroup dataGroup = dataGroups[j];
                if (dataGroup.getGroupName().equals(source)) {
                    groupSources[index] = dataGroup;
                    index++;
                }
            }
        }
        return groupSources;
    }

    private Filter[] applyFilters(IField[] fields, Attribute filterAttribute) {
        Filter[] fieldFilters = null;
        if (filterAttribute != null) {
            fieldFilters = new Filter[fields.length];
            for (int i = 0; i < fields.length; i++) {
                IField field = fields[i];
                // for each field we are going to create a fieldFilter
                fieldFilters[i] = new Filter(filterAttribute.getId(), filterAttribute.getType(), EQUALS_OPERATOR, field.getValue().toString());
            }
        }
        return fieldFilters;
    }

    /**
     * Executes a Suggest query on the oracle secure search.
     * <br/>This query requires a list of example document ids in the ISearchQuery.
     *
     * @param query  the object wrapping requests
     * @param fields used to build a string todo!
     */
    public IResultMapper executeSuggestQuery(ISearchQuery query, IField[] fields) throws ExternalSearchException {
        return null;
    }

    /**
     * Executes a query given a document id.
     *
     * @param documentId  the document id
     * @param datasources
     * @param fields      the security fields to consider
     */
    public IResultMapper executeQuery(int documentId, String[] datasources, IField[] fields) throws ExternalSearchException {
        return null;
    }

    /**
     * Check version.
     * <br/> Return true if expected version matches actual version of oracle secure search.
     *
     * @return true or false
     * @throws com.zynap.talentstudio.organisation.portfolio.search.ExternalSearchException
     *
     */
    public boolean checkVersion() throws ExternalSearchException {
        return true;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private String version;
    private static final String FILTER_CONNECTOR = "or";
    private static final String EQUALS_OPERATOR = "equals";
}
