package com.zynap.talentstudio.web.tag;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.web.mocks.MockBodyContent;
import com.zynap.web.mocks.MockJspWriter;
import junit.framework.TestCase;

import java.io.StringWriter;

public class TestDescriptionTag extends TestCase {

    public void setUp() {
        writer = new StringWriter();
        mockJspWriter = new MockJspWriter(0, true, writer);
        descriptionTag = new DescriptionTag();
    }

    public void testSetBodyContent() throws Exception {
        bodyContent = new MockBodyContent(mockJspWriter);
        descriptionTag.setBodyContent(bodyContent);
        assertNotNull(descriptionTag.getBodyContent());
    }

    public void testDoAfterBody_BreaksGenerated() throws Exception {
        bodyContent = new MockBodyContent(mockJspWriter, getBrString());
        descriptionTag.setBodyContent(bodyContent);
        descriptionTag.doAfterBody();
        assertEquals("This is a simple test<br/>testing chars 10 and 13 exist", mockJspWriter.getWriter().toString());
    }

    private String getBrString() {
        return "This is a simple test\r\ntesting chars 10 and 13 exist";
    }

    private DescriptionTag descriptionTag;
    private MockJspWriter mockJspWriter;
    private MockBodyContent bodyContent;
    private StringWriter writer;
}