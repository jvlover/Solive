package com.ssafy.solive.common.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.solive.common.exception.NoDataException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    @Value("${cloud.aws.cloudfront.domain}")
    public String CLOUD_FRONT_DOMAIN_NAME;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    /**
     * @param multipartFile 업로드할 파일
     * @param fileName      파일 위치 이름
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile multipartFile, String fileName) throws IOException {
        log.info("S3Uploader_upload_start(MultipartFile): " + fileName + " - " + multipartFile);

        File uploadFile = convert(multipartFile)
            .orElseThrow(NoDataException::new);

        log.info("S3Uploader_upload_end(MultipartFile): " + uploadFile);
        return upload(uploadFile, fileName);
    }

    private String upload(File uploadFile, String fileName) {
        log.info("S3Uploader_upload_start(File): " + fileName + " - " + uploadFile);

//        SimpleDateFormat date = new SimpleDateFormat("yyyymmddHHmmss");
//        String orgName = uploadFile.getName();
//        if (orgName.length() > 30) {
//            orgName = orgName.substring(0, 30);
//        }
//        String fileName = dirName + "/" + date.format(new Date()) + "-" + orgName;

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        log.info("S3Uploader_upload_end(File): " + uploadFile);
        return fileName;
    }

    public void delete(String currentFilePath) {
        if ("".equals(currentFilePath) == false && currentFilePath != null) {
            boolean isExistObject = amazonS3Client.doesObjectExist(bucket, currentFilePath);

            if (isExistObject == true) {
                amazonS3Client.deleteObject(bucket, currentFilePath);
            }
        }
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
            CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        targetFile.delete();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        log.info("convert: " + file);
        File convertFile = new File(file.getOriginalFilename());
        log.info("convertFile: " + convertFile);

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
                log.info("convertFile output stream");
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}