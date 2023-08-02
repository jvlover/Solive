package com.ssafy.solive.db.repository;

import static com.ssafy.solive.db.entity.QMasterCode.masterCode;
import static com.ssafy.solive.db.entity.QQuestion.question;
import static com.ssafy.solive.db.entity.QQuestionPicture.questionPicture;
import static com.ssafy.solive.db.entity.QStudent.student;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.solive.api.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.request.QuestionFindMineGetReq;
import com.ssafy.solive.api.response.QuestionFindConditionRes;
import com.ssafy.solive.api.response.QuestionFindDetailRes;
import com.ssafy.solive.api.response.QuestionFindMineRes;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/*
 *  Querydsl을 위한 Question Repository 구현
 */
@Slf4j
@Repository
public class QQuestionRepositoryImpl implements QQuestionRepository {

    private final JPAQueryFactory queryFactory;

    public QQuestionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /*
     *  유저(강사)가 문제를 검색하기 위한 Query
     *  제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<QuestionFindConditionRes> findByCondition(
        QuestionFindConditionGetReq findCondition) {

        log.info("QQuestionRepository_findByCondition_start: " + findCondition.toString());

        // masterCode 값 합치기
        int code = 1000 + findCondition.getMasterCodeMiddle() + findCondition.getMasterCodeLow();

        return queryFactory
            .select(Projections.constructor(QuestionFindConditionRes.class,
                question.id.as("questionId"),
                student.nickname.as("userNickname"),
                question.title.as("title"),
                question.time.as("createTime"),
                masterCode.name.as("masterCodeName")))
            .from(question)
            .leftJoin(question.student).on(student.id.eq(question.student.id))
            .leftJoin(question.masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(mastercodeBetween(code), keywordSearch(findCondition.getKeyword()),
                matchingStateLt())
            .orderBy(timeSort(findCondition.getSort()))
            .fetch();
    }

    /*
     *  유저가 문제를 상세 조회하기 위한 Query
     */
    @Override
    public QuestionFindDetailRes findDetail(Long id) {

        log.info("QQuestionRepository_findDetail_start: " + id);

        return queryFactory
            .select(Projections.constructor(QuestionFindDetailRes.class,
                student.nickname.as("userNickname"),
                question.title.as("title"),
                question.description.as("description"),
                masterCode.id.as("masterCodeId"),
                question.time.as("createTime")))
            .from(question)
            .leftJoin(question.student).on(student.id.eq(question.student.id))
            .leftJoin(question.masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(questionIdEq(id))
            .fetchOne();
    }

    /*
     *  유저(학생)가 자신이 등록했던 문제를 검색하기 위한 Query
     *  매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<QuestionFindMineRes> findMyQuestion(
        QuestionFindMineGetReq findCondition) {

        log.info("QQuestionRepository_findMyQuestion_start: " + findCondition.toString());

        // masterCode 값 합치기
        int code = 1000 + findCondition.getMasterCodeMiddle() + findCondition.getMasterCodeLow();

        return queryFactory
            .select(Projections.constructor(QuestionFindMineRes.class,
                question.id.as("questionId"),
                question.title.as("title"),
                question.time.as("createTime"),
                masterCode.name.as("masterCodeName"),
                question.matchingState.as("matchingState")))
            .from(question)
            .leftJoin(masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(studentIdEq(findCondition.getStudentId()), mastercodeBetween(code),
                keywordSearch(findCondition.getKeyword()),
                matchingStateEq(findCondition.getMatchingState()))
            .orderBy(timeSort(findCondition.getSort()))
            .fetch();
    }

    // 문제 상세 정보 조회 시 해당 문제의 이미지들 조회
    @Override
    public List<String> findQuestionImages(Long questionId) {
        return queryFactory
            .select(questionPicture.pathName)
            .from(questionPicture)
            .where(questionPicture.question.id.eq(questionId))
            .fetch();
    }

    // 문제 리스트 조회 시 각 문제의 썸네일 이미지를 조회
    @Override
    public String findQuestionImage(Long questionId) {
        return queryFactory
            .select(questionPicture.pathName)
            .from(questionPicture)
            .where(questionPicture.question.id.eq(questionId))
            .offset(0)
            .limit(1)
            .fetchOne();
    }

    /*
     * masterCode Range 처리
     */
    private BooleanExpression mastercodeBetween(int code) {
        if (code % 1000 == 0) {
            return masterCode.id.between(1000, 1999);
        } else if (code % 100 == 0) {
            return masterCode.id.between(code, code + 99);
        } else if (code % 10 == 0) {
            return masterCode.id.between(code, code + 9);
        } else {
            return masterCode.id.between(code, code);
        }
    }

    /*
     *  keyword 검색. 검색어 없으면 키워드 없이 검색
     */
    private BooleanExpression keywordSearch(String keyword) {
        return keyword == null ? null : question.title.contains(keyword);
    }

    /*
     *  문제 시간 순 정렬
     *  "TIME_ASC" : 시간 오름차순 정렬
     *  "TIME_DESC" : 시간 내림차순 정렬
     */
    private OrderSpecifier<LocalDateTime> timeSort(String sort) {
        if (sort.equals("TIME_ASC")) {
            return question.time.asc();
        } else {
            return question.time.desc();
        }
    }

    /*
     *  question id로 question 상세 조회하기 위한 where절에서 사용
     */
    private BooleanExpression questionIdEq(Long id) {
        return question.id.eq(id);
    }

    /*
     *  student id로 본인이 등록한 question 조회하기 위한 where절에서 사용
     */
    private BooleanExpression studentIdEq(Long id) {
        return student.id.eq(id);
    }

    /*
     *  학생이 등록한 question 조회할 때 matchingState 검색 조건 반영하여 where절에서 사용
     */
    private BooleanExpression matchingStateEq(Integer id) {
        return question.matchingState.eq(id);
    }

    /*
     *  강사가 문제를 검색할 때, is_matched 값이 0 혹은 1인 경우만 검색되어야 함.
     *  값이 2인 경우는 이미 매칭이 완료 되었으므로 검색 결과에서 제외
     */
    private BooleanExpression matchingStateLt() {
        return question.matchingState.lt(2);
    }

}
