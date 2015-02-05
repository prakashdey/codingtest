/**
 * AdminController.java
 */
package com.mvp.admin.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * This is the base controller for handling all controls in Admin page.
 * 
 * @author prakash
 *
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController implements AdminURLConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    /**
     * Landing page.
     * 
     * @return {@link ModelAndView} to admin page.
     */
	@RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView landingPage() {
        ModelAndView modelAndView = new ModelAndView(LANDING_PAGE);
        return modelAndView;
    }

}
