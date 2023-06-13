package com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types;

public interface TwilioConnectionConfig {
  void initTwilio();

  String getSmsFrom();

  String getWhatsappFrom();

  String getSmsStatusCallback();

  String getWhatsappStatusCallback();
}
