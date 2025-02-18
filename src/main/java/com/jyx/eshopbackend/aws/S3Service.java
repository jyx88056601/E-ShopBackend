package com.jyx.eshopbackend.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // Upload file to S3 bucket
    public String uploadFile(File file) {
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, "product/" + file.getName(), file));
        } catch (AmazonServiceException e) {
            // 处理来自AWS服务的异常
            return "AWS Error: " + e.getErrorMessage();
        } catch (AmazonClientException e) {
            // 处理客户端异常
            return "Client Error: " + e.getMessage();
        } catch (Exception e) {
            // 处理其他异常
            return "Error uploading file: " + e.getMessage();
        }
        return amazonS3.getUrl(this.bucketName, file.getName()).toString();
    }

    public String removeFile(String fileName) {
            try {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, "product/" +fileName));
            } catch (AmazonServiceException e) {
               return "Error deleting file from S3: " + e.getErrorMessage();
            } catch (Exception e) {
                return e.getMessage();
            }
        return "File deleted successfully from S3.";
    }

    // Download file from S3 bucket
    public S3Object downloadFile(String fileName) {
        return amazonS3.getObject(bucketName, fileName);
    }
}