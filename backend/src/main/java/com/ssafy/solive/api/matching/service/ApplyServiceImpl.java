package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.ApplyDeletePutReq;
import com.ssafy.solive.api.matching.request.ApplyFindGetReq;
import com.ssafy.solive.api.matching.request.ApplyRegistPostReq;
import com.ssafy.solive.api.matching.response.ApplyFindRes;
import com.ssafy.solive.api.matching.response.ApplyRegistPostRes;
import com.ssafy.solive.common.exception.NoDataException;
import com.ssafy.solive.db.entity.Apply;
import com.ssafy.solive.db.entity.Question;
import com.ssafy.solive.db.entity.Teacher;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.ApplyRepository;
import com.ssafy.solive.db.repository.QuestionRepository;
import com.ssafy.solive.db.repository.TeacherRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 강사가 학생이 등록한 문제에 지원할 때 필요한 API 서비스
 */
@Slf4j
@Transactional
@Service
public class ApplyServiceImpl implements ApplyService {

    // Spring Data Jpa 사용을 위한 Repositories
    private final ApplyRepository applyRepository;
    private final TeacherRepository teacherRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public ApplyServiceImpl(ApplyRepository applyRepository, TeacherRepository teacherRepository,
        QuestionRepository questionRepository) {
        this.applyRepository = applyRepository;
        this.teacherRepository = teacherRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * 강사가 학생이 등록한 문제에 대해서 풀이 지원 요청 Regist API에 대한 서비스
     *
     * @param registInfo : 문제 지원 신청할 때에 입력한 정보
     * @return user : 요청을 받는 학생의 정보(알림 때문에 필요함)
     */
    @Override
    public ApplyRegistPostRes registApply(ApplyRegistPostReq registInfo) {

        log.info("ApplyService_registApply_start: " + registInfo.toString());

        // registInfo를 바탕으로 Apply Entity 생성 시작
        Teacher teacher = teacherRepository.findById(registInfo.getTeacherId())
            .orElseThrow(NoDataException::new);
        Question question = questionRepository.findById(registInfo.getQuestionId())
            .orElseThrow(NoDataException::new);

        Integer estimatedTime = registInfo.getEstimatedTime();
        Integer solvePoint = registInfo.getSolvePoint();

        Apply apply = Apply.builder()
            .teacher(teacher)
            .question(question)
            .estimatedTime(estimatedTime)
            .solvePoint(solvePoint)
            .build();

        applyRepository.save(apply);

        // 해당 question의 matching_state를 0에서 1로 바꿔야 함
        question.modifyMatchingState(1);

        // 요청을 받는 학생에게 알림을 전송하기 위해 요청 받게 되는 학생의 정보 return
        User user = question.getStudent();

        // 컨트롤러에게 보낼 리스폰스
        ApplyRegistPostRes applyRegistPostRes = ApplyRegistPostRes.builder()
            .user(user)
            .questionTitle(question.getTitle())
            .build();

        // 생성한 Apply Entity를 DB에 insert 완료
        log.info("ApplyService_registApply_end: " + applyRegistPostRes.toString());
        return applyRegistPostRes;
    }

    /**
     * Apply delete(지원 취소) API에 대한 서비스
     *
     * @param deleteInfo : 요청 취소하기 위해 필요한 정보
     */
    @Override
    public boolean deleteApply(ApplyDeletePutReq deleteInfo) {

        log.info("ApplyService_deleteApply_start: " + deleteInfo.toString());

        Apply apply = applyRepository.findById(deleteInfo.getApplyId())
            .orElseThrow(NoDataException::new);

        // deleteInfo의 강사 정보와 해당 지원 신청의 실제 유저 정보가 같아야만 삭제
        if (apply.getTeacher().getId().equals(deleteInfo.getTeacherId())) {
            apply.deleteApply();
            log.info("ApplyService_deleteApply_end: true");
            return true;
        }
        // deleteInfo의 강사 정보와 해당 지원 신청의 실제 유저 정보가 다를 경우
        log.info("ApplyService_deleteApply_end: false");
        return false;
    }

    /**
     * 유저(학생)가 자신이 등록한 문제에 어떤 강사들이 지원 신청했는지 검색하기 위한 API 서비스
     *
     * @param findCondition : 검색 조건. 정렬/검색 기준 : 예상 풀이시간순, 가격순, 평점순 정렬, 강사의 선호 과목과 문제의 과목 일치 여부 선택
     * @return findConditionRes : 강사명, 강사 프로필 사진, 강사의 선호 과목, 강사가 달아 놓은 SP, 예측 시간, 강사 평점
     */
    @Override
    public List<ApplyFindRes> findByCondition(
        ApplyFindGetReq findCondition) {

        log.info("ApplyService_findByCondition_start: " + findCondition.toString());

        List<ApplyFindRes> findConditionRes = applyRepository.findByCondition(
            findCondition);

        if (findConditionRes.size() == 0) {
            log.info("ApplyService_findByCondition_end: No Result");
        } else {
            log.info("ApplyService_findByCondition_end: " + findConditionRes);
        }
        return findConditionRes;
    }
}
