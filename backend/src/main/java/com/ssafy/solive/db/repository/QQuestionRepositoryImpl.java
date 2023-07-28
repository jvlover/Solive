package com.ssafy.solive.db.repository;

import static com.ssafy.solive.db.entity.QMasterCode.masterCode;
import static com.ssafy.solive.db.entity.QQuestion.question;
import static com.ssafy.solive.db.entity.QQuestionPicture.questionPicture;
import static com.ssafy.solive.db.entity.QUser.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.solive.api.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.response.QuestionFindConditionRes;
import com.ssafy.solive.api.response.QuestionFindDetailRes;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/*
 *  Querydsl을 위한 Repository 구현
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
     *  검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<QuestionFindConditionRes> findByCondition(
        QuestionFindConditionGetReq findCondition) {

        log.info("QQuestionRepository_findByCondition_start: " + findCondition.toString());

        // masterCode 값 합치기
        int code = 1000 + findCondition.getMasterCodeMiddle() + findCondition.getMasterCodeLow();

        return queryFactory
            .select(Projections.constructor(QuestionFindConditionRes.class,
                user.nickname.as("userNickname"),
                questionPicture.pathName.as("imagePathName"),
                question.title.as("title"),
                question.time.as("createTime")))
            .from(question)
            .leftJoin(question.user, user).on(user.id.eq(question.user.id))
            .leftJoin(questionPicture)
            .on(questionPicture.question.id.eq(question.id))
            .leftJoin(masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(mastercodeBetween(code), keywordSearch(findCondition.getKeyword()))
            .orderBy(timeSort(findCondition.getSort()))
            .fetch();
    }

    /*
     *  유저(강사)가 문제를 상세 조회하기 위한 Query
     */
    @Override
    public QuestionFindDetailRes findDetail(Long id) {

        log.info("QQuestionRepository_findDetail_start: " + id);

        return queryFactory
            .select(Projections.constructor(QuestionFindDetailRes.class,
                user.nickname.as("userNickname"),
                question.title.as("title"),
                question.description.as("description"),
                questionPicture.pathName.as("imagePathName"),
                masterCode.id.as("masterCodeId"),
                question.time.as("createTime")))
            .from(question)
            .leftJoin(question.user, user).on(user.id.eq(question.user.id))
            .leftJoin(questionPicture)
            .on(questionPicture.question.id.eq(question.id))
            .leftJoin(masterCode).on(masterCode.id.eq(question.masterCode.id))
            .where(questionIdEq(id))
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
}
