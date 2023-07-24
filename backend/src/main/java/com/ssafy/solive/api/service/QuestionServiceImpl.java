package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.common.exception.ImageUploadFailException;
import com.ssafy.solive.db.entity.Question;
import com.ssafy.solive.db.entity.QuestionPicture;
import com.ssafy.solive.db.repository.QuestionPictureRepository;
import com.ssafy.solive.db.repository.QuestionRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class QuestionServiceImpl implements QuestionService {

    QuestionRepository questionRepository;
    QuestionPictureRepository questionPictureRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository,
        QuestionPictureRepository questionPictureRepository) {
        this.questionRepository = questionRepository;
        this.questionPictureRepository = questionPictureRepository;
    }

    @Override
    public Question inputQuestion(QuestionRegistPostReq questionRegistPostReq,
        List<MultipartFile> files) {
        Question question = questionRepository.save(questionRegistPostReq.toQuestion());
        String uploadFilePath = "C:/solive/image/";
        for (MultipartFile file : files) {
            String prefix = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1,
                    file.getOriginalFilename().length());
            String fileName = UUID.randomUUID().toString() + "." + prefix;
            File folder = new File(uploadFilePath);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }
            String pathName = uploadFilePath + fileName;
            String resourcePathName = "/image/" + fileName;
            File dest = new File(pathName);
            try {
                file.transferTo(dest);
                QuestionPicture questionPicture = QuestionPicture.builder()
                    .question(question)
                    .contentType(file.getContentType())
                    .imageName(file.getOriginalFilename())
                    .fileName(fileName)
                    .pathName(pathName)
                    .size((int) file.getSize())
                    .url(resourcePathName)
                    .build();
                questionPictureRepository.save(questionPicture);
            } catch (IllegalStateException | IOException e) {
                throw new ImageUploadFailException();
            }
        }
        return question;
    }
}
