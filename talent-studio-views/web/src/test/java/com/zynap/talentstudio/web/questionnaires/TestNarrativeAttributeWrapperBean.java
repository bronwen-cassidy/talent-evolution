/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestNarrativeAttributeWrapperBean extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        QuestionAttribute narrative = new QuestionAttribute();
        narrative.setNarrative(LABEL);
        narrativeAttributeWrapperBean = new NarrativeAttributeWrapperBean(narrative);
    }

    public void testGetValue() throws Exception {
        assertNull(narrativeAttributeWrapperBean.getValue());
    }

    public void testGetLabel() throws Exception {
        assertEquals(LABEL, narrativeAttributeWrapperBean.getLabel());
    }

    public void testGetId() throws Exception {
        assertNull(narrativeAttributeWrapperBean.getId());
    }

    public void testIsEditable() throws Exception {
        assertFalse(narrativeAttributeWrapperBean.isEditable());
    }

    public void testGetType() throws Exception {
        assertEquals(NarrativeAttributeWrapperBean.NARRATIVE_QUESTION_TYPE, narrativeAttributeWrapperBean.getType());
    }

    private NarrativeAttributeWrapperBean narrativeAttributeWrapperBean;
    private static final String LABEL = "NARRATIVE1";
}