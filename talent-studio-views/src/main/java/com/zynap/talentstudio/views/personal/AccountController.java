package com.zynap.talentstudio.views.personal;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by bronwen.cassidy on 27/11/2015.
 * Controller to manage the angular output for the users personal account.
 */
@Controller
@RequestMapping("/personal/account")
public class AccountController {


    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET,headers = "Accept=application/json")
    public UserWrapperBean findAccount(Long id) {
        UserWrapperBean userWrapperBean = null;
        try {
            User user = (User) userService.findById(id);
            userWrapperBean = new UserWrapperBean(user);
        } catch (TalentStudioException e) {
            e.printStackTrace();
        }
        return userWrapperBean;
    }

}
