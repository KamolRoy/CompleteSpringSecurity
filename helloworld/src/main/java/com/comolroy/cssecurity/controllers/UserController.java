package com.comolroy.cssecurity.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.comolroy.cssecurity.dto.UserEditForm;
import com.comolroy.cssecurity.entities.User;
import com.comolroy.cssecurity.services.UserService;
import com.comolroy.cssecurity.util.MyUtil;

/*
 * User Controller Class
 * Manage user related service
 */
@Controller
// With this class level request mapping, it add '/users' Url to each of method level request mapping
@RequestMapping("/users")
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
	@RequestMapping("/{varificationCode}/verify")
	public String verify(@PathVariable("varificationCode") String varificationCode,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ServletException {
		// String contextPath=request.getContextPath();
		userService.verify(varificationCode);

		MyUtil.flash(redirectAttributes, "success", "varificationSuccess");
		request.logout();

		return "redirect:/";
	}

	// With class level request mapping, not need to add '/users' in method level
	// @RequestMapping(value="/users/{userID}")
	@RequestMapping(value = "/{userId}")
	public String getById(@PathVariable("userId") long userId, Model model) {
		model.addAttribute(userService.findOne(userId));
		return "user";
	}
	
	
	
	@RequestMapping(value = "/{userId}/edit", method= RequestMethod.GET)
	public String edit(@PathVariable("userId") long userId, Model model) {
		User user = userService.findOne(userId);
		
		UserEditForm userEditForm = new UserEditForm();
		userEditForm.setName(user.getName());
		userEditForm.setRoles(user.getRoles());
		model.addAttribute("userEditForm", userEditForm);
		
		return "user-edit";
	}
	
	@RequestMapping(value = "/{userId}/edit", method= RequestMethod.POST)
	public String edit(@PathVariable("userId") long userId, @ModelAttribute("userEditForm") UserEditForm userEditForm, BindingResult result,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ServletException {
		
		if(result.hasErrors()){
			return "user-edit";
		}
		
		userService.update(userId, userEditForm);
		MyUtil.flash(redirectAttributes, "success", "editSuccessful");
		request.logout();
		
		return "redirect:/";
	}
	

}
