package com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types;

import com.pubnub.api.PubNub;

public interface PubNubConnectionConfig {
  PubNub getPubNub();

  String getPubNubApns2Env();

  String getPubNubApns2BundleId();
}
