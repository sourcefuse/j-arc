package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.access.Grant;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.PubNubProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubConnectionConfig;

class PubNubProviderTests {
	@Mock
	private PubNubConnectionConfig pubNubConnectionConfig;

	@Mock
	private PubNub pubnub;

	@Mock
	Publish publish;

	@Mock
	Grant grant;

	@Mock
	PNPublishResult pNPublishResult;

	@Mock
	PNAccessManagerGrantResult pNAccessManagerGrantResult;

	@InjectMocks
	private PubNubProvider pubnubProvider;

	Notification message;

	@BeforeEach
	public void setup() throws PubNubException {
		MockitoAnnotations.openMocks(this);

		Mockito.when(pubNubConnectionConfig.getPubNub()).thenReturn(pubnub);
		Mockito.when(pubNubConnectionConfig.getPubNubApns2BundleId()).thenReturn(null);
		Mockito.when(pubNubConnectionConfig.getPubNubApns2Env()).thenReturn(null);
		this.setUpPublishMock();
		this.setUpGrantMock();

		message = MockNotifications.getApnsNotificationObj();
	}

	private void setUpGrantMock() throws PubNubException {
		Mockito.when(pubnub.grant()).thenReturn(grant);
		Mockito.when(grant.authKeys(Mockito.anyList())).thenReturn(grant);
		Mockito.when(grant.channels(Mockito.anyList())).thenReturn(grant);
		Mockito.when(grant.read(Mockito.anyBoolean())).thenReturn(grant);
		Mockito.when(grant.read(Mockito.anyBoolean())).thenReturn(grant);
		Mockito.when(grant.ttl(Mockito.anyInt())).thenReturn(grant);
		Mockito.when(grant.sync()).thenReturn(pNAccessManagerGrantResult);
	}

	private void setUpPublishMock() throws PubNubException {
		Mockito.when(pubnub.publish()).thenReturn(publish);
		Mockito.when(publish.message(Mockito.any())).thenReturn(publish);
		Mockito.when(publish.channel(Mockito.anyString())).thenReturn(publish);
		Mockito.when(publish.sync()).thenReturn(pNPublishResult);
	}

	/**
	 * successfully sends single push notification to all receiver
	 */
	@Test
	public void testPublish_SendsMailToReceivers() {
		pubnubProvider.publish(message);

		Mockito.verify(pubnub, Mockito.times(2)).publish();
	}

	/**
	 * Fail to execute due to receivers are empty
	 */
	@Test
	public void testPublish_FailDuetoEmptyReceivers() {
		message.getReceiver().setTo(Arrays.asList());

		HttpServerErrorException exception = assertThrows(HttpServerErrorException.class,
				() -> pubnubProvider.publish(message));

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
		assertEquals(NotificationError.RECEIVERS_NOT_FOUND.toString(), exception.getStatusText());
	}

}
