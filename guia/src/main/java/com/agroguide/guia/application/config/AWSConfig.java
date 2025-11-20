package com.agroguide.guia.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AWSConfig {

    @Bean
    public SqsClient sqsClient (){
        return SqsClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        System.getenv("accessKeyId_aws"),
                                        System.getenv("secretAccessKey_aws")
                                )
                        )
                )
                .build();
    }

}
