package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.QuestionDeletePutReq;
import com.ssafy.solive.api.matching.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.matching.request.QuestionModifyPutReq;
import com.ssafy.solive.api.matching.request.QuestionRegistPostReq;
import com.ssafy.solive.api.matching.response.QuestionFindDetailRes;
import com.ssafy.solive.api.matching.response.QuestionFindRes;
import com.ssafy.solive.common.exception.NoDataException;
import com.ssafy.solive.common.exception.matching.QuestionNoImageException;
import com.ssafy.solive.common.exception.matching.QuestionNotFoundException;
import com.ssafy.solive.common.model.FileDto;
import com.ssafy.solive.common.util.FileUploader;
import com.ssafy.solive.db.entity.MasterCode;
import com.ssafy.solive.db.entity.Question;
import com.ssafy.solive.db.entity.QuestionPicture;
import com.ssafy.solive.db.entity.Student;
import com.ssafy.solive.db.entity.Teacher;
import com.ssafy.solive.db.repository.MasterCodeRepository;
import com.ssafy.solive.db.repository.QuestionPictureRepository;
import com.ssafy.solive.db.repository.QuestionRepository;
import com.ssafy.solive.db.repository.StudentRepository;
import com.ssafy.solive.db.repository.TeacherRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 학생이 풀이를 원하는 문제를 등록할 때 API에 대한 서비스
 */
@Slf4j
@Transactional
@Service
public class QuestionServiceImpl implements QuestionService {

