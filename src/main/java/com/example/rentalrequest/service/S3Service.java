package com.example.rentalrequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.accessKeyId:}")
    private String accessKeyId;

    @Value("${aws.secretKey:}")
    private String secretKey;

    public String uploadFile(MultipartFile file) throws IOException {
        S3Client s3 = buildS3Client();

        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }

    private S3Client buildS3Client() {
        // If running on AWS (EC2, ECS, Lambda), use IAM roles (recommended)
        if (accessKeyId.isEmpty() || secretKey.isEmpty()) {
            return S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        }
        // Otherwise use provided credentials (for local development)
        else {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
            return S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();
        }
    }
}

