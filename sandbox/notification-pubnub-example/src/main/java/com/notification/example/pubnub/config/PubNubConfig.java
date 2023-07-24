package com.notification.example.pubnub.config;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubConnectionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PubNubConfig implements PubNubConnectionConfig {

  private final PubNub pubNub;

  @Value("${pubnub.apns2.env:}")
  String pubnunApns2Env;

  @Value("${pubnub.apns2.bundle-id:}")
  String pubnunApns2BundleId;

  public PubNubConfig(
    @Value("${pubnub.publish-key}") String publishKey,
    @Value("${pubnub.subscribe-key}") String subscribeKey,
    @Value("${pubnub.secrete-key}") String secreteKey,
    @Value("${pubnub.app-uuid}") String appUuid
  ) {
    PNConfiguration pnConfiguration = new PNConfiguration();
    pnConfiguration.setPublishKey(publishKey);
    pnConfiguration.setSubscribeKey(subscribeKey);
    pnConfiguration.setUuid(appUuid);
    pnConfiguration.setSecretKey(secreteKey);
    this.pubNub = new PubNub(pnConfiguration);
  }

  @Override
  public PubNub getPubNub() {
    return pubNub;
  }

  @Override
  public String getPubNubApns2Env() {
    return this.pubnunApns2Env;
  }

  @Override
  public String getPubNubApns2BundleId() {
    return this.pubnunApns2BundleId;
  }
}
