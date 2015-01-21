package com.zynap.talentstudio.integration.conversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Process a CSV file. The first line should contain the column headings. These headings can
 * then be used to obtain values from rows.
 */
public class CSV {

    protected BufferedReader br;
    protected ArrayList headings;
    protected ArrayList currentLine = new ArrayList();
    protected int rowCount = 0;
    protected int lineCount = 0;

    /**
     * Construct a new CSV instance. The specified file is opened and the column
     * headings read from the first row.
     */
    public CSV(File inputFile) throws IOException {

        br = new BufferedReader(new FileReader(inputFile));
        // todo: create list to be sorted here

        // Read the first line, which contains the column headings
        readNextRow();
        headings = (ArrayList) currentLine.clone();
    }

    /**
     * Close the file. Not strictly necessary, but may be helpful.
     */
    public void Close() {
        try {
            br.close();
        }
        catch (Exception e) {
        }

        br = null;
    }

    /**
     * Get data for specified column, specified by column number.
     */

    public String get(int columnNumber) {
        if (columnNumber >= currentLine.size()) {
            return "";
        } else {
            return (String) currentLine.get(columnNumber);
        }
    }

    /**
     * Get data for specified column, specified by column name (heading).
     */
    public String get(String columnHeading) throws NoSuchElementException {
        int columnNumber = headings.indexOf(columnHeading);

        if (columnNumber < 0) {
            throw new NoSuchElementException(columnHeading);
        } else {
            return get(columnNumber);
        }
    }

    /**
     * Return the number of rows so far processed. Rows can span multiple lines.
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Return the number of lines so far processed.
     */
    public int getLineCount() {
        return lineCount;
    }

    /**
     * Return an array of stringsholding the column headings.
     */
    public ArrayList getHeadings() {
        return headings;
    }

    /**
     * Read the next row of the csv file. Return true if sucessfull, false if the
     * end of the input file has been reached.
     */
    public boolean readNextRow() throws IOException {
        boolean inQuotedString = false;
        boolean quoted = false;
        String line;
        int len;
        int i;
        int startIdx;
        int endIdx;
        String columnData = "";

        currentLine.clear();
        
        do {
            line = br.readLine();
            lineCount++;

            if (line == null) {
                return false;
            }

            startIdx = 0;
            len = line.length();

            for (i = 0; i < len; i++) {
                if (line.charAt(i) == '"') {
                    if (inQuotedString) {
                        inQuotedString = false;
                        quoted = true;
                    } else {
                        inQuotedString = true;
                        startIdx = i + 1;
                    }
                }

                if (!inQuotedString && ((line.charAt(i) == ',') || (i == (len - 1)))) {
                    if (i == (len - 1)) {
                        if (line.charAt(i) == ',') {
                            endIdx = i;
                        } else {
                            endIdx = i + 1;
                        }
                    } else {
                        endIdx = i;
                    }

                    if (quoted) {
                        endIdx--;
                    }

                    if (startIdx < endIdx) {
                        columnData = columnData + line.substring(startIdx, endIdx);
                    } else {
                        // Empty column
                    }

                    currentLine.add(columnData.trim());
                    columnData = "";
                    startIdx = i + 1;
                    quoted = false;
                }
            }

            if (inQuotedString) {
                columnData = columnData + line.substring(startIdx, len) + "\n";
            }

        } while (inQuotedString);
        rowCount++;

        return true;
	}
}
