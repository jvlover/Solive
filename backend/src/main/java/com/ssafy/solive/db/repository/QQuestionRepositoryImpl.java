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
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class QQuestionRepositoryImpl implements QQuestionRepository {

    private final JPAQueryFactory queryFactory;

    public QQuestionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<QuestionFindConditionRes> findByCondition(
        QuestionFindConditionGetReq findCondition) {

        int code = 1000 + findCondition.getMasterCodeMiddle() + findCondition.getMasterCodeLow();
        return queryFactory
            .select(Projections.constructor(QuestionFindConditionRes.class, user.nickname,
                questionPicture.pathName, question.title, question.time))
            .from(question)
            .leftJoin(question.user, user)
            .leftJoin(questionPicture, questionPicture)
            .where(mastercodeBetween(code), keywordSearch(findCondition.getKeyword()))
            .orderBy(timeSort(findCondition.getSort()))
            .fetch();
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
        return keyword.isEmpty() ? null : question.title.contains(keyword);
    }

    private OrderSpecifier<LocalDateTime> timeSort(String sort) {
        if (sort.equals("TIME_ASC")) {
            return question.time.asc();
        } else {
            return question.time.desc();
        }
    }
}
