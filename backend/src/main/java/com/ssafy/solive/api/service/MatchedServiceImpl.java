package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.MatchedRegistPostReq;
import com.ssafy.solive.db.entity.Apply;
import com.ssafy.solive.db.entity.Matched;
import com.ssafy.solive.db.entity.Question;
import com.ssafy.solive.db.entity.Student;
import com.ssafy.solive.db.entity.Teacher;
import com.ssafy.solive.db.repository.ApplyRepository;
import com.ssafy.solive.db.repository.MatchedRepository;
import com.ssafy.solive.db.repository.QuestionRepository;
import com.ssafy.solive.db.repository.StudentRepository;
import com.ssafy.solive.db.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 *  학생이 강사가 지원한 요청들 중 하나를 수락한 후부터의 과정들 API 서비스
 */

@Slf4j
@Transactional
@Service
public class MatchedServiceImpl implements MatchedService {

    // Spring Data Jpa 사용을 위한 Repository들
    ApplyRepository applyRepository;
    TeacherRepository teacherRepository;
    QuestionRepository questionRepository;
    StudentRepository studentRepository;
    MatchedRepository matchedRepository;

    @Autowired
    public MatchedServiceImpl(ApplyRepository applyRepository, TeacherRepository teacherRepository,
        QuestionRepository questionRepository, StudentRepository studentRepository,
        MatchedRepository matchedRepository) {
        this.applyRepository = applyRepository;
        this.teacherRepository = teacherRepository;
        this.questionRepository = questionRepository;
        this.studentRepository = studentRepository;
        this.matchedRepository = matchedRepository;
    }

    /*
     *  학생이 강사 요청 수락하는 Regist API에 대한 서비스
     */
    @Override
    public void registMatched(MatchedRegistPostReq registInfo) {
        /*
         *  registInfo : 어떤 학생이 어떤 apply를 수락했는가
         */

        log.info("MatchedService_registMatched_start: " + registInfo.toString());

        /*
         *  registInfo를 바탕으로 Matched Entity 생성 시작
         */
        // TODO: IllegalArgumentException을 BaseException을 상속 받는 Custom Exception으로 변경 필요
        Apply apply = applyRepository.findById(registInfo.getApplyId())
            .orElseThrow(IllegalArgumentException::new);
        Student student = studentRepository.findById(registInfo.getStudentId())
            .orElseThrow(IllegalArgumentException::new);
        Teacher teacher = teacherRepository.findById(apply.getTeacher().getId())
            .orElseThrow(IllegalArgumentException::new);
        Question question = questionRepository.findById(apply.getQuestion().getId())
            .orElseThrow(IllegalArgumentException::new);

        Integer solvePoint = apply.getSolvePoint();

        Matched matched = Matched.builder()
            .teacher(teacher)
            .question(question)
            .student(student)
            .solvePoint(solvePoint)
            .build();

        matchedRepository.save(matched);
        /*
         *  생성한 Matched Entity를 DB에 insert 완료
         */
        // TODO: JpaRepository의 save에서 Exception이 발생할 경우가 있는지 확인 필요

        log.info("MatchedService_registMatched_end: success");
    }
}
