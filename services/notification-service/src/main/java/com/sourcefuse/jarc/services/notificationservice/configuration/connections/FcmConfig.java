package com.sourcefuse.jarc.services.notificationservice.configuration.connections;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types.FcmConnectionConfig;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
  value = "notification.provider.push",
  havingValue = "FcmProvider"
)
public class FcmConfig implements FcmConnectionConfig {

  public final FirebaseApp firebaseApp;

  private FcmConfig() throws IOException {
    FileInputStream serviceAccount;
    try {
      serviceAccount =
        new FileInputStream(
          "/Users/harshad.kadam/Downloads/chat-app-9e4df-firebase-adminsdk-al53m-005d933815.json"
        );
      FirebaseOptions options;

      options =
        FirebaseOptions
          .builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      this.firebaseApp = FirebaseApp.initializeApp(options);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public FirebaseApp getFirebaseApp() {
    return firebaseApp;
  }

}
