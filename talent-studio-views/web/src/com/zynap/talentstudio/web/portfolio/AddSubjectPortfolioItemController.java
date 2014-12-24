package com.zynap.talentstudio.web.portfolio;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;

public class AddSubjectPortfolioItemController extends BaseAddPortfolioItemController {

    public AddSubjectPortfolioItemController() {
        super();
    }

    protected Node getNode(Long nodeId) throws TalentStudioException {
        return subjectService.findById(nodeId);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private ISubjectService subjectService;
}
