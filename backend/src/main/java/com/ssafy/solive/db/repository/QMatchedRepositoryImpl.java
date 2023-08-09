package com.ssafy.solive.db.repository;

import static com.ssafy.solive.db.entity.QMasterCode.masterCode;
import static com.ssafy.solive.db.entity.QMatched.matched;
import static com.ssafy.solive.db.entity.QQuestion.question;
import static com.ssafy.solive.db.entity.QTeacher.teacher;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Querydsl을 위한 Matched Repository 구현
 */
@Slf4j
@Repository
public class QMatchedRepositoryImpl implements QMatchedRepository {

    private final JPAQueryFactory queryFactory;

    public QMatchedRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 유저(강사)가 자신의 매칭 이력을 검색하기 위한 Query
     *
     * @param findCondition : 매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @Override
    public List<MatchedFindMineRes> findMyMatching(MatchedFindMineGetReq findCondition) {

        log.info("QMatchedRepository_findMyApply_start: " + findCondition.toString());

        // masterCode 값 합치기
        int code = 1000 + findCondition.getMasterCodeMiddle() + findCondition.getMasterCodeLow();

        return queryFactory
            .select(Projections.constructor(MatchedFindMineRes.class,
                question.id.as("questionId"),
                question.title.as("title"),
                question.time.as("createTime"),
                masterCode.name.as("masterCodeName"),
                question.matchingState.as("matchingState")))
            .from(matched)
            .leftJoin(masterCode).on(masterCode.id.eq(matched.question.masterCode.id))
            .leftJoin(matched.question, question).on(question.id.eq(matched.question.id))
            .where(teacherIdEq(findCondition.getUserId()), mastercodeBetween(code),
                keywordSearch(findCondition.getKeyword()),
                matchingStateEq(findCondition.getMatchingState()))
            .orderBy(timeSort(findCondition.getSort()))
            .offset(findCondition.getPageNum() * 8)
            .limit(8)
            .fetch();
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
