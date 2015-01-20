/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Mar-2007 16:52:29
 */
public abstract class BaseCorporateObjectiveController extends DefaultWizardFormController implements ObjectiveConstants {

    protected final void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) command;
        if (isFinishRequest(request)) {
            getValidator().validate(command, errors);            
        } else {

            switch (getTargetPage(request, page)) {

                case ADD_OBJECTIVE_IDX:

                    ObjectiveWrapperBean wrapperBean = new ObjectiveWrapperBean();
                    wrapperBean.setObjective(new Objective());
                    bean.addObjective(wrapperBean);
                    break;

                case REMOVE_OBJECTIVE_IDX:

                    int deleteIndex = RequestUtils.getIntParameter(request, "deleteIndex", -1);
                    if (deleteIndex > -1) {
                        bean.deleteObjective(deleteIndex);
                    }
                    break;
            }
        }
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    protected static final int SAVE_DEFINITION_IDX = 1;
    private static final int ADD_OBJECTIVE_IDX = 2;
    private static final int REMOVE_OBJECTIVE_IDX = 3;
    IObjectiveService objectiveService;
    static final String CORPORATE_ID_PARAM = "id";
}
