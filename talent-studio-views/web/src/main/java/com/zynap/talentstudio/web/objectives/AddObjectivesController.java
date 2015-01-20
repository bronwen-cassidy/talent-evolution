/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller that adds objectives to subjects.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-May-2006 13:14:20
 */
public class AddObjectivesController extends DefaultObjectivesController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ObjectiveSetFormBean wrapperBean = (ObjectiveSetFormBean) super.formBackingObject(request);
        if (wrapperBean.getObjectives().isEmpty()) {
            addObjective(wrapperBean);
        }
        return wrapperBean;
    }
}