    // Spring Data Jpa 사용을 위한 Repositories
    private final QuestionRepository questionRepository;
    private final QuestionPictureRepository questionPictureRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MasterCodeRepository masterCodeRepository;
    private final FileUploader fileUploader;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository,
        QuestionPictureRepository questionPictureRepository, StudentRepository studentRepository,
        TeacherRepository teacherRepository, MasterCodeRepository masterCodeRepository,
        FileUploader fileUploader) {
        this.questionRepository = questionRepository;
        this.questionPictureRepository = questionPictureRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.masterCodeRepository = masterCodeRepository;
        this.fileUploader = fileUploader;
    }

    /**
     * 문제 regist API에 대한 서비스
     *
     * @param registInfo : 문제 등록할 때 입력한 정보
     * @param fileList   : 문제 사진. 문제는 반드시 사진이 하나 이상 있어야 하므로 Null 불가능
     */
    @Override
    public void registQuestion(QuestionRegistPostReq registInfo,
        List<MultipartFile> fileList) {

        log.info(
            "QuestionService_registQuestion_start: " + registInfo.toString()
                + ", " + fileList.toString());

        // 문제는 반드시 이미지가 필요하므로, 이미지가 없으면 QuestionNoImageException 처리
        if (fileList.get(0).getSize() == 0) {
            throw new QuestionNoImageException();
        }

        // 전달 받은 마스터코드 분류 더해서 최종 마스터코드 번호 얻기
        Integer masterCodeId =
            1000 + registInfo.getSubject() + registInfo.getSubSubject() + registInfo.getDetail();

        /*
         *  registInfo를 바탕으로 Question Entity 생성 시작
         */
        Student student = studentRepository.findById(registInfo.getStudentId())
            .orElseThrow(NoDataException::new);
        MasterCode masterCode = masterCodeRepository.findById(masterCodeId)
            .orElseThrow(NoDataException::new);
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

        /*
         *  files를 바탕으로 QuestionPicture Entity 생성 시작
         */
        List<FileDto> fileDtoList = fileUploader.fileUpload(fileList, "question");

        for (FileDto fileDto : fileDtoList) {
            QuestionPicture questionPicture = QuestionPicture.builder()
                .question(question)
                .fileName(fileDto.getFileName())
                .originalName(fileDto.getOriginalName())
                .path(fileDto.getPath())
                .contentType(fileDto.getContentType())
                .build();
            questionPictureRepository.save(questionPicture);
        }
        /*
         *  files를 바탕으로 QuestionPicture Entity DB에 insert 완료
         */

        // 문제를 등록함과 동시에 학생의 질문한 문제 수 증가
        student.incrementQuestionCount();

        log.info("QuestionService_registQuestion_end: success");
    }

    /**
     * 문제 delete API에 대한 서비스
     *
     * @param deleteInfo : 문제 삭제하기 위해 필요한 정보
     */
    @Override
    public boolean deleteQuestion(QuestionDeletePutReq deleteInfo) {

        log.info("QuestionService_deleteQuestion_start: " + deleteInfo.toString());

        Question question = questionRepository.findById(deleteInfo.getQuestionId())
            .orElseThrow(NoDataException::new);

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

    /**
     * 문제 modify API에 대한 서비스
     *
     * @param modifyInfo : 문제 수정하기 위해 필요한 정보
     */
    @Override
    public boolean modifyQuestion(QuestionModifyPutReq modifyInfo) {

        log.info("QuestionService_modifyQuestion_start: " + modifyInfo.toString());

        Question question = questionRepository.findById(modifyInfo.getQuestionId())
            .orElseThrow(NoDataException::new);
        MasterCode masterCode = masterCodeRepository.findById(modifyInfo.getMasterCodeId())
            .orElseThrow(NoDataException::new);

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

    /**
     * 유저(강사)가 문제를 검색하기 위한 API 서비스
     *
     * @param findCondition : 검색 조건. 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<QuestionFindRes> findByCondition(
        QuestionFindConditionGetReq findCondition) {

        log.info("QuestionService_findByCondition_start: " + findCondition.toString());

        // 문제 리스트 DB에서 얻어 오기
        List<QuestionFindRes> findConditionRes = questionRepository.findByCondition(
            findCondition);

        // 문제 리스트의 각 문제에 썸네일 이미지 Setting
        for (int i = 0; i < findConditionRes.size(); i++) {
            String questionImage = questionRepository.findQuestionImage(
                findConditionRes.get(i).getQuestionId());
            findConditionRes.get(i).setPath(questionImage);
        }

        if (findConditionRes.size() == 0) {
            log.info("QuestionService_findByCondition_end: No Result");
        } else {
            log.info("QuestionService_findByCondition_end: " + findConditionRes);
        }
        return findConditionRes;
    }

    /**
     * 유저가 문제의 상세 정보를 확인하기 위한 API
     *
     * @param id : question의 id
     */
    @Override
    public QuestionFindDetailRes findDetail(Long id) {

        log.info("QuestionService_findDetail_start: " + id);

        // detail 정보 DB로부터 얻어오기
        QuestionFindDetailRes findDetailRes = questionRepository.findDetail(id);
        // 상세 정보 검색 결과가 null이면 NotFoundException 처리
        if (findDetailRes == null) {
            throw new QuestionNotFoundException();
        }

        // 해당 문제의 Images 얻어오기
        List<String> questionImages = questionRepository.findQuestionImages(id);
        // Response에 Images Setting
        findDetailRes.setPath(questionImages);

        log.info("QuestionService_findDetail_end: " + findDetailRes);
        return findDetailRes;
    }

    /**
     * 강사가 접속 시 등록 최신 순으로 문제 12개 조회하기
     */
    @Override
    public List<QuestionFindRes> findLatestQuestionForTeacher() {

        log.info("QuestionService_findLatestQuestionForTeacher_start");

        // 문제 리스트 DB에서 얻어 오기
        List<QuestionFindRes> findRes = questionRepository.findLatestQuestionForTeacher();

        // 문제 리스트의 각 문제에 썸네일 이미지 Setting
        for (int i = 0; i < findRes.size(); i++) {
            String questionImage = questionRepository.findQuestionImage(
                findRes.get(i).getQuestionId());
            findRes.get(i).setPath(questionImage);
        }

        if (findRes.size() == 0) {
            log.info("QuestionService_findLatestQuestionForTeacher_end: No Result");
        } else {
            log.info("QuestionService_findLatestQuestionForTeacher_end: " + findRes);
        }
        return findRes;
    }

    /**
     * 강사가 접속 시 자신이 좋아하는 과목으로 설정한 것과 똑같은 과목의 문제 최신순으로 12개 조회하기
     *
     * @param userId : 강사의 user Id
     */
    @Override
    public List<QuestionFindRes> findFavoriteQuestionForTeacher(Long userId) {

        log.info("QuestionService_findFavoriteQuestionForTeacher_start: " + userId);

        Teacher teacher = teacherRepository.findById(userId)
            .orElseThrow(NoDataException::new);

        // 문제 리스트 DB에서 얻어 오기
        List<QuestionFindRes> findRes = questionRepository.findFavoriteQuestionForTeacher(
            teacher.getMasterCode().getId());

        // 문제 리스트의 각 문제에 썸네일 이미지 Setting
        for (int i = 0; i < findRes.size(); i++) {
            String questionImage = questionRepository.findQuestionImage(
                findRes.get(i).getQuestionId());
            findRes.get(i).setPath(questionImage);
        }

        if (findRes.size() == 0) {
            log.info("QuestionService_findFavoriteQuestionForTeacher_end: No Result");
        } else {
            log.info("QuestionService_findFavoriteQuestionForTeacher_end: " + findRes);
        }
        return findRes;
    }
}
