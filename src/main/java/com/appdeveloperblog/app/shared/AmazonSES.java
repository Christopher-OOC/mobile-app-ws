package com.appdeveloperblog.app.shared;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.appdeveloperblog.app.shared.dto.UserDto;

public class AmazonSES {
	
	final String FROM = "drealcrystal24@gmail.com";
	
	final String SUBJECT = "One last step to complete your registration with PhotoApp";
	
	final String PASSWORD_RESET_SUBJECT = "Password reset request";
	
	final String HTMLBODY = "<h1>Please verify your email address</h1>"
			+ "<p>Thank you for registering with our mobile app. To complete registration process and be able to login"
			+ " click on the following link: "
			+ "<a href='http://localhost:8001/email-verification?token=$tokenValue'>"
			+ "Final step to complete your registration" + "</a><br><br>"
			+ "Thank you! And we are waiting for you inside!";
	
	final String TEXTBODY = """
			Please verify your email address. 
			Thank you for registering with our mobile app. To complete registration process and be able to 
			open then the following URL in your browser window: 
			http://localhost:8001/email-verification?token=$tokenValue
			Thank you! And we  are waiting for you inside!
			""";
	
	final String PASSWORD_RESET_HTMLBODY = """
			<h1>A request to reset your password</h1>
			<p>Hi, $firstName!</p> 
			<p>Someone has requested to reset your password with our project. If it were not you, please ignore,
			otherwise please click on the link below to set a new passoword: 
			<a href='http://localhost:8001/verification-service/password-reset.html?token=$tokenValue'>
			Click this link to Reset Password
			</a><br><br>
			Thank You!
			""";
	
	final String PASSWORD_RESET_TEXTBODY = """
			A request to reset your password
			Hi, $firstName!
			Someone has requested to reset your password with our project. If it were not you, please ignore,
			otherwise please open the link in you broswer to set a new passoword: 
			http://localhost:8001/verification-service/password-reset.html?token=$tokenValue'
			Click this link to Reset Password
			Thank You!
			""";
	
	public void verifyEmail(UserDto userDto) {
		
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
				.standard()
				.withRegion(Regions.US_EAST_2)
				.build();
		
		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
				
		
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(userDto.getEmail()))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		
		client.sendEmail(request);
		
		System.out.println("Email Sent");
	}
	
	public boolean sendPasswordResetRequest(String firstName, String email, String token) {
		
		boolean returnValue = false;
		
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
				.withRegion(Regions.US_EAST_2).build();
		
		String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
		htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);
		
		String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
		textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);
		
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(email))
				.withMessage(new Message()
						.withBody(new Body()
								.withHtml(new Content()
										.withCharset("UTF-8")
										.withData(htmlBodyWithToken))
								.withText(new Content()
										.withCharset("UTF-8")
										.withData(textBodyWithToken))
								)
						.withSubject(new Content()
								.withCharset("UTF-8")
								.withData(PASSWORD_RESET_SUBJECT)))
						.withSource(FROM);
		
		SendEmailResult result = client.sendEmail(request);
		
		if (result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty())) {
			returnValue = true;
		}
		
		return returnValue;
	}
}
