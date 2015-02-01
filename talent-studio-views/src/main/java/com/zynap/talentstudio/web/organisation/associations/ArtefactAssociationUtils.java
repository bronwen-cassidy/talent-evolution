package com.zynap.talentstudio.web.organisation.associations;

import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 04-Jul-2006
 * Time: 10:06:47
 */
public final class ArtefactAssociationUtils {

    /**
     * Remove all entries from the list whose id is in the request
     * as a parameter prefixed by {@link #DELETE_ASSOC_PREFIX} and have a value which is NOT {@link #UNASSIGNED_VALUE}.
     * <br> The format for the parameter value is always "id:position" where position is the position in the List.
     * <br> That is why this uses a list not a collection - wouldn't work otherwise as it depends on the order of elements
     * in the collection being fixed.
     *
     * @param request
     * @param associations
     */
    public static void removeAssociations(HttpServletRequest request, List<ArtefactAssociationWrapperBean> associations) {

        final Collection parameters = WebUtils.getParametersStartingWith(request, DELETE_ASSOC_PREFIX).entrySet();
        for (Iterator iterator = parameters.iterator(); iterator.hasNext();) {
            final Map.Entry entry = (Map.Entry) iterator.next();

            final String value = (String) entry.getValue();
            if (!UNASSIGNED_VALUE.equals(value)) {
                final String parameter = (String) entry.getKey();
                final String[] idValues = StringUtils.delimitedListToStringArray(parameter, DELIMITER);
                int index = Integer.parseInt(idValues[1]);
                if (index < associations.size()) {
                    associations.remove(index);
                }
            }
        }
    }

    public static boolean isAddAssociationRequest(HttpServletRequest request) {
        return StringUtils.hasText(request.getParameter(ADD_ASSOC_PREFIX));
    }

    public static boolean isDeleteAssociationRequest(HttpServletRequest request) {
        return !(WebUtils.getParametersStartingWith(request, DELETE_ASSOC_PREFIX).isEmpty());
    }

    public static boolean isAssociationModificationRequest(HttpServletRequest request) {
        return (isAddAssociationRequest(request) || isDeleteAssociationRequest(request));
    }

    private static final String DELETE_ASSOC_PREFIX = "_deleteAssoc";
    private static final String ADD_ASSOC_PREFIX = "_addAssociation";
    private static final String UNASSIGNED_VALUE = "-1";
    private static final String DELIMITER = ":";
}
