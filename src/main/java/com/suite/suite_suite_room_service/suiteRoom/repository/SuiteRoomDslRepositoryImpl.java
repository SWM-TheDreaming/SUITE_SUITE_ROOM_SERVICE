package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suite.suite_suite_room_service.suiteRoom.dto.StudyCategory;
import com.suite.suite_suite_room_service.suiteRoom.entity.QSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SuiteRoomDslRepositoryImpl implements SuiteRoomDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public SuiteRoomDslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<SuiteRoom> findOpenSuiteWithSearch(boolean isOpen, List<StudyCategory> subjects, String keyword, Pageable pageable) {
        QSuiteRoom suiteRoom = QSuiteRoom.suiteRoom;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(suiteRoom.isOpen.eq(isOpen));

        if(subjects != null && !subjects.isEmpty())
            builder.and(suiteRoom.subject.in(subjects));

        if(!keyword.equals("empty"))
            builder.andAnyOf(
                    suiteRoom.title.contains(keyword),
                    suiteRoom.content.contains(keyword)
            );

        JPAQuery<SuiteRoom> query = jpaQueryFactory.selectFrom(suiteRoom)
                .where(builder);

        return query.orderBy(suiteRoom.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

}
