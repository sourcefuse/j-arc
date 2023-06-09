package com.sourcefuse.jarc.services.notificationservice.configuration.connections;

import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types.SnsConnectionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@ConditionalOnProperty(
  value = "notification.provider.sms",
  havingValue = "SNSProvider"
)
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
