package com.notification.example.twilio.config;

import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioConnectionConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioConfig implements TwilioConnectionConfig {

  @Value("${twilio.sms-from}")
  String smsFrom;

  @Value("${twilio.whatsapp-from}")
  String whatsappFrom;

  private TwilioConfig(
    @Value("${twilio.account-sid}") String accountSid,
    @Value("${twilio.auth-token}") String authToken
  ) {
    try {
      Twilio.init(accountSid, authToken);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getSmsFrom() {
    return this.smsFrom;
  }

  @Override
  public String getWhatsappFrom() {
    return this.whatsappFrom;
  }

  @Override
  public String getSmsStatusCallback() {
    return null;
  }

  @Override
  public String getWhatsappStatusCallback() {
    return null;
  }
}
