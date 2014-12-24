/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

import java.io.Serializable;
import java.util.List;


/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * ! NOTE: any changes to this class must also be copied to AdminWebapp in Kneobase !
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface ISearchResult extends Serializable {

    /**
     * ISearchResult is composed of single search hits
     *
     * @param singleHit - a single search hit
     */
    void addSingleResult(ISearchResult singleHit);

    List getHits();

    String getScope();

    boolean createdBy(Long userId);

    Long getArtefactId();

    int getNumHits();

}
