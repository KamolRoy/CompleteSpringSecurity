package com.comolroy.helloworld.controllers;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.comolroy.helloworld.dto.ForgotPasswordForm;
import com.comolroy.helloworld.dto.ResetPasswordForm;
import com.comolroy.helloworld.dto.SignupForm;
import com.comolroy.helloworld.entities.User;
import com.comolroy.helloworld.services.UserService;
import com.comolroy.helloworld.util.MyUtil;
import com.comolroy.helloworld.validator.ForgotPasswordFormValidator;
import com.comolroy.helloworld.validator.SignupFormValidator;

/*
 * @RestController is the combination of @Controller in class and @ResponseBody in method.
 * with out @ResponseBody, the method will return a path of view resolver
 */
//@RestController
@Controller
public class RootController {

	// private Logger logger = LoggerFactory.getLogger(RootController.class);

	private UserService userService;
	private SignupFormValidator signupFormValidator;
	private ForgotPasswordFormValidator forgotPasswordFormValidator;
//	private ResetPasswordFormValidator resetPasswordFormValidator;

	@Autowired
	public RootController( UserService userService, SignupFormValidator signupFormValidator, ForgotPasswordFormValidator forgotPasswordFormValidator
//			,ResetPasswordFormValidator resetPasswordFormValidator
			) {
		this.userService = userService;
		this.signupFormValidator = signupFormValidator;
		this.forgotPasswordFormValidator = forgotPasswordFormValidator;
//		this.resetPasswordFormValidator = resetPasswordFormValidator;
	}

	@InitBinder("signupForm")
	protected void initSignupBinder(WebDataBinder binder) {
		binder.setValidator(signupFormValidator);
	}
	
	@InitBinder("forgotPasswordForm")
	protected void initForgotPasswordFormBinder(WebDataBinder binder) {
		binder.setValidator(forgotPasswordFormValidator);
	}
	
	/*@InitBinder("resetPasswordForm")
	protected void initResetPasswordFormBinder(WebDataBinder binder) {
		binder.setValidator(resetPasswordFormValidator);
	}
*/
	@RequestMapping("/")
	// @ResponseBody
	public String home() throws MessagingException {

		// mailSender.send("comolroy@gmail.com", "Hello, world", "Mail from
		// spring");

		List<User> users = userService.getAllUser();

		for (User user : users) {
			System.out.println("System User: " + user);
		}

		return "home";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {

		model.addAttribute("signupForm", new SignupForm());

		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		if (result.hasErrors()) {
			return "signup";
		}

		System.out.println("Context Path: " + request.getRequestURI());

		userService.signup(signupForm);

		MyUtil.flash(redirectAttributes, "success", "signupSuccess");

		// logger.info("From Root Controller: " + signupForm.toString());

		return "redirect:/";
	}

	
	/*
	 * While user click on forgot password button in login form
	 */
	@RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
	public String forgotPassword(Model model) {

		model.addAttribute("forgotPasswordForm", new ForgotPasswordForm());

		return "forgot-password";
	}

	/*
	 * While user click on submit button on forgot password page
	 */
	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
	public String forgotPassword(@ModelAttribute("forgotPasswordForm") @Valid ForgotPasswordForm forgotPasswordForm,
			BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors())
			return "forgot-password";

		userService.forgotPassword(forgotPasswordForm);
		MyUtil.flash(redirectAttributes, "info", "checkMailResetPassword");

		return "redirect:/";
	}

	
	 /*
	  *  While user click the reset password link that sent by email
	  */
	 
	@RequestMapping(value = "/reset-password/{forgotPasswordCode}", method = RequestMethod.GET)
	public String resetPassword(@PathVariable("forgotPasswordCode") String forgotPasswordCode, Model model)
			throws ServletException {
		model.addAttribute("resetPasswordForm", new ResetPasswordForm());
		return "reset-password";
	}

	
	 /*
	  *  While user click on submit button of reset password from
	  */
	 
	/*@RequestMapping(value = "/reset-password/{forgotPasswordCode}", method = RequestMethod.POST)
	public String resetPassword(@PathVariable("forgotPasswordCode") String forgotPasswordCode,
			@ModelAttribute("resetPasswordForm") @Valid ResetPasswordForm resetPasswordForm, BindingResult result,
			RedirectAttributes redirectAttribute) {

		// Before form validation of reset-password form, need to check
		// formPasswordCode is valid.
		userService.resetPassword(forgotPasswordCode, resetPasswordForm, result);

		if (result.hasErrors()) {
			return "reset-password";
		}

		MyUtil.flash(redirectAttribute, "success", "passwordChanged");

		return "redirect:/login";
	}*/
}
