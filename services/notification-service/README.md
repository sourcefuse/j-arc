# @jArc/notification-service

## Overview

Microservice for handling notifications to users through real time notifications, email, or SMS. 

### Usage
- Add the `notification-service` in dependencies (in `pom.xml`).
  ```xml
    <dependencies>
        ...
        <dependency>
            <groupId>com.sourcefuse.jarc.services</groupId>
	        <artifactId>notification-service</artifactId>
	        <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
  ```
- Add `notification-service` pacakge in component scan in your application
 ```java
    package com.example;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.ComponentScan;

    @SpringBootApplication
    @ComponentScan(
    {
        "com.example",
        "com.sourcefuse.jarc.services.notificationservice"
    }
    )
    public class ExampleApplication {

        public static void main(String[] args) {
            SpringApplication.run(ExampleApplication.class, args);
        }

    }
 ```
- Using this service you can send Email, SMS and push notifications. Steps to add any of these are described below. You may choose to add one or more of these depending on your requirement.

  - **Email Notifications with Amazon SES** -

    - Either implement an SES Provider Component by refering [this](https://github.com/sourcefuse/j-arc/tree/master/services/notification-service/src/main/java/com/sourcefuse/jarc/services/notificationservice/providers/email/ses) or you can use the same SES provider provided by us.

    - To Use the SES provider which is provided by us, follow below steps
        - Add the following property (in `src/main/resources/application.properties`).
        ```properties
        notification.provider.email= SesProvider
        ```
        - After adding above property, create a new component class by implementing a `MailConnectionConfig` class. refer below example
        ```java
        @Component
        public class SesMailerConfig implements MailConnectionConfig {

            @Value("${cloud.aws.credentials.access-key}")
            private String accessKey;

            @Value("${cloud.aws.credentials.secret-key}")
            private String secretKey;

            @Value("${cloud.aws.region.static}")
            private String region;

            @Value("${notification.config.sender-mail:#{null}}")
            private String senderMail;

            @Value("${notification.config.send-to-multiple-receivers:#{false}}")
            private Boolean sendToMultipleReceivers;

            private final JavaMailSender javaMailSender;

            SesMailerConfig() {
                AwsBasicCredentials credentials = AwsBasicCredentials.create(
                accessKey,
                secretKey
                );

                SesClient client = SesClient
                .builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
                try {
                javaMailSender = new SimpleEmailServiceJavaMailSender(client);
                } catch (Exception e) {
                e.printStackTrace();
                throw e;
                }
            }

            @Override
            public JavaMailSender getJavaMailSender() {
                return javaMailSender;
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

        ```

  - **Email Notifications with Javamailer** -

    - Either implement an Javamailer Provider Component by refering [this](https://github.com/sourcefuse/j-arc/tree/master/services/notification-service/src/main/java/com/sourcefuse/jarc/services/notificationservice/providers/email/javamailer) or you can use the same Javamailer provider provided by us.

    - To Use the Javamailer provider which is provided by us, follow below steps
        - Add the following property (in `src/main/resources/application.properties`).
        ```properties
        notification.provider.email= JavaMailerProvider
        ```
        - After adding above property, create a new component class by implementing a `MailConnectionConfig` class. refer below example
        ```java
        @Component
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
        ```


  - **SMS Notification with Amazon SNS** -
    
    - Either implement an SNS Provider Component by refering [this](https://github.com/sourcefuse/j-arc/tree/master/services/notification-service/src/main/java/com/sourcefuse/jarc/services/notificationservice/providers/sms/sns) or you can use the same SNS provider provided by us.

    - To Use the SNS provider which is provided by us, follow below steps
        - Add the following property (in `src/main/resources/application.properties`).
        ```properties
        notification.provider.sms= SnsProvider
        ```
        - After adding above property, create a new component class by implementing a `SnsConnectionConfig` class. refer below example
        ```java

        @Component
        public class SnsConfig implements SnsConnectionConfig {

            @Value("${cloud.aws.credentials.access-key}")
            private String accessKey;

            @Value("${cloud.aws.credentials.secret-key}")
            private String secretKey;

            @Value("${cloud.aws.region.static}")
            private String region;

            private final SnsClient snsClient;

            SnsConfig() {
                AwsBasicCredentials credentials = AwsBasicCredentials.create(
                accessKey,
                secretKey
                );

                snsClient =
                SnsClient
                    .builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
            }

            @Override
            public SnsClient getSnsClient() {
                return snsClient;
            }
        }

       
        ```

  - **SMS Notification with Twilio** -
    
    - Either implement an Twilio Provider Component by refering [this](https://github.com/sourcefuse/j-arc/tree/master/services/notification-service/src/main/java/com/sourcefuse/jarc/services/notificationservice/providers/sms/twilio) or you can use the same Twilio provider provided by us.

    - To Use the Twilio provider which is provided by us, follow below steps
        - Add the following property (in `src/main/resources/application.properties`).
        ```properties
        notification.provider.sms= TwilioProvider
        ```
        - After adding above property, create a new component class by implementing a `TwilioConnectionConfig` class. refer below example
        ```java
        @Component
        public class TwilioConfig implements TwilioConnectionConfig {

            @Value("${twilio.sms-from}")
            String smsFrom;

            @Value("${twilio.whatsapp-from}")
            String whatsappFrom;

            private TwilioConfig(@Value("${twilio.account-sid}") String accountSid,
                    @Value("${twilio.auth-token}") String authToken) {
                try {
                    Twilio.init(accountSid, authToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String getSmsFrom() {
                return this.smsFrom;
            }

            @Override
            public String getWhatsappFrom() {
                return this.whatsappFrom;
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
        ```

  - **Push Notification with FCM** -
    
    - Either implement an FCM Provider Component by refering [this](https://github.com/sourcefuse/j-arc/tree/master/services/notification-service/src/main/java/com/sourcefuse/jarc/services/notificationservice/providers/push/fcm) or you can use the same FCM provider provided by us.

    - To Use the FCM provider which is provided by us, follow below steps
        - Add the following property (in `src/main/resources/application.properties`).
        ```properties
        notification.provider.push= FcmProvider
        ```
        - After adding above property, create a new component class by implementing a `FcmConnectionConfig` class. refer below example
        ```java

        @Component
        public class FcmConfig implements FcmConnectionConfig {

            public final FirebaseApp firebaseApp;

            private FcmConfig() throws IOException {
                FileInputStream serviceAccount;
                try {
                    serviceAccount = new FileInputStream(
                            "<location of your private key>");
                    FirebaseOptions options;

                    options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

                    this.firebaseApp = FirebaseApp.initializeApp(options);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }

            @Override
            public FirebaseApp getFirebaseApp() {
                return firebaseApp;
            }
        }

        ```
    - To get private key follow this steps
        - In the Firebase console, open Settings > Service Accounts.
        - Click Generate New Private Key, then confirm by clicking Generate Key.
        - Download and Securely store the JSON file containing the key.

  - **Push Notifications with Pubnub** -
    - Either implement an Pubnub Provider Component by refering [this](https://github.com/sourcefuse/j-arc/tree/master/services/notification-service/src/main/java/com/sourcefuse/jarc/services/notificationservice/providers/push/pubnub) or you can use the same Pubnub provider provided by us.

    - To Use the Pubnub provider which is provided by us, follow below steps
        - Add the following property (in `src/main/resources/application.properties`).
        ```properties
        notification.provider.push= PubNubProvider
        ```
        - After adding above property, create a new component class by implementing a `PubNubConnectionConfig` class. refer below example
        ```java
        @Component
        public class PubNubConfig implements PubNubConnectionConfig {

            private final PubNub pubNub;

            @Value("${pubnub.apns2.env:}")
            String pubnunApns2Env;

            @Value("${pubnub.apns2.bundle-id:}")
            String pubnunApns2BundleId;

            private PubNubConfig(
                @Value("${pubnub.publish-key}") String publishKey,
                @Value("${pubnub.subscribe-key}") String subscribeKey
            ) {
                PNConfiguration pnConfiguration = new PNConfiguration();
                pnConfiguration.setPublishKey(publishKey);
                pnConfiguration.setSubscribeKey(subscribeKey);
                pnConfiguration.setUuid("chat-app");
                pnConfiguration.setSecretKey(
                "sec-c-NTY3ZjQzNDUtNzVkOC00MTljLTgyM2ItYWMyNmI3YzA0YTEx"
                );
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

        ```
  - **Push Notifications with Socket.io** -
    
    - Either implement an Socket.io Provider Component by refering [this](https://github.com/sourcefuse/j-arc/tree/master/services/notification-service/src/main/java/com/sourcefuse/jarc/services/notificationservice/providers/push/socketio) or you can use the same Socket.io provider provided by us.

    - To Use the Socket.io provider which is provided by us, follow below steps
        - Add the following property (in `src/main/resources/application.properties`).
        ```properties
        notification.provider.push= SocketIoProvider
        ```
        - After adding above property, create a new component class by implementing a `SocketIoConnectionConfig` class. refer below example
        ```java
        @Component
        public class SocketIoConfig implements SocketIoConnectionConfig {

            Socket socket;

            @Value("${socket.default-path:#{null}}")
            String defaultPath;

            private SocketIoConfig() {
                try {
                // Configure and connect to the Socket.IO server
                IO.Options options = new IO.Options();
                options.path = "/";

                // add token if your socket requires auth
                    Map<String, String> auth = new HashMap<>();
                    auth.put(
                        "token",
                        "<enter your auth token here>" 
                    );
                    options.auth = auth;
                //
                String serverUrl = "http://127.0.0.1:3009"; // Replace with your actual Socket.IO server URL
                socket = IO.socket(serverUrl, options);
                socket.connect();
                if (!socket.connected()) {
                    throw new RuntimeException(
                    "Failed to initialize Socket.IO client connection"
                    );
                }
                } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to initialize Socket.IO client: " + e.getMessage()
                );
                }
            }

            @Override
            public Socket getSocket() {
                if (socket == null || !socket.connected()) {
                throw new ResponseStatusException(
                    HttpStatus.PRECONDITION_FAILED,
                    "Lost the socket connection"
                );
                }
                return socket;
            }

            @Override
            public String getDefaultPath() {
                return this.defaultPath;
            }
        }

        ```
- Start the application

### Application Properties
Do not forget to set Application properties. The examples below show a common configuration for a PostgreSQL Database running locally. (in `src/main/resources/application.properties`).
```properties
  spring.datasource.username=pg_service_user
  spring.datasource.password=pg_service_user_password
  spring.datasource.url=jdbc:postgresql://localhost:5432/notification_db
  spring.jpa.show-sql= false

  spring.jpa.hibernate.ddl-auto= update
  spring.jpa.properties.hibernat.dialect= org.hibernate.dialect.PostgreSQLDialect
  spring.liquibase.enabled= false

  spring.data.redis.host= localhost
  spring.data.redis.port= 6379
  spring.data.redis.password=

  notification.provider.email= 
  notification.provider.push=
  notification.provider.sms=
```
| Name          | Required | Default Value | Description                                                                                                                        |
| ------------- | -------- | ------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| spring.datasource.username | Y |  | Login username of the database. |
| spring.datasource.password | Y |  | Login password of the database. |
| spring.datasource.url | Y |  | JDBC URL of the database. |
| spring.jpa.show-sql | N | false | Whether to enable logging of SQL statements. |
| spring.jpa.hibernate.ddl-auto | N |  | DDL mode |
| spring.jpa.properties.hibernat.dialect | Y |  | Dialect in Hibernate class. |
| spring.liquibase.enabled | N | true | Whether to enable Liquibase support. |
| spring.data.redis.host | N | localhost | Redis server host. |
| spring.data.redis.port | N | 6379 |  Redis server port. |
| spring.data.redis.password | Y |  |  Redis server host. |
| notification.provider.email | N |  | Email Notification provider provided by us. values can be SesProvider, JavaMailerProvider. |
| notification.provider.push | N |  | Push Notification provider provided by us. values can be FcmProvider, PubNubProvider, SocketIoProvider. |
| notification.provider.sms | N |  | SMS Notification provider provided by us. values can be SnsProvider, TwilioProvider. |
| app.jwt-secret | Y |  | JWT Secrete should be in Base64 Format |
| app-jwt-expiration-milliseconds | Y |  | JWT token expiration in milliseconds |
| swagger.auth.username | Y |  | Username for accessing swagger URL's |
| swagger.auth.password | Y |  | Password for accessing swagger URL's |

### Create Notification Payload Structures

#### Email Notification with SES

```json
 {
  "receiver": {
    "to": [{
      "id": "string", // Email address
      "name": "string", // Optional
    },
    //... 
    ]
  },
  "subject": "Subject Of Notification",
  "body": "Notification Body",
  "sentDate": "10/10/2023",
  "type": "EMAIL",
  // options are optional
  "options": {
    "fromEmail": "string" // We do not support attachments with SES Provider yet.
  }
}
```

#### Email Notification with Nodemailer

```json
{
  "receiver": {
    "to": [{
      "id": "string", // Email address
      "name": "string",// Optional
    },
    //...
    ]
  },
  "subject": "Subject Of Notification",
  "body": "Notification Body",
  "sentDate": "10/10/2023",
  "type": "EMAIL",
  // options are optional
  "options": {
    "from": "string", // Email address
    "subject": "string",
    "text": "string",
    "html": "string",
    // We do not support attachments with Java Mailer Provider yet.
  }
}
```

#### SMS Notification with SNS

```json
{
  "receiver": {
    "to": [{
      "id": "string", // TopicArn or PhoneNumber
      "name": "string",// Optional
    },
    //...
    ]
  },
  "subject": "Subject Of Notification",
  "body": "Notification Body",
  "sentDate": "10/10/2023",
  "type": "SMS",
  // options are optional
  "options": {
    "smsType": "string"
  }
}
```

#### SMS Notification with Twilio

```json
{
  "receiver": {
    "to": [{
      "id": "string", // PhoneNumber
      "name": "string",// Optional,
      // type can be either TEXT_SMS_USER or WHATSAPP_USER,
      // if none matches then default value for type is  TEXT_SMS_USER
      "type": "string" 
    },
    //...
    ]
  },
  "subject": "Subject Of Notification",
  "body": "Notification Body",
  "sentDate": "10/10/2023",
  "type": "SMS",
  // options are optional
  "options": {
    "mediaUrl": [
        "https://some.url" ,
        //...
    ]
  }
}
```

#### Push Notification with FCM
- for Android config (refer [this](https://firebase.google.com/docs/reference/admin/dotnet/class/firebase-admin/messaging/android-config))
- for Webpush config (refer [this](https://firebase.google.com/docs/reference/admin/dotnet/class/firebase-admin/messaging/webpush-config))
- for APNS config (refer [this](https://firebase.google.com/docs/reference/admin/dotnet/class/firebase-admin/messaging/apns-config))
- for Fcm Options (refer [this](https://firebase.google.com/docs/reference/admin/dotnet/class/firebase-admin/messaging/fcm-options))
```json
{
  "receiver": {
    "to": [{
      "id": "string", // registration token 
      "name": "string",// Optional,
      // Optional, type can be REGISTRATION_TOKEN, FCM_TOPIC, FCM_CONDITION
      // if not provided default value is REGISTRATION_TOKEN
      // and if provided but does not match above mentioned value 
      // then it will not send notification to this receiver
      "type": "string" 
    },
    //...
    ]
  },
  "subject": "Subject Of Notification",
  "body": "Notification Body",
  "sentDate": "10/10/2023",
  "type": "PUSH",
  // options are optional
  "options": {
    "imageUrl": "https://some.url",
    "dryRun":  false, // boolean value
    "android": {}, // Android config,  all keys should be in snake case
    "webpush": {}, // Webpush config ,  all keys should be in snake case
    "apns": {}, // APNS config ,  all keys should be in snake case
    "fcmOptions": {}, // fcm options,  all keys should be in snake case
  }
}

```

#### Push Notification with Pubunb

```json
{
  "receiver": {
    "to": [{
      "id": "string", // channel identifier
      "name": "string",// Optional,
      "type": "CHANNEL" 
    },
    //...
    ]
  },
  "subject": "Subject Of Notification",
  "body": "Notification Body",
  "sentDate": "10/10/2023",
  "type": "PUSH",
  // options are optional
  "options": {
    "sound": "string"
  }
}
```

#### Push Notification with Socket.io

```json
{
  "receiver": {
    "to": [{
      "id": "string", // channel identifier
      "name": "string",// Optional,
      "type": "CHANNEL" 
    },
    //...
    ]
  },
  "subject": "Subject Of Notification",
  "body": "Notification Body",
  "sentDate": "10/10/2023",
  "type": "PUSH",
  // options are optional
  "options": {
    "path": "string"
  }
}
```

### Blacklisting Users

Here is a sample implementation of how we can blacklist a user(s).

Create a new component by implementing the `INotificationFilterFunc` class:

```java
@Service
public class NotificationFilterService implements INotificationFilterFunc {

  List<String> blacklistedUsers = Arrays.asList(
    "black-listed-user-one",
    "black-listed-user-two",
    // ...
  );

  @Override
  public Notification filter(Notification notification) {
    List<Subscriber> receivers = notification
      .getReceiver()
      .getTo()
      .stream()
      .filter((Subscriber subscriber) ->
        !blacklistedUsers.contains(subscriber.getId())
      )
      .toList();
    notification.getReceiver().setTo(receivers);
    return notification;
  }
}

```

Note: One can modify the provider according to the requirements

### API Documentation

#### Common Headers

Authorization: Bearer <token> where <token> is a JWT token signed using JWT issuer and secret.
`Content-Type: application/json` in the response and in request if the API method is NOT GET

#### Common Request path Parameters

{version}: Defines the API Version

### Common Responses

| Status | Description                                          |
| ------ | ---------------------------------------------------- |
| 200    | Successful Response. Response body varies w.r.t API  |
| 401    | Unauthorized: The JWT token is missing or invalid    |
| 403    | Forbidden : Not allowed to execute the concerned API |
| 404    | Entity Not Found                                     |
| 400    | Bad Request (Error message varies w.r.t API)         |
| 201    | No content: Empty Response                           |

## API's Details

Visit the [OpenAPI spec docs](./openapi.md)
