package com.zynap.talentstudio.organisation.portfolio.search;

/**
 * Facade for document searches using 3rd party software such as Autonomy.
 *
 * @author bcassidy
 */
public interface ISearchManager {

    /**
     * Execute a query to return documents based on the specified query and fields.
     *
     * <br> Filters the documents returned according to scope.
     * <ul>
     * <li>public documents are always included in the search results returned</li>
     * <li>protected documents are included in the search results if the current user has access to the artefact the item belongs to</li>
     * <li>private documents are included in the search results returned if the current user created the document</li>
     * </ul>
     *
     * @param query The object that contains the criteria to use in the search (eg: the query text)
     * @param fields The field parameters (content types to search for)
     * @return ISearchResult   - {@link ISearchResult} object contining the results
     */
    ISearchResult search(ISearchQuery query, IField[] fields) throws ExternalSearchException;

    /**
     * Execute a query to return documents considered similar to the one referenced by the doc id parameter.
     *
     * <br> Filters the documents returned according to scope.
     * <ul>
     * <li>public documents are always included in the search results returned</li>
     * <li>protected documents are included in the search results if the current user has access to the artefact the item belongs to</li>
     * <li>private documents are included in the search results returned if the current user created the document</li>
     * </ul>
     *
     * @param docId The document id
     * @param fields The field parameters (content types to search for)
     * @param datasources The data sources (delimited string of datasources separated by "+" - can be only one of them)
     * @return ISearchResult   - {@link ISearchResult} object contining the results
     */
    ISearchResult search(int docId, IField[] fields, String datasources) throws ExternalSearchException;

    /**
     * Check version.
     * <br/> Return true if expected version matches actual version of 3rd party software.
     *
     * @return true or false
     * @throws ExternalSearchException
     */
    boolean checkVersion() throws ExternalSearchException;    
}
