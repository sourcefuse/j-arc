package com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types;

public interface TwilioConnectionConfig {

	public void initTwilio();

	public String getSmsFrom();

	public String getWhatsappFrom();
	
	public String getSmsStatusCallback();
	
	public String getWhatsappStatusCallback();
}
