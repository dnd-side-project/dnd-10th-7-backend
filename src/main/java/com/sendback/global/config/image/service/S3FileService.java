package com.sendback.global.config.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sendback.global.config.image.S3SaveDir;
import com.sendback.global.exception.type.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.sendback.global.config.image.exception.ImageExceptionType.AWS_S3_DELETE_FAIL;
import static com.sendback.global.config.image.exception.ImageExceptionType.AWS_S3_UPLOAD_FAIL;

@Service
@RequiredArgsConstructor
public class S3FileService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile uploadFile, String s3UploadFilePath, S3SaveDir s3SaveDir) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(uploadFile.getSize());
        metadata.setContentType(uploadFile.getContentType());
        String bucketName = bucket + s3SaveDir.path;
        try (InputStream inputStream = uploadFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, s3UploadFilePath,inputStream,metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AmazonServiceException e) {
            throw new ImageException(AWS_S3_UPLOAD_FAIL);
        }

        return amazonS3Client.getUrl(bucketName, s3UploadFilePath).toString();
    }

    public void delete(String s3DeleteFilePath, S3SaveDir s3SaveDir) {
        String bucketName = bucket + s3SaveDir.path;

        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, s3DeleteFilePath));
        } catch (AmazonServiceException e) {
            throw new ImageException(AWS_S3_DELETE_FAIL);
        }
    }
}