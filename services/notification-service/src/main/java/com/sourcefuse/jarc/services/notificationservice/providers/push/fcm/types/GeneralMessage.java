package com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.FcmOptions;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralMessage {
	Notification notification;
	AndroidConfig android;
	WebpushConfig webpush;
	ApnsConfig apns;
	FcmOptions fcmOptions;
}