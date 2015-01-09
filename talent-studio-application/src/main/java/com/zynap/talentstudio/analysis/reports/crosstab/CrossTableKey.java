package com.zynap.talentstudio.analysis.reports.crosstab;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 15-Mar-2005
 * Time: 11:11:46
 */
public class CrossTableKey implements Serializable {

    private String rowValue;
    private String colValue;

    public CrossTableKey(String rowValue, String colValue) {
        this.rowValue = rowValue;
        this.colValue = colValue;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrossTableKey)) return false;
        final CrossTableKey crossTableKey = (CrossTableKey) o;
        if (colValue != null ? !colValue.equals(crossTableKey.colValue) : crossTableKey.colValue != null) return false;
        if (rowValue != null ? !rowValue.equals(crossTableKey.rowValue) : crossTableKey.rowValue != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (rowValue != null ? rowValue.hashCode() : 0);
        result = 29 * result + (colValue != null ? colValue.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CrossTableKey[");
        stringBuffer.append("\r\n rowValue=" + rowValue);
        stringBuffer.append("\r\n colValue=" + colValue);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    public String getRowValue() {
        return rowValue;
    }

    public String getColValue() {
        return colValue;
    }
}
