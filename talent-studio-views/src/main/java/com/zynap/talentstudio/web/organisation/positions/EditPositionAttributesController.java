package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.EditNodeAttributesController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: ssoong
 * Date: 24-Nov-2004
 * Time: 16:04:58
 */
public class EditPositionAttributesController extends EditNodeAttributesController {

    public EditPositionAttributesController() {
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));
        //get the id from the request and get the position
        Long positionId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        Position position = getPositionService().findByID(positionId);
        PositionWrapperBean wrapper = new PositionWrapperBean(position);
        applyAttributes(request, wrapper, position);
        return wrapper;
    }

    public final ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        PositionWrapperBean positionWrapperBean = (PositionWrapperBean) command;

        boolean imageAttributeDeleted = clearImageExtendedAttribute(request, positionWrapperBean);
        if (imageAttributeDeleted) return showForm(request, response, errors);

        Position pos = positionWrapperBean.getModifiedPosition(UserSessionFactory.getUserSession().getUser());
        pos.setActive(RequestUtils.getBooleanParameter(request, "active", false));
        positionService.update(pos);
        positionService.updateCurrentHoldersInfo(pos.getId());
        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.NODE_ID_PARAM, pos.getId());
        return new ModelAndView(view);
    }
}