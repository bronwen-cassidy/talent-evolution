package com.zynap.talentstudio.web.utils.tree;

import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.NodeSearchQueryWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectSearchQueryWrapper;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author jsuiras
 * @version 0.1
 * @since 17-Aug-2005 11:56:35
 */
public class TreeWrapperBean implements Serializable {


    public TreeWrapperBean(List<Branch> tree, Map branches, String popupId, String leafIcon) {
        this.tree = tree;
        this.branches = branches;
        this.popupId = popupId;
        this.leafIcon = leafIcon;
    }

    public List<Branch> getTree() {
        return tree;
    }

    public void setTree(List<Branch> tree) {
        this.tree = tree;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Map getBranches() {
        return branches;
    }

    public void setBranches(Map branches) {
        this.branches = branches;
    }

    public String getPopupId() {
        return popupId;
    }

    public void setPopupId(String popupId) {
        this.popupId = popupId;
    }

    public String getLeafIcon() {
        return leafIcon;
    }

    public void setLeafIcon(String leafIcon) {
        this.leafIcon = leafIcon;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        if (!StringUtils.hasText(positionId)) this.positionId = null;
        else this.positionId = new Long(positionId);
    }

    public void setFilter(NodeSearchQueryWrapper filter) {
        this.filter = filter;
    }

    public NodeSearchQueryWrapper getFilter() {
        return filter;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public List getResults() {
        return results;
    }

    public void setIsSearchRun(boolean isSearchRun) {

        searchRun = isSearchRun;
    }

    public boolean isSearchRun() {
        return searchRun;
    }

    private List<Branch> tree;
    private String branchId;
    private Map branches;
    private String popupId;
    private String leafIcon;
    private Long positionId;
    private NodeSearchQueryWrapper filter;
    private List results;
    private boolean searchRun=false;
}
