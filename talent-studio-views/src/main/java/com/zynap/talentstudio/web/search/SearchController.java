package com.zynap.talentstudio.web.search;

import com.zynap.talentstudio.search.DataTerm;
import com.zynap.talentstudio.search.SearchTermService;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SearchController extends ZynapMultiActionController {

    public ModelAndView findAvailableKeyWords(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = new HashMap<String, Object>();
        String keyWord = request.getParameter("word");
        List<DataTerm> words = searchTermService.search(keyWord);
        model.put("terms", words);
        return new ModelAndView("listwords", model);
    }

    public void setSearchTermService(SearchTermService searchTermService) {
        this.searchTermService = searchTermService;
    }

    private SearchTermService searchTermService;
}
