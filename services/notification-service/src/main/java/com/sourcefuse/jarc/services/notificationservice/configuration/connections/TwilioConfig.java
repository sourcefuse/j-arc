package com.sourcefuse.jarc.services.notificationservice.configuration.connections;

import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioConnectionConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
  value = "notification.provider.sms",
  havingValue = "TwilioProvider"
)
public class TwilioConfig implements TwilioConnectionConfig {

  @Value("${twilio.accountSid}")
  private String accountSid;

  @Value("${twilio.authToken}")
  private String authToken;

  @Value("${twilio.sms-from}")
  private String smsFrom;

  @Value("${twilio.whatsapp-from}")
  private String whatsappFrom;

  private TwilioConfig() {
    this.initTwilio();
  }

  @Override
  public void initTwilio() {
    try {
      Twilio.init(accountSid, authToken);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getSmsFrom() {
    return smsFrom;
  }

  @Override
  public String getWhatsappFrom() {
    return whatsappFrom;
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
