package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.QuestionDeletePutReq;
import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.common.exception.ImageUploadFailException;
import com.ssafy.solive.common.exception.NoImageException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
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
    public Question registQuestion(QuestionRegistPostReq registInfo,
        List<MultipartFile> files) {
        if (files.get(0).getSize() == 0) {
            throw new NoImageException();
        }
        // TODO: Studnt Entity 구현 후, Req의 StudentId 에 대해 Illegal Arg 확인 필요
        Question question = questionRepository.save(registInfo.toQuestion());
        // TODO: JpaRepository의 save에서 Exception이 발생할 경우 어떻게 처리할 지 조사할 필요
        // TODO: 파일 업로드 관련 추가한 컬럼들 차후 상황에 따라 재고 필요
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

    @Override
    public boolean deleteQuestion(QuestionDeletePutReq deleteInfo) {
        Question question = questionRepository.findById(deleteInfo.getQuestionId())
            .orElseThrow(IllegalArgumentException::new);
        // TODO: IllegalArg Exception 을 적절한 Custom Exception으로 대체
        // TODO: Student Entity 만들어지면 인가 과정 작성 필요
        question.deleteQuestion();
        return true;
    }
}
