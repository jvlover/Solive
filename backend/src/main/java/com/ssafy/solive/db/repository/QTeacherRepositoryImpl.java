package com.ssafy.solive.db.repository;

import static com.ssafy.solive.db.entity.QMasterCode.masterCode;
import static com.ssafy.solive.db.entity.QTeacher.teacher;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.solive.api.user.response.TeacherOnlineGetRes;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class QTeacherRepositoryImpl implements QTeacherRepository {

    private final JPAQueryFactory queryFactory;

    public QTeacherRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<TeacherOnlineGetRes> findOnlineTeacher() {
        log.info("QTeacherRepositoryImpl_findOnlineTeacher_start");

        return queryFactory
            .select(Projections.constructor(TeacherOnlineGetRes.class,
                teacher.path.as("path"),
                teacher.nickname.as("nickname"),
                masterCode.name.as("masterCodeName"),
                teacher.ratingSum.doubleValue().divide(teacher.ratingCount.doubleValue())
                    .as("rating")))
            .from(teacher)
            .leftJoin(teacher.masterCode, masterCode).on(masterCode.id.eq(teacher.masterCode.id))
            .where(isOnline(), checkNotZeroDivisor())
            .orderBy(ratingSort())
            .limit(3)
            .fetch();
    }

    private BooleanExpression isOnline() {
        return teacher.masterCode.id.eq(11);
    }

    private OrderSpecifier<Double> ratingSort() {
        return teacher.ratingSum.doubleValue().divide(teacher.ratingCount.doubleValue()).desc();
    }

    private BooleanExpression checkNotZeroDivisor() {
        return teacher.ratingCount.ne(0);
    }
}
