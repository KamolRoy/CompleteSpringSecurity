package com.comolroy.cssecurity.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.comolroy.cssecurity.dto.SignupForm;
import com.comolroy.cssecurity.entities.User;
import com.comolroy.cssecurity.repositories.UserRepository;
import com.comolroy.cssecurity.services.UserService;

@Component
public class SignupFormValidator extends LocalValidatorFactoryBean {
	
//	private UserRepository userRepository;
	private UserService userService;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository, UserService userService) {
//		this.userRepository = userRepository;
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(SignupForm.class);
	}

	@Override
	public void validate(Object target, Errors errors, Object... validationHints) {
		super.validate(target, errors, validationHints);
		
		if(!errors.hasErrors()){
			
			SignupForm signupForm=(SignupForm) target;
			User user = userService.loadUserByEmail(signupForm.getEmail());
			
//			User user= userRepository.findByEmail(singupForm.getEmail());
			if(user != null){
				errors.rejectValue("email", "emailNotUnique");
			}
			
			
		}
		
	}

}
