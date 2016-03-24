package com.comolroy.helloworld.services;

import java.util.List;

import com.comolroy.helloworld.dto.ForgotPasswordForm;
import com.comolroy.helloworld.dto.SignupForm;
import com.comolroy.helloworld.entities.User;

public interface UserService {

	public abstract void signup(SignupForm signup);

	public abstract void verify(String varificationCode);

	public abstract List<User> getAllUser();

	public abstract void forgotPassword(ForgotPasswordForm forgotPasswordForm);
	
	public abstract User loadUserByEmail(String email);

//	public abstract void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm,
//			BindingResult result);


}
