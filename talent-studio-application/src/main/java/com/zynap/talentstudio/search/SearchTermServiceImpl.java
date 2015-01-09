package com.zynap.talentstudio.search;

import java.util.List;

/**
 *
 */
public class SearchTermServiceImpl implements SearchTermService {

    public List<SearchTermResult> search(Long userId, String searchTerm) {
        return searchTermDao.search(userId, searchTerm, null);
    }

    public List<DataTerm> search(String searchTerm) {
        return searchTermDao.search(searchTerm);
    }

    public void setSearchTermDao(SearchTermDao searchTermDao) {
        this.searchTermDao = searchTermDao;
    }

    private SearchTermDao searchTermDao;
}
