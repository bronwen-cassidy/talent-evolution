package com.zynap.talentstudio.web.history;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: amark
 * Date: 22-Dec-2004
 * Time: 09:26:26
 */
public final class SavedURL implements Serializable {

    private final String url;
    private Map<String, Object> parameters = new HashMap<String, Object>();
    private Object command;

    public SavedURL(String url, Map<String, Object> parameters) {
        this.url = url;
        this.parameters = parameters;
    }

    public SavedURL(String url, Object command) {
        this.url = url;
        this.command = command;
    }

    public String getURL() {
        return url;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getCompleteURL() {

        Map<String, Object> temp = buildParameters();
        return ZynapWebUtils.buildURL(url, temp, false);
    }

    public Map buildParameters() {
        Map<String, Object> temp = new HashMap<String, Object>();

        for (Iterator iterator = this.parameters.entrySet().iterator(); iterator.hasNext();) {
            
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            String value;

            if (val instanceof String[]) {
                String[] values = (String[]) val;
                value = StringUtils.arrayToCommaDelimitedString(values);
            } else {
                value = val.toString();
            }

            temp.put(key, value);
        }

        return temp;
    }

    public Object getCommand() {
        return command;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SavedURL)) return false;

        final SavedURL savedURL = (SavedURL) object;

        if (command != null ? !command.equals(savedURL.command) : savedURL.command != null) return false;
        if (parameters != null ? !parameters.equals(savedURL.parameters) : savedURL.parameters != null) return false;
        if (!url.equals(savedURL.url)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = url.hashCode();
        result = 29 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 29 * result + (command != null ? command.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SavedURL[");
        stringBuffer.append("\r\n url=").append(url);
        stringBuffer.append("\r\n parameters=").append(parameters == null ? null : "size:" + parameters.size() + parameters);
        stringBuffer.append("\r\n command=").append(command);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }
}
