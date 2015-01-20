package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.controller.ViewConfig;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;


/**
 * class description.
 * 
 * User: jsuiras
 * Date: 20-Apr-2005
 * Time: 14:42:35
 */
public class AddPositionWizardFormController extends BasePositionWizardController {

    private ViewConfig cancelViewConfig;
    private static final String ADD_POSITION_CORE_VIEW = "addposition";

    public AddPositionWizardFormController() {
        super();
    }

    public void setCancelViewConfig(ViewConfig cancelViewConfig) {
        this.cancelViewConfig = cancelViewConfig;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long organisationId = RequestUtils.getLongParameter(request, ParameterConstants.ORG_UNIT_ID_PARAM);
        OrganisationUnit organisationUnit = new OrganisationUnit();
        if (organisationId != null) {
            organisationUnit.setId(organisationId);
        }

        Position position = new Position(organisationUnit);
        PositionWrapperBean positionWrapperBean = new PositionWrapperBean(position);

        // set addable attributes and defined extended attributes on the wrapper
        DisplayConfig displayConfig = getDisplayConfigService().find(Node.POSITION_UNIT_TYPE_, DisplayConfig.ADD_TYPE);
        DisplayConfigItem configItem = displayConfig.getFirstDisplayConfigItem();
        DynamicAttributesHelper.assignDisplayConfigAttributes(positionWrapperBean, configItem, position, getDynamicAttributeService());

        positionWrapperBean.addNewPrimaryAssociation();
        return positionWrapperBean;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        PositionWrapperBean positionWrapperBean = (PositionWrapperBean) command;
        Position newPosition = positionWrapperBean.getModifiedPosition(UserSessionFactory.getUserSession().getUser());

        try {
            getPositionService().create(newPosition);
        } catch (DataIntegrityViolationException e) {

            // Need to reset ids to ensure that persistence layer still sees them as new unsaved objects
            positionWrapperBean.resetIds();

            errors.rejectValue("position.title", "error.duplicate.title", e.getMessage());
            return showForm(request, errors, ADD_POSITION_CORE_VIEW);
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ParameterConstants.NODE_ID_PARAM, newPosition.getId());

        // this parameter indicates that the position has just been added - used later by ViewPositionController
        parameters.put(ControllerConstants.NEW_NODE, Boolean.TRUE);
        RedirectView view = new ZynapRedirectView(getSuccessView(), parameters);

        return new ModelAndView(view);
    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return new ModelAndView(cancelViewConfig.getRedirectView());
    }
}
