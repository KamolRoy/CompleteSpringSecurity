package com.comolroy.helloworld.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.comolroy.helloworld.services.UserService;
import com.comolroy.helloworld.util.MyUtil;

/*
 * User Controller Class
 * Manage user related service
 */
@Controller
public class UserController {

	private UserService userService;
	

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
		
	}

	

	/*
	 * >> Here 'varificationCode' is a path variable To access this link user
	 * has to login first. This method managed the verification supplied by user
	 * from what they get through email. After parsing the verification code the
	 * supply it to User Service to verify.
	 */
	@RequestMapping("/users/{varificationCode}/verify")
	public String verify(@PathVariable("varificationCode") String varificationCode,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ServletException {
		// String contextPath=request.getContextPath();
		userService.verify(varificationCode);

		MyUtil.flash(redirectAttributes, "success", "varificationSuccess");
		request.logout();

		return "redirect:/";
	}

	

}
