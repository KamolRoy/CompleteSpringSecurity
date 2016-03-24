package com.comolroy.helloworld.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.comolroy.helloworld.dto.ResetPasswordForm;

public class ResetPasswordFormValidator extends LocalValidatorFactoryBean {

	@Override
	public boolean supports(Class<?> clazz) {
		
		return clazz.isAssignableFrom(ResetPasswordForm.class);
	}

	@Override
	public void validate(Object target, Errors errors, Object... validationHints) {
		super.validate(target, errors, validationHints);
		if(!errors.hasErrors()){
			ResetPasswordForm resetPasswordForm = (ResetPasswordForm) target;
			if(!resetPasswordForm.getPassword().equals(resetPasswordForm.getRetypePassword())){
				//The method is different, because it is a global error
				errors.reject("passwordsDoNotMatch");
			}
		}
	}

}
