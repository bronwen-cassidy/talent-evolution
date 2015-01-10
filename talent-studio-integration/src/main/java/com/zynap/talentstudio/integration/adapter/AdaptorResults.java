package com.zynap.talentstudio.integration.adapter;

import com.zynap.talentstudio.organisation.Node;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bronwen.
 * Date: 06/05/12
 * Time: 22:27
 */
public interface AdaptorResults extends Serializable {

    List<Node> getModifiedResults();
    List<Node> getAddedResults();
    List<Node> getErroredResults();
    List<Node> getPendingResults();
}
