package com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public interface FcmConnectionConfig {
  FirebaseApp getFirebaseApp();

  default FirebaseMessaging getFirebaseMessaging() {
    return FirebaseMessaging.getInstance(getFirebaseApp());
  }
}
