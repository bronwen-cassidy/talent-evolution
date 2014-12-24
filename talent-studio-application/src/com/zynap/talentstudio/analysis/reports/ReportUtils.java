/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.common.util.StringUtil;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Feb-2007 14:14:00
 */
public abstract class ReportUtils {

    public static List<AnalysisParameter> getNonAssociatedQuestionnaireAttributes(Report report) {
        final List<AnalysisParameter> results = new ArrayList<AnalysisParameter>();

        for (AnalysisParameter attribute : report.getQuestionnaireAttributes()) {
            if (!attribute.isAssociatedArtefactAttribute()) {
                results.add(attribute);
            }
        }

        return results;
    }

    public static String formatCSV(StringBuffer output, String delimiter) {
        // tokenize the buffer by new lines
        StringBuffer result = new StringBuffer();
        // get the first row for the headings
        final String str = output.toString();
        final String[] strings = StringUtils.delimitedListToStringArray(str, ReportConstants.ROW_SEPARATOR);

        String[] populatedColumns = null;
        // extract the first one as this is our headers, remain unchanged
        final String headers = StringUtils.replace(strings[0], ReportConstants.COL_SEPARATOR, delimiter);
        result.append(headers).append(StringUtil.LINE_SEPARATOR_WINDOWS);
        
        for (int i = 1; i < strings.length; i++) {
            String row = strings[i];
            // tokenize into columns now, cannot split by comma unfortunately 
            String[] columns = StringUtils.delimitedListToStringArray(row, ReportConstants.COL_SEPARATOR);
            if(populatedColumns == null) populatedColumns = columns;

            for (int j = 0; j < columns.length; j++) {
                if(!StringUtils.hasText(columns[j])) {
                    columns[j] = populatedColumns[j];
                }
                if(columns[j].indexOf(delimiter) != -1 && !columns[j].startsWith("\"")) {
                    result.append("\"").append(columns[j]).append("\"");    
                } else result.append(columns[j]);
                if(j < columns.length -1 ) result.append(delimiter);
            }
            populatedColumns = columns;

            // add in the \r\n now to represent a new row
            if(i < strings.length - 1) result.append(StringUtil.LINE_SEPARATOR_WINDOWS);
        }
        return result.toString();
    }
}
