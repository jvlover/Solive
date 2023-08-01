package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ApplyDeletePutReq;
import com.ssafy.solive.api.request.ApplyRegistPostReq;
import com.ssafy.solive.db.entity.Apply;
import com.ssafy.solive.db.entity.Question;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.ApplyRepository;
import com.ssafy.solive.db.repository.QuestionRepository;
import com.ssafy.solive.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 *  강사가 학생이 등록한 문제에 지원할 때 필요한 API 서비스
 */

@Slf4j
@Transactional
@Service
public class ApplyServiceImpl implements ApplyService {

    // Spring Data Jpa 사용을 위한 Repository들
    ApplyRepository applyRepository;
    UserRepository userRepository;
    QuestionRepository questionRepository;

    @Autowired
    public ApplyServiceImpl(ApplyRepository applyRepository, UserRepository userRepository,
        QuestionRepository questionRepository) {
        this.applyRepository = applyRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    /*
     *  Regist API에 대한 서비스
     */
    @Override
    public void registApply(ApplyRegistPostReq registInfo) {
        /*
         *  registInfo : 문제 지원 신청할 때에 입력한 정보
         */

        log.info("ApplyService_registApply_start: " + registInfo.toString());

        /*
         *  registInfo를 바탕으로 Apply Entity 생성 시작
         */
        // TODO: IllegalArgumentException을 BaseException을 상속 받는 Custom Exception으로 변경 필요
        User user = userRepository.findById(registInfo.getTeacherId())
            .orElseThrow(IllegalArgumentException::new);
        Question question = questionRepository.findById(registInfo.getQuestionId())
            .orElseThrow(IllegalArgumentException::new);

        Integer estimatedTime = registInfo.getEstimatedTime();
        Integer solvePoint = registInfo.getSolvePoint();

        Apply apply = Apply.builder()
            .user(user)
            .question(question)
            .estimatedTime(estimatedTime)
            .solvePoint(solvePoint)
            .build();

        applyRepository.save(apply);
        /*
         *  생성한 Apply Entity를 DB에 insert 완료
         */
        // TODO: JpaRepository의 save에서 Exception이 발생할 경우가 있는지 확인 필요

        log.info("ApplyService_registApply_end: success");
    }

    /*
     *  delete API에 대한 서비스
     */
    @Override
    public boolean deleteApply(ApplyDeletePutReq deleteInfo) {
        /*
         *  deleteInfo : 신청 취소하기 위해 필요한 정보
         */
        // TODO: IllegalArg Exception 을 적절한 Custom Exception으로 대체

        log.info("ApplyService_deleteApply_start: " + deleteInfo.toString());

        Apply apply = applyRepository.findById(deleteInfo.getApplyId())
            .orElseThrow(IllegalArgumentException::new);

        // deleteInfo의 강사 정보와 해당 지원 신청의 실제 유저 정보가 같아야만 삭제
        if (apply.getUser().getId().equals(deleteInfo.getTeacherId())) {
            apply.deleteApply();
            log.info("ApplyService_deleteApply_end: true");
            return true;
        }
        // deleteInfo의 강사 정보와 해당 지원 신청의 실제 유저 정보가 다를 경우
        log.info("ApplyService_deleteApply_end: false");
        return false;
    }
}
