package com.sparta.actionboss.domain.post.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String s3Bucket;


    // 파일을 S3에 업로드
    // UUID.randomUUID() : 중복 방지
    public List<String> upload(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        List<String> uploaderFileURLs = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            // MultipartFile -> File 변환
            File uploadFile = convert(multipartFile).orElseThrow(
                    () -> new IllegalArgumentException("파일 전환에 실패했습니다."));
            String uploadedImageURL = upload(uploadFile, dirName);
            uploaderFileURLs.add(uploadedImageURL);
        }
        return uploaderFileURLs;
    }

    // Overloading
    private String upload(File uploadFile, String dirName) {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + uploadFile.getName();
        String s3FileName = dirName + "/" + uniqueFileName;
        String uploadImageURL = putS3(uploadFile, s3FileName);

        boolean deleteSuccessful = uploadFile.delete(); // 로컬에 저장된 이미지 삭제
        if (!deleteSuccessful) {
            log.error("로컬에 저장된 이미지 삭제 실패");
        }
        return uploadImageURL;
    }

    // 실제로 S3로 업로드
    private String putS3(File uploadFile, String s3FileName) {
        amazonS3Client.putObject(new PutObjectRequest(s3Bucket, s3FileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(s3Bucket, s3FileName).toString();
    }

    public Optional<File> convert(MultipartFile multipartFile) throws IOException {
        String homeDirectory = System.getProperty("user.dir");
        String targetDirectory = homeDirectory + "/src/main/resources/images/";
        File directory = new File(targetDirectory);

        if (!directory.exists()) {
            directory.mkdirs();
        }
        // 유니크한 파일명
        String uniqueFileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

        File convertFile = new File(targetDirectory + uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) { // fileOutputStream 데이터 -> 바이트 스트림으로 저장
                fileOutputStream.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        throw new IOException("파일 전환에 실패했습니다: " + multipartFile.getOriginalFilename() + " (경로: " + convertFile.getAbsolutePath() + ")");
    }

    public String getRequestFolderNameFromImageUrl(String imageUrl) {
        try {
            URL pasedUrl = new URL(imageUrl);
            String path = pasedUrl.getPath();
            String dir = path.substring(0, path.lastIndexOf("/") + 1).substring(8);
            return dir;
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
        return null;
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
