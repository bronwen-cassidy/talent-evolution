/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HtmlUtils {

    private static final Map<Character, String> HEX_ENTITLES = new HashMap<Character, String>(6);

    static {
        HEX_ENTITLES.put(new Character('&'), "%26");
        HEX_ENTITLES.put(new Character('?'), "%3F");
        HEX_ENTITLES.put(new Character('/'), "%2F");
        HEX_ENTITLES.put(new Character('='), "%3D");
        HEX_ENTITLES.put(new Character('\''), "%27");
        HEX_ENTITLES.put(new Character(' '), "%20");
    }

    public static String escapeHex(String value) {
        if (value == null) return value;
        if (value.length() == 0) return value;
        StringBuffer result = new StringBuffer();
        char[] characters = value.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            char character = characters[i];
            if (HEX_ENTITLES.containsKey(new Character(character))) {
                result.append(HEX_ENTITLES.get(new Character(character)));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    public static String htmlEscape(String input) {
        if (input == null) {
            return null;
        }

        StringBuffer escaped = new StringBuffer(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            // handle non-special ASCII chars first since they will be most common
            if ((c >= 0 && c <= 33)
                    || (c >= 35 && c <= 37)
                    || (c >= 40 && c <= 59)
                    || (c == 61)
                    || (c >= 63 && c <= 159)) {
                escaped.append(c);
                continue;
            }

            // handle special chars
            if (c == 34) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 38) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 39) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 60) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 62) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 160 && c <= 255) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 338 && c <= 339) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 352 && c <= 353) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 376) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 402) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 710) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 732) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 913 && c <= 929) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 931 && c <= 937) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 945 && c <= 969) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 977 && c <= 978) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 982) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8194 && c <= 8195) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8201) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8204 && c <= 8207) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8211 && c <= 8212) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8216 && c <= 8218) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8220 && c <= 8222) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8224 && c <= 8226) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8230) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8240) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8242 && c <= 8243) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8249 && c <= 8250) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8254) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8260) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8364) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8465) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8472) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8476) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8482) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8501) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8592 && c <= 8596) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8629) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8656 && c <= 8660) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8704) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8706 && c <= 8707) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8709) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8711 && c <= 8713) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8715) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8719) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8721 && c <= 8722) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8727) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8730) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8733 && c <= 8734) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8736) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8743 && c <= 8747) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8756) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8764) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8773) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8776) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8800 && c <= 8801) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8804 && c <= 8805) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8834 && c <= 8836) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8838 && c <= 8839) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8853) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8855) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8869) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 8901) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 8968 && c <= 8971) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c >= 9001 && c <= 9002) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 9674) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 9824) {
                writeDecimalReference(c, escaped);
                continue;
            }
            if (c == 9827) {
                writeDecimalReference(c, escaped);
                continue;
            }
            // all other chars
            escaped.append(c);
        }
        return escaped.toString();
    }

    private static void writeDecimalReference(char c, StringBuffer buf) {
        buf.append(REFERENCE_START);
        buf.append((int) c);
        buf.append(';');
    }

    private static final String REFERENCE_START = "&#";
}
