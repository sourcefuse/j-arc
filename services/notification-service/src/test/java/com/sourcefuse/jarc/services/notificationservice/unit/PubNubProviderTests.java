package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.sourcefuse.jarc.services.notificationservice.types.Config;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

  Config notificationAccess;

  @BeforeEach
  void setup() throws PubNubException {
    MockitoAnnotations.openMocks(this);

    Mockito.when(pubNubConnectionConfig.getPubNub()).thenReturn(pubnub);
    Mockito
      .when(pubNubConnectionConfig.getPubNubApns2BundleId())
      .thenReturn(null);
    Mockito.when(pubNubConnectionConfig.getPubNubApns2Env()).thenReturn(null);
    this.setUpPublishMock();
    this.setUpGrantMock();

    message = MockNotifications.getApnsNotificationObj();
    notificationAccess = MockNotifications.getNotificationAccess();
  }

  private void setUpGrantMock() throws PubNubException {
    Mockito.when(pubnub.grant()).thenReturn(grant);
    Mockito.when(grant.authKeys(Mockito.anyList())).thenReturn(grant);
    Mockito.when(grant.channels(Mockito.anyList())).thenReturn(grant);
    Mockito.when(grant.read(Mockito.anyBoolean())).thenReturn(grant);
    Mockito.when(grant.write(Mockito.anyBoolean())).thenReturn(grant);
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
   *
   * @throws PubNubException
   */
  @Test
  void testPublish_SendsNotificationToReceivers() throws PubNubException {
    pubnubProvider.publish(message);

    Mockito.verify(pubnub, Mockito.times(2)).publish();
    Mockito.verify(publish, Mockito.times(2)).sync();
  }

  /**
   * Fail to execute due to receivers are empty
   */
  @Test
  void testPublish_FailDuetoEmptyReceivers() {
    message.getReceiver().setTo(Arrays.asList());

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> pubnubProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVERS_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * Fail to grants access due to authorization token not exists in config options
   */
  @Test
  void testPublish_FailDueAuthorizationTokenNotExists() {
    notificationAccess.getOptions().remove("token");

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> pubnubProvider.grantAccess(notificationAccess)
    );
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.ATHORIZATION_TOKEN_OR_TTL_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * Fail to grants access due to ttl not exists in config options
   */
  @Test
  void testGrantAccess_FailDuetoTtlNotFound() {
    notificationAccess.getOptions().remove("ttl");

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> pubnubProvider.grantAccess(notificationAccess)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.ATHORIZATION_TOKEN_OR_TTL_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * successfully grants access to single all receiver
   *
   * @throws PubNubException
   */
  @Test
  void testGrantAccess_GrantsAccessToUsers() throws PubNubException {
    pubnubProvider.grantAccess(notificationAccess);

    Mockito.verify(pubnub, Mockito.times(1)).grant();
    Mockito.verify(grant, Mockito.times(1)).sync();
  }

  /**
   * Fail to revoke access due to authorization token not exists in config options
   */
  @Test
  void testRevokeAccess_FailDuetoTokenNotFound() {
    notificationAccess.getOptions().remove("token");

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> pubnubProvider.revokeAccess(notificationAccess)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.ATHORIZATION_TOKEN_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * successfully revoke access to single all receiver
   *
   * @throws PubNubException
   */
  @Test
  void testRevokeAccess_RevokesAccessOfUsers() throws PubNubException {
    pubnubProvider.revokeAccess(notificationAccess);

    Mockito.verify(pubnub, Mockito.times(1)).grant();
    Mockito.verify(grant, Mockito.times(1)).sync();
  }
}
