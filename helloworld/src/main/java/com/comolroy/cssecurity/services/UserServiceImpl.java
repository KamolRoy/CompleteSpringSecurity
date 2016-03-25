package com.comolroy.cssecurity.services;

import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.BindingResult;

import com.comolroy.cssecurity.dto.ForgotPasswordForm;
import com.comolroy.cssecurity.dto.ResetPasswordForm;
import com.comolroy.cssecurity.dto.SignupForm;
import com.comolroy.cssecurity.dto.UserDetailsImpl;
import com.comolroy.cssecurity.dto.UserEditForm;
import com.comolroy.cssecurity.entities.User;
import com.comolroy.cssecurity.entities.User.Role;
import com.comolroy.cssecurity.mail.MailSender;
import com.comolroy.cssecurity.util.MyUtil;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

	// private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private MailSender mailSender;

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	public UserServiceImpl(PasswordEncoder passwordEncoder, MailSender mailSender) {
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUser() {
		return this.sessionFactory.getCurrentSession().createQuery("from User").list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.comolroy.helloworld.services.UserService#signup(com.comolroy.
	 * helloworld.dto.SignupForm) The method is used by RootController's
	 * signup(value = "/signup", method = RequestMethod.POST) Signup method in
	 * User-Service-Impl will save a new user when the user's input verification
	 * is done. After saving the user, it will send email with verification
	 * link.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void signup(SignupForm signupForm) {
		final User user = new User();
		user.setEmail(signupForm.getEmail());
		user.setName(signupForm.getName());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.getRoles().add(Role.UNVERIFIED);
		// user.setRoles(Role.UNVERIFIED.toString());
		user.setVarificationCode(RandomStringUtils.randomAlphanumeric(16));
		System.out.println("User " + user);

		// With Hibernate

		getCurrentSession().save(user);

		// With Spring JPA

		// userRepository.save(user);

		// This part is to ensure that the verification link will be send after
		// user is saved in database.
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

			@Override
			public void afterCommit() {
				String verifyLink = new String(MyUtil.hostUrl()).trim() + "/users/" + user.getVarificationCode()
						+ "/verify";

				try {
					mailSender.send(user.getEmail(), MyUtil.getMessage("verifySubject"),
							MyUtil.getMessage("verifyEmail", verifyLink));
					logger.info("Verification mail to " + user.getEmail() + " queued.");
				} catch (MessagingException e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			}

		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.comolroy.helloworld.services.UserService#verify(java.lang.String)
	 * This Method is called by User Controller's verify method. It verify the
	 * supplied verification code against user's one. As
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void verify(String varificationCode) {

		long loggedInUserID = MyUtil.getSessionUser().getId();
		System.out.println("Call from Verify method.");

		// Using Hibernate
		Criteria crit = getCurrentSession().createCriteria(User.class);
		crit.add(Restrictions.idEq(loggedInUserID));
		User user = (User) crit.uniqueResult();

		// Using Spring JPA
		// User user = userRepository.findOne(loggedInUserID);

		MyUtil.validate(user.getRoles().contains(Role.UNVERIFIED), "alreadyVerified");
		MyUtil.validate(user.getVarificationCode().equals(varificationCode), "incorrect", "verification code");

		// user.setRoles(Role.VERIFIED.toString());
		user.getRoles().remove(Role.UNVERIFIED);
		user.setVarificationCode(null);
		getCurrentSession().save(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.comolroy.helloworld.services.UserService#forgotPassword(com.comolroy.
	 * helloworld.dto.ForgotPasswordForm) This method is called from
	 * UserController Forgot Password Post method.
	 */

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void forgotPassword(ForgotPasswordForm forgotPasswordForm) {
		final User user = loadUserByEmail(forgotPasswordForm.getEmail());
		final String forgotPasswordCode = RandomStringUtils.randomAlphanumeric(User.RANDOM_LEANGTH);

		user.setForgotPasswordCode(forgotPasswordCode);
		getCurrentSession().save(user);

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

			@Override
			public void afterCommit() {
				try {
					mailForgotPasswordLink(user);
				} catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			}
		});

	}

	/*
	 * This method is called from UserServiceImpl forgotPassword method.
	 */
	private void mailForgotPasswordLink(User user) throws MessagingException {
		String forgotPasswordLink = MyUtil.hostUrl().trim() + "/reset-password/" + user.getForgotPasswordCode();
		mailSender.send(user.getEmail(), MyUtil.getMessage("forgotPasswordSubject"),
				MyUtil.getMessage("forgotPasswordEmail", forgotPasswordLink));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.comolroy.helloworld.services.UserService#resetPassword(java.lang.String, com.comolroy.helloworld.dto.ResetPasswordForm, org.springframework.validation.BindingResult)
	 * This method is called from UserController Reset Password Post method
*/	 

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm, BindingResult result) {
		Criteria crit = getCurrentSession().createCriteria(User.class);
		crit.add(Restrictions.eq("forgotpasswordcode", forgotPasswordCode));
		
		User user= (User)crit.uniqueResult();
		
		if(user==null)
			result.reject("invalidForgotPassword");
		
		if(result.hasErrors())
			return;
		
		user.setForgotPasswordCode(null);
		user.setPassword(passwordEncoder.encode(resetPasswordForm.getPassword().trim()));
		getCurrentSession().saveOrUpdate(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.comolroy.helloworld.services.UserService#loadUserByEmail(java.lang.
	 * String) This method is called from ForgotPasswordFormValidator and
	 * SignupFormValidator validate method.
	 */
	public User loadUserByEmail(String email) {
		Criteria crit = getCurrentSession().createCriteria(User.class);
		crit.add(Restrictions.eq("email", email));
		return (User) crit.uniqueResult();
	}
	
	/*
	 * Called form findOne method
	 */
	public User loadUserById(long userId) {
		Criteria crit = getCurrentSession().createCriteria(User.class);
		crit.add(Restrictions.idEq(userId));
		return (User) crit.uniqueResult();
	}
	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see com.comolroy.helloworld.services.UserService#findOne(long)
	 * This method is called from User Controller getByID method
	 */
	@Override
	public User findOne(long userId) {
		User user = loadUserById(userId);
		User loggedIn = MyUtil.getSessionUser();
		
		if(loggedIn == null || loggedIn.getId() != user.getId() && !loggedIn.isAdmin()){
			//hide user email id
			user.setEmail("Confidential");
		}
		
		return user;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.comolroy.helloworld.services.UserService#update(long, com.comolroy.helloworld.dto.UserEditForm)
	 * Called form User Controller edit method.
	 */

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void update(long userId, UserEditForm userEditForm) {
		User loggedIn = MyUtil.getSessionUser();
		MyUtil.validate(loggedIn.isAdmin() || loggedIn.getId() == userId, "noPermission");
		User user = loadUserById(userId);
		user.setName(userEditForm.getName());
		if(loggedIn.isAdmin())
			user.setRoles(userEditForm.getRoles());
		getCurrentSession().saveOrUpdate(user);
	}

	@Override
	// This method is written because of implementing UserDetailsService
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// Using Hibernate
		Criteria crit = getCurrentSession().createCriteria(User.class);
		crit.add(Restrictions.eq("email", username));
		User user = (User) crit.uniqueResult();

		// Using Spring JPA
		// User user = userRepository.findByEmail(username);

		if (user == null)
			throw new UsernameNotFoundException(username);

		return new UserDetailsImpl(user);
	}

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

}
