package com.comolroy.helloworld.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SmtpMailSender implements MailSender{
	
	private JavaMailSender javaMailSender;
	
	public void setJavaMailSender(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void send(String to, String subject, String body) throws MessagingException{
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		
		helper = new MimeMessageHelper(message, true); // True indicate mutlipart message
		
		helper.setSubject(subject);
		helper.setTo(to);
		helper.setText(body, true); // True indicate html continue using helper ojbect for more functionalities like adding attachment.
		
		javaMailSender.send(message);
	}
	
	
	
}