package com.zynap.util.format;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MessageTemplateFormatter.
 * Supplies methods to operate on a String with necessary parameters.
 *
 * @author Angus Mark
 */
public class MessageTemplateFormatter {
    /**
     * Left outter pattern delimiter
     */
    private static final String TOKEN_START = "\\$\\{(";

    /**
     * Right outter pattern delimiter
     */
    private static final String TOKEN_END = ")\\}";

    /**
     * Matcher pattern. Matches ${1 or more letters or words}
     */
    private static final Pattern MATCH_PATTERN = Pattern.compile(TOKEN_START + "[a-zA-Z_\\-0-9\\.]+" + TOKEN_END);

    /**
     * The String to perform parsing/replacement etc
     */
    private String template;

    /**
     * The list of properties within the given String
     */
    private List properties;

    /**
     * Default constructor
     *
     * @param template The String toParse
     */
    public MessageTemplateFormatter(String template) {
        setTemplate(template);
    }

    /**
     * Format the template with the given list of values.
     *
     * @param list The list of values
     * @return Formatted string
     */
    public String format(List list) {
        String formattedString = template;

        // For each String property

        for (Object property : properties) {
            String currentProperty = (String) property;

            // For each object in passed list

            for (Object obj : list) {
                Object value = null;

                if (obj instanceof Map) {
                    value = ((Map) obj).get(currentProperty);
                } else {
                    try {
                        value = BeanUtils.getNestedProperty(obj, currentProperty);
                    } catch (IllegalAccessException ignored) {
                    } catch (InvocationTargetException ignored) {
                    } catch (NoSuchMethodException ignored) {
                    }
                }

                if (value != null) {
                    formattedString = formattedString.replaceAll(TOKEN_START + currentProperty + TOKEN_END, value.toString());
                    break; // Go on to the next property
                }
            }
        }

        return formattedString;
    }

    /**
     * Will take an object as opposed to a list.
     *
     * @param object The object to format.
     * @return The formatted template.
     */
    public String format(Object object) {
        List list = new ArrayList(1);
        list.add(object);
        return format(list);
    }


    /**
     * Get the list of parameter/properties to alter within template.
     *
     * @return The list of parameter/properties to alter within template.
     */
    public List getProperties() {
        return properties;
    }

    /**
     * Get the template to be parsed.
     *
     * @return The template to be parsed.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the template to be parsed.
     *
     * @param template The template to be parsed.
     */
    public void setTemplate(String template) {
        this.template = template;
        properties = new ArrayList();

        // Parse String
        Matcher matcher = MATCH_PATTERN.matcher(template);

        while (matcher.find()) {
            properties.add(matcher.group(1));
        }

    }

}
