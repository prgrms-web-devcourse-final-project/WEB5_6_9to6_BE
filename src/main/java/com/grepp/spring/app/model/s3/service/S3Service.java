package com.grepp.spring.app.model.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 기본 업로드 (디렉토리 없음)
     */
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return uploadFile(multipartFile, "");
    }

    /**
     * 디렉토리를 포함해 S3에 파일 업로드
     *
     * @param multipartFile 업로드할 파일
     * @param dirName 디렉토리 이름 (예: "reward-items")
     * @return 업로드된 파일의 S3 URL
     * @throws IOException InputStream 처리 중 예외 발생 시
     */
    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + (originalFileName != null ? originalFileName : "unknown");

        // 디렉토리가 있으면 경로 앞에 추가
        if (dirName != null && !dirName.isBlank()) {
            fileName = dirName + "/" + fileName;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
