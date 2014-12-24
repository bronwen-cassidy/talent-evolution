/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.attributes.AttributeValueFile;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectPicture;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ImageFileViewController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long nodeId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);

        if (myDetails) {
            UserSession userSession = ZynapWebUtils.getUserSession(request);
            if (userSession != null) {
                User user = userSession.getUser();
                Subject currentSubject = user != null ? user.getSubject() : null;
                if (currentSubject != null) {
                    nodeId = currentSubject.getId();
                }
            }
        }

        try {
            final boolean forceDownload = !ZynapWebUtils.isInternetExplorer(request);

            Long imageId = RequestUtils.getLongParameter(request, ParameterConstants.ITEM_ID);
            if (imageId != null) {
                AttributeValueFile item = dynamicAttributeService.findAttributeValueFile(imageId);
                // todo discover what the following if staatement was trying to achieve??
                //if (!(item.getNode().getId().equals(nodeId) || ((Questionnaire) item.getNode()).getSubjectId().equals(nodeId))) return null;
                if (item.getBlobValue() != null) {
                    ResponseUtils.writeToResponse(response, request, item.getValue(), item.getBlobValue(), forceDownload);
                }
            } else {
                final SubjectPicture subject = subjectService.findPicture(nodeId);
                final byte[] picture = subject.getPicture();
                if (picture != null) {
                    ResponseUtils.writeToResponse(response, request, "jpg", subject.getPicture(), forceDownload);
                }
            }
        } catch (Throwable e) {
        }

        return null;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setMyDetails(boolean myDetails) {
        this.myDetails = myDetails;
    }

    private IDynamicAttributeService dynamicAttributeService;
    private ISubjectService subjectService;
    private boolean myDetails;
}
