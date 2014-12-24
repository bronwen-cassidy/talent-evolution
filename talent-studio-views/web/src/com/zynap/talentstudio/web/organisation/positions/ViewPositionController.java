package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.BrowsePositionController;
import com.zynap.talentstudio.web.organisation.DeleteNodeController;
import com.zynap.talentstudio.web.organisation.PositionBrowseNodeWrapper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: ssoong
 * Date: 26-Apr-2004
 * Time: 15:09:51
 */
public class ViewPositionController extends BrowsePositionController {

    /**
     * Get the backing object used by this wizard.
     *
     * @param request
     * @return instance of {@link com.zynap.talentstudio.web.organisation.BrowseNodeWrapper}
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final boolean newNode = RequestUtils.getBooleanParameter(request, ControllerConstants.NEW_NODE, false);

        BrowseNodeWrapper browseNodeWrapper = recoverFormBackingObject(request);
        if (browseNodeWrapper == null) {
            Long nodeId = DeleteNodeController.getNodeId(request);
            browseNodeWrapper = new PositionBrowseNodeWrapper(null, null, null);
            browseNodeWrapper.setNodeId(nodeId);
            browseNodeWrapper.setNodeType(Node.POSITION_UNIT_TYPE_);
            setDisplayInfo(browseNodeWrapper, request);
            updateNodeInfo(browseNodeWrapper, ZynapWebUtils.getUserSession(request));
            browseNodeWrapper.setNewNode(newNode);
        }

        return browseNodeWrapper;
    }
}

