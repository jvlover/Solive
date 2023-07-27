package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.QuestionDeletePutReq;
import com.ssafy.solive.api.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.request.QuestionModifyPutReq;
import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.api.response.QuestionFindConditionRes;
import com.ssafy.solive.api.response.QuestionFindDetailRes;
import com.ssafy.solive.common.exception.ImageUploadFailException;
import com.ssafy.solive.common.exception.NoImageException;
import com.ssafy.solive.common.exception.QuestionNotFoundException;
import com.ssafy.solive.db.entity.MasterCode;
import com.ssafy.solive.db.entity.Question;
import com.ssafy.solive.db.entity.QuestionPicture;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.MasterCodeRepository;
import com.ssafy.solive.db.repository.QuestionPictureRepository;
import com.ssafy.solive.db.repository.QuestionRepository;
import com.ssafy.solive.db.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
public class MatchingServiceImpl implements MatchingService {

    QuestionRepository questionRepository;
    QuestionPictureRepository questionPictureRepository;
    UserRepository userRepository;
    MasterCodeRepository masterCodeRepository;

    @Autowired
    public MatchingServiceImpl(QuestionRepository questionRepository,
        QuestionPictureRepository questionPictureRepository, UserRepository userRepository,
        MasterCodeRepository masterCodeRepository) {
        this.questionRepository = questionRepository;
        this.questionPictureRepository = questionPictureRepository;
        this.userRepository = userRepository;
        this.masterCodeRepository = masterCodeRepository;
    }

    @Override
    public Question registQuestion(QuestionRegistPostReq registInfo,
        List<MultipartFile> files) {
        if (files.get(0).getSize() == 0) {
            throw new NoImageException();
        }
        // TODO: Studnt Entity 구현 후, Student 지위 체크 과정 필요한 지 확인
        User user = userRepository.findById(registInfo.getStudentId())
            .orElseThrow(IllegalArgumentException::new);
        MasterCode masterCode = masterCodeRepository.findById(registInfo.getMasterCodeId())
            .orElseThrow(IllegalArgumentException::new);
        String title = registInfo.getTitle();
        String description = registInfo.getDescription();

        Question question = Question.builder()
            .user(user)
            .masterCode(masterCode)
            .title(title)
            .description(description)
            .build();

        questionRepository.save(question);
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
        // TODO: IllegalArg Exception 을 적절한 Custom Exception으로 대체
        Question question = questionRepository.findById(deleteInfo.getQuestionId())
            .orElseThrow(IllegalArgumentException::new);
        if (question.getUser().getId().equals(deleteInfo.getStudentId())) {
            question.deleteQuestion();
            return true;
        }
        return false;
    }

    @Override
    public boolean modifyQuestion(QuestionModifyPutReq modifyInfo) {
        // TODO: IllegalArg Exception 을 적절한 Custom Exception으로 대체
        Question question = questionRepository.findById(modifyInfo.getQuestionId())
            .orElseThrow(IllegalArgumentException::new);
        MasterCode masterCode = masterCodeRepository.findById(modifyInfo.getMasterCodeId())
            .orElseThrow(IllegalArgumentException::new);
        String title = modifyInfo.getTitle();
        String description = modifyInfo.getDescription();

        if (question.getUser().getId().equals(modifyInfo.getStudentId())) {
            question.modifyQuestion(masterCode, title, description);
            return true;
        }
        return false;
    }

    @Override
    public List<QuestionFindConditionRes> findByCondition(
        QuestionFindConditionGetReq findCondition) {
        log.info("MatchingService_findByCondition_start: " + findCondition.toString());
        return questionRepository.findByCondition(findCondition);
    }

    @Override
    public QuestionFindDetailRes findDetail(Long id) {
        log.info("MatchingService_findDetail_start: " + Long.toString(id));
        QuestionFindDetailRes findDetailRes = questionRepository.findDetail(id);
        if (findDetailRes == null) {
            throw new QuestionNotFoundException();
        } else {
            return findDetailRes;
        }
    }
}
