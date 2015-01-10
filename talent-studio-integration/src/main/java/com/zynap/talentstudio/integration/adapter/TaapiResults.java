package com.zynap.talentstudio.integration.adapter;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bronwen.
 * Date: 06/05/12
 * Time: 22:29
 */
public class TaapiResults implements AdaptorResults {

    public TaapiResults(List<Node> modifiedResults, List<Node> addResults, List<Node> erroredResults, List<Node> pendingResults) {
        this.modifiedResults = modifiedResults;
        this.addedResults = addResults;
        this.erroredResults = erroredResults;
        this.pendingResults = pendingResults;
    }

    public TaapiResults() {
        this(new ArrayList<Node>(), new ArrayList<Node>(), new ArrayList<Node>(), new ArrayList<Node>());
    }

    public void addModified(Node n) {
        modifiedResults.add(n);
    }

    public void addAdded(Node n) {
        addedResults.add(n);
    }

    public List<Node> getModifiedResults() {
        return modifiedResults;
    }

    public List<Node> getErroredResults() {
        return erroredResults;
    }

    public List<Node> getPendingResults() {
        return pendingResults;
    }

    public List<Node> getAddedResults() {
        return addedResults;
    }

    public void addErrored(Node node) {
        erroredResults.add(node);
    }

    public void addPending(Node node) {
        pendingResults.add(node);
    }

    private List<Node> modifiedResults;
    private List<Node> addedResults;
    private List<Node> erroredResults;
    private List<Node> pendingResults;
}
