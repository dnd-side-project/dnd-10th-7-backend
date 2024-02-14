package com.sendback.global.config.image.service;

import com.sendback.global.config.image.FilePathUtils;
import com.sendback.global.config.image.S3SaveDir;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final S3FileService s3FileService;

    public List<String> upload(List<MultipartFile> files, String type) {

        return files.stream()
                .map(file -> uploadImage(file, type))
                .toList();
    }

    public String uploadImage(MultipartFile multipartFile, String type) {
        S3SaveDir s3SaveDir = S3SaveDir.toEnum(type);
        //파일 타입에 따라 업로드 파일 경로 만들기
        String s3UploadFilePath = FilePathUtils.createS3UploadFilePath(multipartFile);
        //s3 업로드
        return s3FileService.upload(multipartFile, s3UploadFilePath, s3SaveDir);
    }

    public List<String> updateImage(List<String> urlsToDelete,  List<MultipartFile> newFiles, String type) {

        deleteMultipleImages(type, urlsToDelete);

        return newFiles.stream()
                .map(file -> uploadImage(file, type))
                .toList();
    }

    public void deleteMultipleImages(String type, List<String> urlsToDelete) {
        S3SaveDir s3SaveDir = S3SaveDir.toEnum(type);
        urlsToDelete.forEach((imageUrl) -> {
            String filePath = FilePathUtils.parseFilePathFromUrl(imageUrl);
            s3FileService.delete(filePath, s3SaveDir);
        });
    }

}