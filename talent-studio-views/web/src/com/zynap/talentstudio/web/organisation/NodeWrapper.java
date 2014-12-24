/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.domain.admin.User;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 27-Apr-2007 12:48:05
 */
public class NodeWrapper implements Serializable {


    public NodeWrapper(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public Long getId() {
        return node.getId();
    }

    public String getLabel() {
        return node.getLabel();
    }

    public boolean isHasPicture() {
        return node instanceof Subject && ((Subject) node).isHasPicture();
    }

    public String getJobTitles() {
        StringBuffer titleInfo = new StringBuffer(" ( ");
        if(node instanceof Subject) {
            List<Position> positions = ((Subject) node).getPrimaryPositions();
            int index = 0;
            for (Position position : positions) {
                titleInfo.append(position.getLabel());
                index++;
                if(index < positions.size()) titleInfo.append(", ");
            }
            titleInfo.append(" )");
            return titleInfo.toString();
        }
        return "";
    }

    public Long getUserId() {
        if(node instanceof Subject) {
            User user = ((Subject) node).getUser();
            return user != null ? user.getId() : null;
        }
        return null;
    }

    private Node node;
}
