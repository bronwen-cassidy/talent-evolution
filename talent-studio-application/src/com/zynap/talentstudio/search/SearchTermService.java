package com.zynap.talentstudio.search;

import java.util.List;

/**
 *
 */
public interface SearchTermService {

    List<SearchTermResult> search(Long userId, String searchTerm);

    /**
     * List of all distinct terms in the system whether you are allowed to see them or not.
     * This method just provides a selection dictionary of all terms in the database, when a term is selected
     * then the search term search executes which has ties into security so there may be no results to that.
     *
     * @param searchTerm - the term we are liiking for
     * @return list of terms starting with
     */
    List<DataTerm> search(String searchTerm);
}
