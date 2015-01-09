/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

/**
 * Interface for autonomy query component.
 *
 * @author bcassidy
 */
public interface ISearcher {

    /**
     * Executes a Text query on the autonomy DRE.
     *
     * @param query the query object wrapping all optional and required query fields
     * @param fields the fields that the results must contain in order to be returned
     */
    IResultMapper executeTextQuery(ISearchQuery query, IField[] fields) throws ExternalSearchException;

    /**
     * Executes a Suggest query on the autonomy DRE..
     * <br/>This query requires a list of example document ids in the ISearchQuery.
     *
     * @param query the object wrapping requests
     * @param fields used to build a string i.e.fieldText=1:artefactId,2:artefactId,3:artefactId,4:artefactId,17:artefactId
     */
    IResultMapper executeSuggestQuery(ISearchQuery query, IField[] fields) throws ExternalSearchException;

    /**
     * Executes a query given a document id.
     *
     * @param documentId the document id
     * @param datasources
     * @param fields the security fields to consider
     */
    IResultMapper executeQuery(int documentId, String[] datasources, IField[] fields) throws ExternalSearchException;

    /**
     * Check version.
     * <br/> Return true if expected version matches actual version of Autonomy.
     *
     * @return true or false
     * @throws ExternalSearchException
     */
    boolean checkVersion() throws ExternalSearchException;
}
