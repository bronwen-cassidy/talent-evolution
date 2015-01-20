/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.mocks;

import javax.servlet.jsp.JspWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MockJspWriter extends JspWriter {

    public MockJspWriter(int principalId, boolean b) {
        super(principalId, b);
    }

    public MockJspWriter(int principalId, boolean b, Writer enclosingWriter) {
        super(principalId, b);
        value = new StringBuffer();
        wrappedWriter = enclosingWriter;
    }

    public void newLine() throws IOException {
        value.append("\n");
    }

    public void print(boolean b) throws IOException {
        value.append(b);
    }

    public void print(char c) throws IOException {
        value.append(c);
    }

    public void print(int i) throws IOException {
        value.append(i);
    }

    public void print(long l) throws IOException {
        value.append(l);
    }

    public void print(float v) throws IOException {
        value.append(v);
    }

    public void print(double v) throws IOException {
        value.append(v);
    }

    public void print(char[] chars) throws IOException {
        value.append(chars);
    }

    public void print(String label) throws IOException {
        value.append(label);
    }

    public void print(Object o) throws IOException {
        value.append(o);
    }

    public void println() throws IOException {
        value.append("\n");
    }

    public void println(boolean b) throws IOException {
        value.append(b).append("\n");
    }

    public void println(char c) throws IOException {
        value.append(c).append("\n");
    }

    public void println(int principalId) throws IOException {
    }

    public void println(long l) throws IOException {
        value.append(l).append("\n");
    }

    public void println(float v) throws IOException {
        value.append(v).append("\n");
    }

    public void println(double v) throws IOException {
        value.append(v).append("\n");
    }

    public void println(char[] chars) throws IOException {
        value.append(chars).append("\n");
    }

    public void println(String label) throws IOException {
        value.append(label).append("\n");
    }

    public void println(Object o) throws IOException {
        value.append(o).append("\n");
    }

    public void clear() throws IOException {
        value.delete(0, value.length());
    }

    public void clearBuffer() throws IOException {
        value.delete(0, value.length());
    }

    public void flush() throws IOException {
        //wrappedWriter.flush();
    }

    public void close() throws IOException {
        //wrappedWriter.close();
    }

    public int getRemaining() {
        return 0;
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        value.append(cbuf);
    }

    public void write(String str) throws IOException {
        value.append(str);
    }

    public Writer getWriter() throws IOException {
        wrappedWriter.write(value.toString());
        return wrappedWriter;
    }

    public String getValue() {
        return value.toString();
    }

    private Writer wrappedWriter;
    private StringBuffer value = new StringBuffer();
}
