package com.ssafy.solive.db.repository;

import static com.ssafy.solive.db.entity.QApply.apply;
import static com.ssafy.solive.db.entity.QMasterCode.masterCode;
import static com.ssafy.solive.db.entity.QQuestion.question;
import static com.ssafy.solive.db.entity.QTeacher.teacher;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.solive.api.request.ApplyFindGetReq;
import com.ssafy.solive.api.response.ApplyFindRes;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/*
 *  Querydsl을 위한 Apply Repository 구현
 */
@Slf4j
@Repository
public class QApplyRepositoryImpl implements QApplyRepository {

    private final JPAQueryFactory queryFactory;

    public QApplyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /*
     *  유저(학생)가 강사들의 문제 풀이 지원 신청을 검색하기 위한 Query
     *  정렬 / 검색 기준 : 예상 풀이시간순, 가격순, 평점순 정렬, 강사의 선호 과목과 문제의 과목 일치 여부 선택
     */
    @Override
    public List<ApplyFindRes> findByCondition(ApplyFindGetReq findCondition) {

        log.info("QApplyRepository_findByCondition_start: " + findCondition.toString());

        return queryFactory
            .select(Projections.constructor(ApplyFindRes.class,
                apply.id.as("applyId"),
                teacher.nickname.as("teacherNickname"),
                teacher.pathName.as("teacherPathName"),
                masterCode.id.as("teacherSubjectId"),
                apply.solvePoint.as("solvePoint"),
                apply.estimatedTime.as("estimatedTime"),
                teacher.ratingSum.as("ratingSum"),
                teacher.ratingCount.as("ratingCount")))
            .from(apply)
            .leftJoin(apply.teacher).on(teacher.id.eq(apply.teacher.id))
            .leftJoin(apply.question)
            .on(question.id.eq(apply.question.id))
            .where(questionIdEq(findCondition.getQuestionId()),
                favoriteCodeMatch(findCondition.getIsFavorite()))
            .orderBy(applySort(findCondition.getSort()))
            .fetch();
    }

    /*
     *  강사의 지원 신청 정렬 기준
     *  예상 풀이시간 순 오름차순 : TIME
     *  가격 순 오름차순 : PRICE_ASC, 내림차순 : PRICE_DESC
     *  평점 내림차순 : RATE
     */
    private OrderSpecifier<?> applySort(String sort) {
        if (sort.equals("TIME")) {
            return apply.estimatedTime.asc();
        } else if (sort.equals("PRICE_ASC")) {
            return apply.solvePoint.asc();
        } else if (sort.equals("PRICE_DESC")) {
            return apply.solvePoint.desc();
        } else {
            return apply.teacher.ratingSum.avg().desc();
        }
    }

    /*
     *  question id로 apply 조회하기 위한 where절에서 사용
     */
    private BooleanExpression questionIdEq(Long id) {
        return apply.question.id.eq(id);
    }

    /*
     *  강사의 선호 과목과 문제의 과목 일치 여부 선택
     *  True : 선택함, False : 선택 안 함
     */
    private BooleanExpression favoriteCodeMatch(Boolean isFavorite) {
        return !isFavorite ? null
            : apply.teacher.masterCode.id.eq(apply.question.masterCode.id);
    }
}
