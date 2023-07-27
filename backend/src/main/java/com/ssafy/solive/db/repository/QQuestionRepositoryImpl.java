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

@Slf4j
@Repository
public class QQuestionRepositoryImpl implements QQuestionRepository {

    private final JPAQueryFactory queryFactory;

    public QQuestionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<QuestionFindConditionRes> findByCondition(
        QuestionFindConditionGetReq findCondition) {

        log.info("QQuestionRepository_findByCondition_start: " + findCondition.toString());
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

    @Override
    public QuestionFindDetailRes findDetail(Long id) {
        log.info("QQuestionRepository_findDetail_start: " + Long.toString(id));

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

    private BooleanExpression keywordSearch(String keyword) {
        return keyword == null ? null : question.title.contains(keyword);
    }

    private OrderSpecifier<LocalDateTime> timeSort(String sort) {
        if (sort.equals("TIME_ASC")) {
            return question.time.asc();
        } else {
            return question.time.desc();
        }
    }

    private BooleanExpression questionIdEq(Long id) {
        return question.id.eq(id);
    }
}
