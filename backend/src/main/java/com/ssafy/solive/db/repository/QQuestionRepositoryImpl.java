package com.ssafy.solive.db.repository;

import static com.ssafy.solive.db.entity.QMasterCode.masterCode;
import static com.ssafy.solive.db.entity.QQuestion.question;
import static com.ssafy.solive.db.entity.QQuestionPicture.questionPicture;
import static com.ssafy.solive.db.entity.QStudent.student;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import com.ssafy.solive.api.matching.response.QuestionFindDetailRes;
import com.ssafy.solive.api.matching.response.QuestionFindRes;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Querydsl을 위한 Question Repository 구현
 */
@Slf4j
@Repository
public class QQuestionRepositoryImpl implements QQuestionRepository {

    private final JPAQueryFactory queryFactory;

    public QQuestionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 유저(강사)가 문제를 검색하기 위한 Query
     *
     * @param findCondition : 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<QuestionFindRes> findByCondition(
        QuestionFindConditionGetReq findCondition) {

        log.info("QQuestionRepository_findByCondition_start: " + findCondition.toString());

        // masterCode 값 합치기
        int code = 1000 + findCondition.getMasterCodeMiddle() + findCondition.getMasterCodeLow();

        return queryFactory
            .select(Projections.constructor(QuestionFindRes.class,
                question.id.as("questionId"),
                student.nickname.as("userNickname"),
                question.title.as("title"),
                question.time.as("createTime"),
                masterCode.name.as("masterCodeName")))
            .from(question)
            .leftJoin(question.student, student).on(student.id.eq(question.student.id))
            .leftJoin(question.masterCode, masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(mastercodeBetween(code), keywordSearch(findCondition.getKeyword()),
                matchingStateLt())
            .orderBy(timeSort(findCondition.getSort()))
            .offset(findCondition.getPageNum() * 8)
            .limit(8)
            .fetch();
    }

    /**
     * 유저가 문제를 상세 조회하기 위한 Query
     *
     * @param id : 유저 Id
     */
    @Override
    public QuestionFindDetailRes findDetail(Long id) {

        log.info("QQuestionRepository_findDetail_start: " + id);

        return queryFactory
            .select(Projections.constructor(QuestionFindDetailRes.class,
                student.nickname.as("userNickname"),
                question.title.as("title"),
                question.description.as("description"),
                masterCode.name.as("masterCodeName"),
                question.time.as("createTime"),
                question.matchingState.as("state")))
            .from(question)
            .leftJoin(question.student, student).on(student.id.eq(question.student.id))
            .leftJoin(question.masterCode, masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(questionIdEq(id))
            .fetchOne();
    }

    /**
     * 유저(학생)가 자신이 등록했던 문제를 검색하기 위한 Query
     *
     * @param findCondition : 매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<MatchedFindMineRes> findMyQuestion(MatchedFindMineGetReq findCondition) {

        log.info("QQuestionRepository_findMyQuestion_start: " + findCondition.toString());

        // masterCode 값 합치기
        int code = 1000 + findCondition.getMasterCodeMiddle() + findCondition.getMasterCodeLow();

        return queryFactory
            .select(Projections.constructor(MatchedFindMineRes.class,
                question.id.as("questionId"),
                question.title.as("title"),
                question.time.as("createTime"),
                masterCode.name.as("masterCodeName"),
                question.matchingState.as("matchingState")))
            .from(question)
            .leftJoin(question.masterCode, masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(studentIdEq(findCondition.getUserId()), mastercodeBetween(code),
                keywordSearch(findCondition.getKeyword()),
                matchingStateEq(findCondition.getMatchingState()))
            .orderBy(timeSort(findCondition.getSort()))
            .offset(findCondition.getPageNum() * 9)
            .limit(9)
            .fetch();
    }

    /**
     * 문제 상세 정보 조회 시 해당 문제의 이미지들 조회
     *
     * @param questionId : 문제 id
     */
    @Override
    public List<String> findQuestionImages(Long questionId) {
        return queryFactory
            .select(questionPicture.path)
            .from(questionPicture)
            .where(questionPicture.question.id.eq(questionId))
            .fetch();
    }

    /**
     * 문제 리스트 조회 시 각 문제의 썸네일 이미지를 조회
     *
     * @param questionId : 문제 id
     */
    @Override
    public String findQuestionImage(Long questionId) {
        return queryFactory
            .select(questionPicture.path)
            .from(questionPicture)
            .where(questionPicture.question.id.eq(questionId))
            .offset(0)
            .limit(1)
            .fetchOne();
    }

    /**
     * 강사가 접속 시 문제 등록 최신순으로 12개 조회
     */
    @Override
    public List<QuestionFindRes> findLatestQuestionForTeacher() {

        log.info(
            "QQuestionRepository_findLatestQuestionForTeacher_start");

        return queryFactory
            .select(Projections.constructor(QuestionFindRes.class,
                question.id.as("questionId"),
                student.nickname.as("userNickname"),
                question.title.as("title"),
                question.time.as("createTime"),
                masterCode.name.as("masterCodeName")))
            .from(question)
            .leftJoin(question.student, student).on(student.id.eq(question.student.id))
            .leftJoin(question.masterCode, masterCode).on(masterCode.id.eq(question.masterCode.id))
            .orderBy(timeSort("TIME_DESC"))
            .limit(12)
            .fetch();
    }

    /**
     * 강사가 접속 시 자신이 좋아하는 과목으로 설정한 것과 동일한 문제 최신 순으로 12개 조회
     */
    @Override
    public List<QuestionFindRes> findFavoriteQuestionForTeacher(Integer subjectId) {

        log.info(
            "QQuestionRepository_findFavoriteQuestionForTeacher_start: " + subjectId);

        return queryFactory
            .select(Projections.constructor(QuestionFindRes.class,
                question.id.as("questionId"),
                student.nickname.as("userNickname"),
                question.title.as("title"),
                question.time.as("createTime"),
                masterCode.name.as("masterCodeName")))
            .from(question)
            .leftJoin(question.student, student).on(student.id.eq(question.student.id))
            .leftJoin(question.masterCode, masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(subjectCodeEq(subjectId))
            .orderBy(timeSort("TIME_DESC"))
            .limit(12)
            .fetch();
    }

    /**
     * masterCode Range 처리
     */
    private BooleanExpression mastercodeBetween(int code) {
        if (code % 1000 == 0) {
            return question.masterCode.id.between(1000, 1999);
        } else if (code % 100 == 0) {
            return question.masterCode.id.between(code, code + 99);
        } else if (code % 10 == 0) {
            return question.masterCode.id.between(code, code + 9);
        } else {
            return question.masterCode.id.between(code, code);
        }
    }

    /**
     * keyword 검색. 검색어 없으면 키워드 없이 검색
     */
    private BooleanExpression keywordSearch(String keyword) {
        return keyword == null ? null : question.title.contains(keyword);
    }

    /**
     * 문제 시간 순 정렬
     */
    private OrderSpecifier<LocalDateTime> timeSort(String sort) {
        if (sort.equals("TIME_ASC")) {  // "TIME_ASC" : 시간 오름차순 정렬
            return question.time.asc();
        } else {    // "TIME_DESC" : 시간 내림차순 정렬
            return question.time.desc();
        }
    }

    /**
     * question id로 question 상세 조회하기 위한 where 절에서 사용
     */
    private BooleanExpression questionIdEq(Long id) {
        return question.id.eq(id);
    }

    /**
     * student id로 본인이 등록한 question 조회하기 위한 where 절에서 사용
     */
    private BooleanExpression studentIdEq(Long id) {
        return question.student.id.eq(id);
    }

    /**
     * 학생이 등록한 question 조회할 때 matchingState 검색 조건 반영하여 where 절에서 사용
     */
    private BooleanExpression matchingStateEq(Integer id) {

        if (id == 3) {
            return question.matchingState.lt(id);
        } else {
            return question.matchingState.eq(id);
        }
    }

    /**
     * 강사가 문제를 검색할 때, is_matched 값이 0 혹은 1인 경우만 검색되어야 함. 값이 2인 경우는 이미 매칭이 완료되었으므로 검색 결과에서 제외
     */
    private BooleanExpression matchingStateLt() {
        return question.matchingState.lt(2);
    }

    /**
     * 강사의 좋아하는 과목과 문제의 과목 분류 마스터코드가 일치할 때 where 절
     */
    private BooleanExpression subjectCodeEq(Integer subjectId) {
        return question.masterCode.id.eq(subjectId);
    }
}
