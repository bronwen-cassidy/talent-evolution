/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.util;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class IdGenerator extends AbstractIdGenerator {

    public String generateStringId(String baseString, String sequence) {
        int sequenceNum = getTemplate().queryForInt("select " + sequence +".nextval from dual");
        String trimmedBaseString = generateStringId(baseString);
        trimmedBaseString += sequenceNum;
        return trimmedBaseString;
    }

    public String generateStringId(String baseString) {
        return StringUtils.deleteAny(baseString, " ,'!?\"\\/-_@*+").toUpperCase();
    }
}
