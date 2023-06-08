package com.sourcefuse.jarc.core.enums;

public enum NotificationError {
	PROVIDER_NOT_FOUND("Provider not found"),
	SENDER_NOT_FOUND("Message sender not found in request"),
	MESSAGE_FROM_NOT_FOUND("Message From not found in request"),
	MESSAGE_TITLE_NOT_FOUND("Message title not found"),
	RECEIVERS_EXCEEDS_500("Message receiver count cannot exceed 500"),
	RECEIVERS_NOT_FOUND("Message receiver not found in request"),
	MESSAGE_DATA_NOT_FOUND( "Message data incomplete"),
	CHANNEL_INFO_MISSING("Channel info is missing"),
	MESSAGE_RECEIVER_OR_TOPIC_OR_CONDITION_NOT_FOUND("Message receiver, topic or condition not found in request");

	private final String name;

	NotificationError(String value) {
		this.name = value;
	}

	public String value() {
		return this.name;
	}

	@Override
	public String toString() {
		return name;
	}
}
