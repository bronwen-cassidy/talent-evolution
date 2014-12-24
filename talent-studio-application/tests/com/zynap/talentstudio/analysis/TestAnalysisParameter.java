/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 08-Feb-2006 10:10:11
 * @version 0.1
 */
import com.zynap.talentstudio.ZynapTestCase;

public class TestAnalysisParameter extends ZynapTestCase {

    public void testIsDynamicAttribute() throws Exception {
        String simpleName = "39";
        AnalysisParameter attribute = new AnalysisParameter(simpleName, null, null);
        assertEquals(true, attribute.isDynamicAttribute());
        assertEquals("", attribute.getNestedPathWithoutId());
    }

    public void testIsDynamicAttributePositionNested() throws Exception {
        String name = "position.39";
        AnalysisParameter attribute = new AnalysisParameter(name, null, null);
        assertEquals(true, attribute.isDynamicAttribute());
    }

    public void testIsDynamicAttributeSubjectNested() throws Exception {
        String name = "subject.39";
        AnalysisParameter attribute = new AnalysisParameter(name, null, null);
        assertEquals(true, attribute.isDynamicAttribute());
    }

    public void testIsDynamicAttributeComplexNested() throws Exception {
        String name = "subjectPrimaryAssociations.subject.39";
        AnalysisParameter attribute = new AnalysisParameter(name, null, null);
        assertEquals(true, attribute.isDynamicAttribute());
    }

    public void testGetNestedPathWithoutId() throws Exception {
        String name = "subjectPrimaryAssociations.subject.subjectSecondaryAssociations.position.39";
        AnalysisParameter attribute = new AnalysisParameter(name, null, null);        
        assertEquals("subjectPrimaryAssociations.subject.subjectSecondaryAssociations.position", attribute.getNestedPathWithoutId());
    }
    
    public void testGetPrefixWithoutQuestionId() throws Exception {
        String name = "subjectPrimaryAssociations.subject.39";
        AnalysisParameter attribute = new AnalysisParameter(name, new Long(-12), null);
        String questionPathPrefix = attribute.getNestedPathWithoutId();
        assertEquals("subjectPrimaryAssociations.subject", questionPathPrefix);
    }
}