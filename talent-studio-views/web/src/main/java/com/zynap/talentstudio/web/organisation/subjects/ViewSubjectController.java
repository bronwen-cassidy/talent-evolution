/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.BrowseSubjectController;
import com.zynap.talentstudio.web.organisation.DeleteNodeController;
import com.zynap.talentstudio.web.organisation.SubjectBrowseNodeWrapper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Class or Interface description.
 *
 * @author bcassidy, aandersson
 * @version $Revision: $
 *          $Id: $
 */
public final class ViewSubjectController extends BrowseSubjectController {

    /**
     * Get the backing object used by this wizard.
     *
     * @param request HttpServletRequest
     * @return instance of {@link com.zynap.talentstudio.web.organisation.BrowseNodeWrapper}
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {

        final boolean newNode = RequestUtils.getBooleanParameter(request, ControllerConstants.NEW_NODE, false);

        BrowseNodeWrapper browseNodeWrapper = recoverFormBackingObject(request);
        if (browseNodeWrapper == null) {
            Long nodeId = DeleteNodeController.getNodeId(request);
            browseNodeWrapper = new SubjectBrowseNodeWrapper(null, null, null);
            browseNodeWrapper.setNodeId(nodeId);
            browseNodeWrapper.setNodeType(Node.SUBJECT_UNIT_TYPE_);
            setDisplayInfo(browseNodeWrapper, request);
            updateNodeInfo(browseNodeWrapper, ZynapWebUtils.getUserSession(request));
            browseNodeWrapper.setNewNode(newNode);
        }

        return browseNodeWrapper;
    }
}
