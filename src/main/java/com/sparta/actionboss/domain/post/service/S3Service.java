package com.sparta.actionboss.domain.post.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.sparta.actionboss.global.exception.S3Exception;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String s3Bucket;


    // 파일을 S3에 업로드
    public List<String> upload(
            List<MultipartFile> multipartFiles,
            String dirName
    ) throws IOException {
        List<String> fileNames = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            // MultipartFile -> File 변환
            File uploadFile = convert(multipartFile).orElseThrow(
                    () -> new S3Exception(ClientErrorCode.S3_CONVERT_FAILURE));
            String fileName = uploadFile.getName();
            upload(uploadFile, dirName);
            fileNames.add(fileName);
        }
        return fileNames;
    }

    // Overloading
    private String upload(File uploadFile, String dirName) {
        String s3FileName = dirName + "/" + uploadFile.getName();
        String uploadImageURL = putS3(uploadFile, s3FileName);

        boolean deleteSuccessful = uploadFile.delete(); // 임시로 저장된 이미지 삭제
        if (!deleteSuccessful) {
            throw new S3Exception(ClientErrorCode.S3_TEMP_IMAGE_DELETE_FAILURE);
        }
        return uploadImageURL;
    }

    // 실제로 S3로 업로드
    private String putS3(File uploadFile, String s3FileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(s3Bucket, s3FileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(s3Bucket, s3FileName).toString();
    }

    public Optional<File> convert(MultipartFile multipartFile) throws IOException {

        String originalFileName = multipartFile.getOriginalFilename();

        if (originalFileName == null) {
            log.error("originalFileName == null");
            return Optional.empty();
        }
        String fileExtension = originalFileName
                .substring(originalFileName.lastIndexOf("."));

        String fileName = File.separator + originalFileName
                .substring(0, originalFileName.lastIndexOf(".")) + "-";
        // 유니크한 파일명 -> createTempFile 중복방지: 자체적으로 난수 생성
        File convertFile = File.createTempFile(fileName, fileExtension);

        if (convertFile.exists()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
                // fileOutputStream 데이터 -> 바이트 스트림으로 저장
                fileOutputStream.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        throw new S3Exception(ClientErrorCode.S3_CONVERT_FAILURE);
    }

    public void deleteFolder(String requestFolderName) {
        ObjectListing objectListing = amazonS3Client.listObjects(s3Bucket, "images/" + requestFolderName);
        List<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            keyVersions.add(new DeleteObjectsRequest.KeyVersion(objectSummary.getKey()));
        }
        if (!keyVersions.isEmpty()) {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(s3Bucket).withKeys(keyVersions);
            amazonS3Client.deleteObjects(deleteObjectsRequest);
        }
    }
}
