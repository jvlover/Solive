package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.QuestionDeletePutReq;
import com.ssafy.solive.api.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.request.QuestionFindMineGetReq;
import com.ssafy.solive.api.request.QuestionModifyPutReq;
import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.api.response.QuestionFindConditionRes;
import com.ssafy.solive.api.response.QuestionFindDetailRes;
import com.ssafy.solive.api.response.QuestionFindMineRes;
import com.ssafy.solive.common.exception.ImageUploadFailException;
import com.ssafy.solive.common.exception.NoImageException;
import com.ssafy.solive.common.exception.QuestionNotFoundException;
import com.ssafy.solive.db.entity.MasterCode;
import com.ssafy.solive.db.entity.Question;
import com.ssafy.solive.db.entity.QuestionPicture;
import com.ssafy.solive.db.entity.Student;
import com.ssafy.solive.db.repository.MasterCodeRepository;
import com.ssafy.solive.db.repository.QuestionPictureRepository;
import com.ssafy.solive.db.repository.QuestionRepository;
import com.ssafy.solive.db.repository.StudentRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/*
 *  학생과 강사가 문제를 Matching 할 때 API에 대한 서비스
 */

@Slf4j
@Transactional
@Service
public class QuestionServiceImpl implements QuestionService {

    // Spring Data Jpa 사용을 위한 Repository들
    QuestionRepository questionRepository;
    QuestionPictureRepository questionPictureRepository;
    StudentRepository studentRepository;
    MasterCodeRepository masterCodeRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository,
        QuestionPictureRepository questionPictureRepository, StudentRepository studentRepository,
        MasterCodeRepository masterCodeRepository) {
        this.questionRepository = questionRepository;
        this.questionPictureRepository = questionPictureRepository;
        this.studentRepository = studentRepository;
        this.masterCodeRepository = masterCodeRepository;
    }

    /*
     *  Regist API에 대한 서비스
     */
    @Override
    public void registQuestion(QuestionRegistPostReq registInfo,
        List<MultipartFile> files) {
        /*
         *  files : 문제 사진. 문제는 반드시 사진이 하나 이상 있어야 하므로 null일 수 없음
         *  registInfo : 문제 등록할 때 입력한 정보
         */

        log.info("QuestionService_registQuestion_start: " + registInfo.toString() + ", "
            + files.toString());

        // 문제는 반드시 이미지가 필요하므로, 이미지가 없으면 NoImageException 처리
        if (files.get(0).getSize() == 0) {
            throw new NoImageException();
        }

        // 전달 받은 마스터코드 분류 더해서 최종 마스터코드 번호 얻기
        Integer masterCodeId =
            1000 + registInfo.getSubject() + registInfo.getSubSubject() + registInfo.getDetail();

        /*
         *  registInfo를 바탕으로 Question Entity 생성 시작
         */
        // TODO: IllegalArgumentException을 BaseException을 상속 받는 Custom Exception으로 변경 필요
        Student student = studentRepository.findById(registInfo.getStudentId())
            .orElseThrow(IllegalArgumentException::new);
        MasterCode masterCode = masterCodeRepository.findById(masterCodeId)
            .orElseThrow(IllegalArgumentException::new);
        String title = registInfo.getTitle();
        String description = registInfo.getDescription();

        Question question = Question.builder()
            .student(student)
            .masterCode(masterCode)
            .title(title)
            .description(description)
            .build();

        questionRepository.save(question);
        /*
         *  생성한 Question Entity DB에 insert 완료
         */
        // TODO: JpaRepository의 save에서 Exception이 발생할 경우가 있는지 확인 필요

        /*
         *  files를 바탕으로 QuestionPicture Entity 생성 시작
         */
        // TODO: 파일 업로드 관련 DB에 추가한 여러 컬럼들 나중에 전부 필요한 지 확인 필요

        // 파일 업로드 경로
        String uploadFilePath = "C:/solive/image/";

        for (MultipartFile file : files) {
            // 파일 확장자 명
            String suffix = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1,
                    file.getOriginalFilename().length());
            // 랜덤한 파일 이름 생성
            String fileName = UUID.randomUUID().toString() + "." + suffix;

            // 파일 업로드 경로 디렉토리가 만약 존재하지 않으면 생성
            File folder = new File(uploadFilePath);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }

            String pathName = uploadFilePath + fileName;    // 파일 절대 경로
            String resourcePathName = "/image/" + fileName; // url
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
                throw new ImageUploadFailException(); // 이미지 등록 실패 시 Exception
            }
            /*
             *  생성한 Question Picture Entity를 DB에 insert 완료
             */
        }

        log.info("QuestionService_registQuestion_end: success");
    }

    /*
     *  delete API에 대한 서비스
     */
    @Override
    public boolean deleteQuestion(QuestionDeletePutReq deleteInfo) {
        /*
         *  deleteInfo : 문제 삭제하기 위해 필요한 정보
         */
        // TODO: IllegalArg Exception 을 적절한 Custom Exception으로 대체

        log.info("QuestionService_deleteQuestion_start: " + deleteInfo.toString());

        Question question = questionRepository.findById(deleteInfo.getQuestionId())
            .orElseThrow(IllegalArgumentException::new);

        // deleteInfo의 유저 정보와 해당 문제의 실제 유저 정보가 같아야만 삭제
        if (question.getStudent().getId().equals(deleteInfo.getStudentId())) {
            question.deleteQuestion();
            log.info("QuestionService_deleteQuestion_end: true");
            return true;
        }
        // deleteInfo의 유저 정보와 해당 문제의 실제 유저 정보가 다를 경우
        log.info("QuestionService_deleteQuestion_end: false");
        return false;
    }

    /*
     *  modify API에 대한 서비스
     */
    @Override
    public boolean modifyQuestion(QuestionModifyPutReq modifyInfo) {
        /*
         *  modifyInfo : 문제 삭제하기 위해 필요한 정보
         */
        // TODO: IllegalArg Exception 을 적절한 Custom Exception으로 대체

        log.info("QuestionService_modifyQuestion_start: " + modifyInfo.toString());

        Question question = questionRepository.findById(modifyInfo.getQuestionId())
            .orElseThrow(IllegalArgumentException::new);
        MasterCode masterCode = masterCodeRepository.findById(modifyInfo.getMasterCodeId())
            .orElseThrow(IllegalArgumentException::new);

        String title = modifyInfo.getTitle();
        String description = modifyInfo.getDescription();

        // modifyInfo의 유저 정보와 해당 문제의 실제 유저 정보가 같아야만 삭제
        if (question.getStudent().getId().equals(modifyInfo.getStudentId())) {
            question.modifyQuestion(masterCode, title, description);
            log.info("QuestionService_modifyQuestion_end: true");
            return true;
        }
        // modifyInfo의 유저 정보와 해당 문제의 실제 유저 정보가 다를 경우
        log.info("QuestionService_modifyQuestion_end: false");
        return false;
    }

    /*
     *  유저(강사)가 문제를 검색하기 위한 API 서비스
     *  제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<QuestionFindConditionRes> findByCondition(
        QuestionFindConditionGetReq findCondition) {
        /*
         *  findCondition : 검색 조건
         */

        log.info("QuestionService_findByCondition_start: " + findCondition.toString());

        // 문제 리스트 DB에서 얻어 오기
        List<QuestionFindConditionRes> findConditionRes = questionRepository.findByCondition(
            findCondition);

        // 문제 리스트의 각 문제에 썸네일 이미지 Setting
        for (int i = 0; i < findConditionRes.size(); i++) {
            String questionImage = questionRepository.findQuestionImage(
                findConditionRes.get(i).getQuestionId());
            findConditionRes.get(i).setImagePathName(questionImage);
        }

        log.info("QuestionService_findByCondition_end: " + findConditionRes.toString());

        return findConditionRes;
    }

    /*
     *  유저가 문제의 상세 정보를 확인하기 위한 API
     */
    @Override
    public QuestionFindDetailRes findDetail(Long id) {
        /*
         *  id : question의 id
         */

        log.info("QuestionService_findDetail_start: " + id);

        // detail 정보 DB로부터 얻어오기
        QuestionFindDetailRes findDetailRes = questionRepository.findDetail(id);
        // 상세 정보 검색 결과가 null이면 NotFoundException 처리
        if (findDetailRes == null) {
            log.info("QuestionService_findDetail_end: QuestionNotFoundException");
            throw new QuestionNotFoundException();
        }
        // 해당 문제의 Images 얻어오기
        List<String> questionImages = questionRepository.findQuestionImages(id);
        // Response에 Images Setting
        findDetailRes.setImagePathName(questionImages);

        log.info("QuestionService_findDetail_end: " + findDetailRes.toString());
        return findDetailRes;
    }

    /*
     *  유저(학생)가 자신이 등록했던 문제를 검색하기 위한 API
     *  매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<QuestionFindMineRes> findMyQuestion(
        QuestionFindMineGetReq findCondition) {
        /*
         *  findCondition : 검색 조건
         */

        log.info("QuestionService_findMyQuestion_start: " + findCondition.toString());

        // 문제 리스트 DB에서 얻어 오기
        List<QuestionFindMineRes> findConditionRes = questionRepository.findMyQuestion(
            findCondition);

        // 문제 리스트의 각 문제에 썸네일 이미지 Setting
        for (int i = 0; i < findConditionRes.size(); i++) {
            String questionImage = questionRepository.findQuestionImage(
                findConditionRes.get(i).getQuestionId());
            findConditionRes.get(i).setImagePathName(questionImage);
        }

        log.info("QuestionService_findMyQuestion_end: " + findConditionRes.toString());

        return findConditionRes;
    }

}
