package com.comolroy.helloworld.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.comolroy.helloworld.dto.ForgotPasswordForm;
import com.comolroy.helloworld.entities.User;
import com.comolroy.helloworld.services.UserService;

@Component
public class ForgotPasswordFormValidator extends LocalValidatorFactoryBean {

	UserService userService;
	
	
	@Autowired
	public ForgotPasswordFormValidator(UserService userService) {
		this.userService = userService;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.beanvalidation.SpringValidatorAdapter#supports(java.lang.Class)
	 * Need to override following two method from LocalValidatorFactoryBean
	 */
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(ForgotPasswordForm.class);
	}

	@Override
	public void validate(Object target, Errors errors, Object... validationHints) {
		super.validate(target, errors, validationHints);
		
		if(!errors.hasErrors()){
			ForgotPasswordForm forgotPasswordForm = (ForgotPasswordForm)target;
			User user = userService.loadUserByEmail(forgotPasswordForm.getEmail());
			
			if(user==null){
				errors.rejectValue("email", "emailNotFound");
			}
		}
	}

}
