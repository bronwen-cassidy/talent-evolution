package com.zynap.web.controller.admin;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

/**
 * User: amark
 * Date: 10-Jun-2005
 * Time: 16:51:07
 */
public class UserRedirectView extends ZynapRedirectView {

	/**
	 * Create a new RedirectView with the given URL.
	 *
	 * @param url the URL to redirect to
	 * @param user The user
	 */
	public UserRedirectView(String url, User user) {

		super(url);
		addStaticAttribute(ParameterConstants.USER_ID, user.getId());
	}
}
