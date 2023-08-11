package com.ssafy.solive.db.repository;

import static com.ssafy.solive.db.entity.QApply.apply;
import static com.ssafy.solive.db.entity.QMasterCode.masterCode;
import static com.ssafy.solive.db.entity.QQuestion.question;
import static com.ssafy.solive.db.entity.QTeacher.teacher;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.solive.api.matching.request.ApplyFindGetReq;
import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.response.ApplyFindRes;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Querydsl을 위한 Apply Repository 구현
 */
@Slf4j
@Repository
public class QApplyRepositoryImpl implements QApplyRepository {

    private final JPAQueryFactory queryFactory;

    public QApplyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 유저(학생)가 강사들의 문제 풀이 지원 신청을 검색하기 위한 Query
     *
     * @param findCondition : 정렬/검색 기준 : 예상 풀이시간순, 가격순, 평점순 정렬, 강사의 선호 과목과 문제의 과목 일치 여부 선택
     */
    @Override
    public List<ApplyFindRes> findByCondition(ApplyFindGetReq findCondition) {

        log.info("QApplyRepository_findByCondition_start: " + findCondition.toString());

        return queryFactory
            .select(Projections.constructor(ApplyFindRes.class,
                apply.id.as("applyId"),
                teacher.nickname.as("teacherNickname"),
                teacher.path.as("teacherPathName"),
                masterCode.name.as("teacherSubjectName"),
                apply.solvePoint.as("solvePoint"),
                apply.estimatedTime.as("estimatedTime"),
                teacher.ratingSum.as("ratingSum"),
                teacher.ratingCount.as("ratingCount")))
            .from(apply)
            .leftJoin(apply.teacher, teacher).on(teacher.id.eq(apply.teacher.id))
            .leftJoin(apply.question, question)
            .on(question.id.eq(apply.question.id))
            .leftJoin(masterCode).on(teacher.masterCode.id.eq(masterCode.id))
            .where(questionIdEq(findCondition.getQuestionId()),
                favoriteCodeMatch(findCondition.getIsFavorite()))
            .orderBy(applySort(findCondition.getSort()))
            .fetch();
    }

    /**
     * 유저(강사)가 자신이 지원했던 신청을 검색하기 위한 Query
     *
     * @param findCondition : 매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<MatchedFindMineRes> findMyApply(MatchedFindMineGetReq findCondition) {

        log.info("QApplyRepository_findMyApply_start: " + findCondition.toString());

        // masterCode 값 합치기
        int code = 1000 + findCondition.getMasterCodeMiddle() + findCondition.getMasterCodeLow();

        return queryFactory
            .select(Projections.constructor(MatchedFindMineRes.class,
                question.id.as("questionId"),
                question.title.as("title"),
                question.time.as("createTime"),
                masterCode.name.as("masterCodeName"),
                question.matchingState.as("matchingState")))
            .from(apply)
            .leftJoin(masterCode).on(masterCode.id.eq(apply.question.masterCode.id))
            .leftJoin(apply.question, question).on(question.id.eq(apply.question.id))
            .where(teacherIdEq(findCondition.getUserId()), mastercodeBetween(code),
                keywordSearch(findCondition.getKeyword()),
                matchingStateEq(findCondition.getMatchingState()))
            .orderBy(timeSort(findCondition.getSort()))
            .offset(findCondition.getPageNum() * 8)
            .limit(8)
            .fetch();
    }

    /**
     * 강사의 지원 신청 정렬 기준 나누기
     *
     * @param sort : 정렬 기준
     */
    private OrderSpecifier<?> applySort(String sort) {
        if (sort.equals("TIME")) {  // 예상 풀이시간 순 오름차순 : TIME
            return apply.estimatedTime.asc();
        } else if (sort.equals("PRICE_ASC")) {  // 가격 순 오름차순 : PRICE_ASC
            return apply.solvePoint.asc();
        } else if (sort.equals("PRICE_DESC")) { // 가격 순 내림차순 : PRICE_DESC
            return apply.solvePoint.desc();
        } else {    // 평점 내림차순 : RATE
            return apply.teacher.ratingSum.avg().desc();
        }
    }

    /**
     * question id로 apply 조회하기 위한 where 절에서 사용
     */
    private BooleanExpression questionIdEq(Long id) {
        return apply.question.id.eq(id);
    }

    /**
     * 강사의 선호 과목과 문제의 과목 일치 여부 선택
     */
    private BooleanExpression favoriteCodeMatch(Boolean isFavorite) {

        //  True : 선택함, False : 선택 안 함
        return !isFavorite ? null
            : apply.teacher.masterCode.id.eq(apply.question.masterCode.id);
    }

    /**
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
     * teacher id로 본인이 지원한 question 조회하기 위한 where 절에서 사용
     */
    private BooleanExpression teacherIdEq(Long id) {
        return teacher.id.eq(id);
    }

    /**
     * question 조회할 때 matchingState 검색 조건 반영하여 where 절에서 사용
     */
    private BooleanExpression matchingStateEq(Integer id) {
        return question.matchingState.eq(id);
    }
}
