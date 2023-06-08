package com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types;

import com.pubnub.api.PubNub;

public interface PubNubConnectionConfig {

	public PubNub getPubNub();

	public String getPubNubApns2Env();

	public String getPubNubApns2BundleId();
}
