package com.agroguide.guia.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AWSConfig {
    //@Value Sirve para leer valores desde application.properties
    @Value("${aws.access-key}")
    //Lee las creedencias AWS desde aplication propierties
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;
    //Bean sqsClient()
    @Bean
    public SqsClient sqsClient (){
        return SqsClient.builder()
                .region(Region.US_EAST_2)
                //Clabes inyectadas por medio de credenciales AWS
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey
                                )
                        )
                )
                .build();
    }

}
