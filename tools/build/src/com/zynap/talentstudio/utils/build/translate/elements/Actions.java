package com.zynap.talentstudio.utils.build.translate.elements;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Actions implements Serializable {

    public void addAction(Action action) {
        int index = new Integer(action.getStep()).intValue();
        actions.add(index, action);
    }

    public List getActions() {
        return actions;
    }

    private List<Action> actions = new ArrayList<Action>();
}
