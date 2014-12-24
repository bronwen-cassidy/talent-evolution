package com.zynap.talentstudio.web.history;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Object that holds command objects in a map for history using tokens as keys.
 *
 * User: amark
 * Date: 22-Dec-2004
 * Time: 09:25:38
 */
public final class History implements Serializable {

    private final Map<String, List<SavedURL>> urls = new HashMap<String, List<SavedURL>>();

    public History() {
    }

    /**
     * Clear all history other than for the specified token.
     * @param currentToken
     */
    void clear(String currentToken) {
        if (StringUtils.hasText(currentToken)) {

            for (Iterator iterator = urls.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (!currentToken.equals(entry.getKey())) {
                    iterator.remove();
                }
            }
        } else {
            urls.clear();
        }
    }

    void removeLastURL(String token) {
        final List urlList = urls.get(token);
        if (urlList != null && !urlList.isEmpty()) {
            urlList.remove(0);
        }
    }

    void removeLastURL(String token, String requestURI) {
        final SavedURL savedURL = getURL(token);
        if (savedURL != null && requestURI.equals(savedURL.getURL())) {
            removeLastURL(token);
        }
    }

    SavedURL getLastUrl() {
        Set<Map.Entry<String,List<SavedURL>>> entries = urls.entrySet();
        if(!entries.isEmpty()) {
            Map.Entry<String, List<SavedURL>> listEntry = entries.iterator().next();
            List<SavedURL> value = listEntry.getValue();
            if (value != null && !value.isEmpty()) {
                return value.get(0);
            }
        }
        return null;
    }

    SavedURL getURL(String token) {
        final List urlList = urls.get(token);
        if (urlList != null && !urlList.isEmpty()) {
            return (SavedURL) urlList.get(0);
        }

        return null;
    }

    void addURL(String token, SavedURL savedURL) {

        List urlList = urls.get(token);
        if (urlList == null) {
            urlList = new ArrayList();
            urlList.add(0, savedURL);
            urls.put(token, urlList);
        }
        else if (!urlList.isEmpty()) {
            // get the first element and check the url
            final SavedURL firstURL = (SavedURL) urlList.get(0);
            if (firstURL != null && firstURL.getURL().equals(savedURL.getURL())) {
                // replace
                urlList.set(0, savedURL);
            } else {
                // add
                urlList.add(0, savedURL);
            }
        }
        else {
                urlList.add(0, savedURL);
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof History)) return false;

        final History history = (History) o;

        if (!urls.equals(history.urls)) return false;

        return true;
    }

    public int hashCode() {
        return urls.hashCode();
    }
}
