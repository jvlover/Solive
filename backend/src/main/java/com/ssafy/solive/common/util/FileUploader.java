package com.ssafy.solive.common.util;

import com.ssafy.solive.common.exception.ImageUploadFailException;
import com.ssafy.solive.common.model.FileDto;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class FileUploader {

    private final S3Uploader s3Uploader;

    @Autowired
    public FileUploader(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    public List<FileDto> fileUpload(List<MultipartFile> fileList, String subFolderName) {

        // TODO: "C:/solive/image/" -> S3 주소로 변경

        String s3UploadPath = "C:/solive/image/" + subFolderName;
        List<FileDto> fileDtoList = new ArrayList<FileDto>();

        for (MultipartFile file : fileList) {
            try {
                String originalName = file.getOriginalFilename();
                // 파일 확장자 명
                String extension = originalName.substring( // 파일명에 .은 반드시 있으니 예외처리 X
                    originalName.lastIndexOf(".") + 1);

                // 랜덤한 파일 이름 생성
                String fileName = UUID.randomUUID() + "." + extension;

                // 파일 업로드 경로 디렉토리가 만약 존재하지 않으면 생성
                Files.createDirectories(Paths.get(s3UploadPath));

                // 파일 절대 경로
                String path = s3UploadPath + fileName;

                fileDtoList.add(FileDto.builder()
                    .fileName(fileName)
                    .originalName(file.getOriginalFilename())
                    .path(path)
                    .contentType(file.getContentType())
                    .build());

                s3Uploader.upload(file, fileName);
            } catch (IllegalStateException | IOException e) {
                throw new ImageUploadFailException(); // 이미지 등록 실패 시 Exception
            }
        }

        return fileDtoList;
    }
}
