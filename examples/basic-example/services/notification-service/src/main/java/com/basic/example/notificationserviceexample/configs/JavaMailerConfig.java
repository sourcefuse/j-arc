package com.basic.example.notificationserviceexample.configs;

import com.sourcefuse.jarc.services.notificationservice.providers.email.types.MailConnectionConfig;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class JavaMailerConfig implements MailConnectionConfig {

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.port}")
  private int port;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Value("${notification.config.sender-mail:#{null}}")
  private String senderMail;

  @Value("${notification.config.send-to-multiple-receivers:#{false}}")
  private Boolean sendToMultipleReceivers;

  public JavaMailerConfig() {
    //  for sonar
  }

  private JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(port);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

    mailSender.setJavaMailProperties(properties);
    return mailSender;
  }

  @Override
  public JavaMailSender getJavaMailSender() {
    return javaMailSender();
  }

  @Override
  public String getSenderMail() {
    return senderMail;
  }

  @Override
  public boolean shouldSendToMultipleReceivers() {
    return sendToMultipleReceivers;
  }
}
