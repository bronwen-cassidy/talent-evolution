/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */

package com.zynap.talentstudio.integration.tools;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 13-Jul-2007 10:57:38
 */

public class TemplateGenerator extends AbstractGenerator implements ITemplateGenerator {

    public void generate() {

        nodeDetails.put(Node.POSITION_UNIT_TYPE_, POSITION_START);
        nodeDetails.put(Node.SUBJECT_UNIT_TYPE_, SUBJECT_START);
        nodeDetails.put(Node.ORG_UNIT_TYPE_, ORGANISATION_START);

        for (Map.Entry<String, String> entry : nodeDetails.entrySet()) {
            generateTemplateFile(entry.getKey());
        }
    }

    private void generateTemplateFile(String type) {

        final String headers = getHeaders(type);
        writeToFile(type, headers);
    }

    private String getHeaders(String type) {

        StringBuilder header = new StringBuilder(nodeDetails.get(type));
        Collection<DynamicAttribute> attributes = dynamicAttributeService.getAllActiveAttributes(type, false);
        for (DynamicAttribute dynAttr : attributes) {
            header.append(COMMA);
            header.append(dynAttr.getExternalRefLabel());
        }
        return header.toString();
    }

    private void writeToFile(String type, String headers) {

        final String fileName = type + END_OF_FILENAME;
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(fileName));
            out.write(headers);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private final String COMMA = ",";
    public static final String END_OF_FILENAME = "_template.csv";

    private final String SUBJECT_START = "id,subjectAssociations.stpAssociation.target.id,subjectAssociations.stpAssociation@type,firstName,secondName,email,telephone,dateOfBirth";
    private final String POSITION_START = "id,title,organisationUnit.id,sourceAssociations.ptpAssociation.target.id,sourceAssociations.ptpAssociation@type";
    private final String ORGANISATION_START = "id,parent.id,label";

    public Map<String, String> nodeDetails = new HashMap<String, String>();
    
    private static final Log logger = LogFactory.getLog(TemplateGenerator.class);
}
