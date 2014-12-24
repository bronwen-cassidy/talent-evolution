/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.mocks;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MockBodyContent extends BodyContent {

    public MockBodyContent(JspWriter jspWriter) {
        super(jspWriter);
    }

    public MockBodyContent(JspWriter jspWriter, String content) {
        super(jspWriter);
        _content = content;
    }

    public Reader getReader() {
        return null;
    }

    public String getString() {
        return _content;
    }

    public void writeOut(Writer writer) throws IOException {
    }

    public void newLine() throws IOException {
    }

    public void print(boolean b) throws IOException {
    }

    public void print(char c) throws IOException {
    }

    public void print(int principalId) throws IOException {
    }

    public void print(long l) throws IOException {
    }

    public void print(float v) throws IOException {
    }

    public void print(double v) throws IOException {
    }

    public void print(char[] chars) throws IOException {
    }

    public void print(String label) throws IOException {
        getEnclosingWriter().print(label);
    }

    public void print(Object o) throws IOException {
    }

    public void println() throws IOException {
    }

    public void println(boolean b) throws IOException {
    }

    public void println(char c) throws IOException {
    }

    public void println(int principalId) throws IOException {
    }

    public void println(long l) throws IOException {
    }

    public void println(float v) throws IOException {
    }

    public void println(double v) throws IOException {
    }

    public void println(char[] chars) throws IOException {
    }

    public void println(String label) throws IOException {
    }

    public void println(Object o) throws IOException {
    }

    public void clear() throws IOException {
    }

    public void clearBuffer() throws IOException {
    }

    public void close() throws IOException {
    }

    public int getRemaining() {
        return 0;
    }

    /**
     * Write a portion of an array of characters.
     *
     * @param cbuf Array of characters
     * @param off  Offset from which to start writing characters
     * @param len  Number of characters to write
     * @throws java.io.IOException If an I/O error occurs
     */
    public void write(char cbuf[], int off, int len) throws IOException {
    }

    private String _content;
}
