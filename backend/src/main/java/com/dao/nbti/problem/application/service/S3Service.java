package com.dao.nbti.problem.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadProblemImage(MultipartFile file, int categoryId) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String key = String.format("problem/%d/%s/%s", categoryId, uuid, originalFilename);

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                            .build(),
                    RequestBody.fromInputStream(inputStream, inputStream.available())
            );
        }

        return getFileUrl(key);
    }

    private String getFileUrl(String key) {
        S3Utilities utilities = s3Client.utilities();
        return utilities.getUrl(builder -> builder.bucket(bucketName).key(key)).toString();
    }
}