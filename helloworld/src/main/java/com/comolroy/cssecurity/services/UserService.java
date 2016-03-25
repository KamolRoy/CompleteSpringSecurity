package com.comolroy.cssecurity.services;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.comolroy.cssecurity.dto.ForgotPasswordForm;
import com.comolroy.cssecurity.dto.ResetPasswordForm;
import com.comolroy.cssecurity.dto.SignupForm;
import com.comolroy.cssecurity.dto.UserEditForm;
import com.comolroy.cssecurity.entities.User;

public interface UserService {

	public abstract void signup(SignupForm signup);

	public abstract void verify(String varificationCode);

	public abstract List<User> getAllUser();

	public abstract void forgotPassword(ForgotPasswordForm forgotPasswordForm);
	
	public abstract User loadUserByEmail(String email);

	public abstract void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm,
			BindingResult result);

	public abstract User findOne(long userID);

	public abstract void update(long userId, UserEditForm userEditForm);


}
