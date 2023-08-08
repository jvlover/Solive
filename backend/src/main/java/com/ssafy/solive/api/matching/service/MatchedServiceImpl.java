package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.request.MatchedRegistPostReq;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import com.ssafy.solive.common.exception.NoDataException;
import com.ssafy.solive.db.entity.Apply;
import com.ssafy.solive.db.entity.Matched;
import com.ssafy.solive.db.entity.Question;
import com.ssafy.solive.db.entity.Student;
import com.ssafy.solive.db.entity.Teacher;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.ApplyRepository;
import com.ssafy.solive.db.repository.MatchedRepository;
import com.ssafy.solive.db.repository.QuestionRepository;
import com.ssafy.solive.db.repository.StudentRepository;
import com.ssafy.solive.db.repository.TeacherRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 학생이 강사가 지원한 요청들 중 하나를 수락한 후부터의 과정들 API 서비스
 */
@Slf4j
@Transactional
@Service
public class MatchedServiceImpl implements MatchedService {

    // Spring Data Jpa 사용을 위한 Repositories
    private final ApplyRepository applyRepository;
    private final TeacherRepository teacherRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final MatchedRepository matchedRepository;

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

    /**
     * 학생이 강사 요청 수락하는 Regist API에 대한 서비스
     *
     * @param registInfo : 어떤 학생이 어떤 apply를 수락했는가
     * @return teacher : 학생이 강사 요청을 수락했을 때, 알림을 받게 될 강사
     */
    @Override
    public User registMatched(MatchedRegistPostReq registInfo) {

        log.info("MatchedService_registMatched_start: " + registInfo.toString());

        // registInfo를 바탕으로 Matched Entity 생성 시작
        Apply apply = applyRepository.findById(registInfo.getApplyId())
            .orElseThrow(NoDataException::new);
        Student student = studentRepository.findById(registInfo.getStudentId())
            .orElseThrow(NoDataException::new);
        Teacher teacher = teacherRepository.findById(apply.getTeacher().getId())
            .orElseThrow(NoDataException::new);
        Question question = questionRepository.findById(apply.getQuestion().getId())
            .orElseThrow(NoDataException::new);

        Integer solvePoint = apply.getSolvePoint();

        Matched matched = Matched.builder()
            .teacher(teacher)
            .question(question)
            .student(student)
            .solvePoint(solvePoint)
            .build();

        matchedRepository.save(matched);

        // 매칭이 시작되었으므로 apply에서는 row 삭제
        apply.deleteApply();

        // 해당 question의 matching_state를 1에서 2로 바꿔야 함
        question.modifyMatchingState(2);

        // 생성한 Matched Entity를 DB에 insert 완료
        log.info("MatchedService_registMatched_end: " + teacher.toString());
        // 매칭이 성사되었다는 알림을 받을 강사
        return teacher;
    }

    /**
     * 유저가 자신이 등록했던 문제, 매칭 이력을 검색하기 위한 API
     *
     * @param findCondition : 검색 조건. 매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<MatchedFindMineRes> findMyMatching(
        MatchedFindMineGetReq findCondition) {

        log.info("MatchedService_findMyMatching_start: " + findCondition.toString());

        // 학생이 자신의 문제 이력 검색하려는 경우
        if (findCondition.getUserState() == 1) {
            // 문제 리스트 DB에서 얻어 오기
            List<MatchedFindMineRes> findConditionRes = questionRepository.findMyQuestion(
                findCondition);

            // 문제 리스트의 각 문제에 썸네일 이미지 Setting
            for (int i = 0; i < findConditionRes.size(); i++) {
                String questionImage = questionRepository.findQuestionImage(
                    findConditionRes.get(i).getQuestionId());
                findConditionRes.get(i).setImagePathName(questionImage);
            }

            if (findConditionRes.size() == 0) {
                log.info("MatchedService_findMyMatching_end: No Result");
            } else {
                log.info("MatchedService_findMyMatching_end: " + findConditionRes);
            }
            return findConditionRes;

        } else {    // 강사가 자신의 매칭 이력을 검색하려는 경우

            // teacherId를 통해 teacher의 apply 리스트 가져오기(아직 매칭 되기 전 상태인 것들)
            List<MatchedFindMineRes> findApplyList = applyRepository.findMyApply(findCondition);

            // teacherId를 통해 Matching 리스트 가져오기(매칭이 된 후의 문제들)
            List<MatchedFindMineRes> findMatchedList = matchedRepository.findMyMatching(
                findCondition);

            // 두 리스트 병합
            List<MatchedFindMineRes> findConditionRes = Stream.of(findApplyList, findMatchedList)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

            // 문제 리스트의 각 문제에 썸네일 이미지 Setting
            for (int i = 0; i < findConditionRes.size(); i++) {
                String questionImage = questionRepository.findQuestionImage(
                    findConditionRes.get(i).getQuestionId());
                findConditionRes.get(i).setImagePathName(questionImage);
            }

            if (findConditionRes.size() == 0) {
                log.info("MatchedService_findMyMatching_end: No Result");
            } else {
                log.info("MatchedService_findMyMatching_end: " + findConditionRes);
            }
            return findConditionRes;
        }
    }
}
