package com.sourcefuse.jarc.services.notificationservice.configuration.connections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubConnectionConfig;

@Service
@ConditionalOnProperty(value = "notification.provider.push", havingValue = "PubNubProvider")
public class PubNubConfig implements PubNubConnectionConfig {

	private final PubNub pubNub;

	@Value("${pubnub.apns2.env:}")
	String pubnunApns2Env;

	@Value("${pubnub.apns2.bundle-id:}")
	String pubnunApns2BundleId;

	private PubNubConfig(@Value("${pubnub.publish-key}") String publishKey,
			@Value("${pubnub.subscribe-key}") String subscribeKey) {
		PNConfiguration pnConfiguration = new PNConfiguration();
		pnConfiguration.setPublishKey(publishKey);
		pnConfiguration.setSubscribeKey(subscribeKey);
		pnConfiguration.setUuid("chat-app");
		pnConfiguration.setSecretKey("sec-c-NTY3ZjQzNDUtNzVkOC00MTljLTgyM2ItYWMyNmI3YzA0YTEx");
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
