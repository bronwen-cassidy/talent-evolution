/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.organisation.DeleteNodeController;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DeleteSubjectController extends DeleteNodeController {

    /**
     * Get node.
     *
     * @param nodeId The node id
     * @return A Node
     * @throws com.zynap.exception.TalentStudioException
     */
    protected Node getNode(Long nodeId) throws TalentStudioException {
        return subjectService.findById(nodeId);
    }

    /**
     * Delete node.
     *
     * @param command The command object
     * @throws com.zynap.exception.TalentStudioException
     */
    protected void deleteNode(Object command) throws TalentStudioException {
        Subject subject = (Subject) command;
        getSubjectService().delete(subject);
    }

    public ISubjectService getSubjectService() {
        return subjectService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private ISubjectService subjectService;
}
