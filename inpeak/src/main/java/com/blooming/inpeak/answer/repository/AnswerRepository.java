package com.blooming.inpeak.answer.repository;

import com.blooming.inpeak.answer.domain.Answer;
import com.blooming.inpeak.answer.dto.response.UserStatsResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /**
     * 특정 멤버가 특정 날짜에 진행한 인터뷰의 답변 리스트 조회
     */
    @Query("SELECT a FROM Answer a " +
        "JOIN FETCH a.interview i " +
        "JOIN FETCH a.question q " +
        "WHERE i.memberId = :memberId " +
        "AND i.startDate = :date")
    List<Answer> findAnswersByMemberAndDate(
        @Param("memberId") Long memberId,
        @Param("date") LocalDate date
    );

    /**
     * 특정 멤버의 활동 내용 통계
     */
    @Query("""
    SELECT new com.blooming.inpeak.answer.dto.response.UserStatsResponse(
        COUNT(a.id),
        SUM(CASE WHEN a.status = 'CORRECT' THEN 1 ELSE 0 END),
        SUM(CASE WHEN a.status = 'INCORRECT' THEN 1 ELSE 0 END),
        SUM(CASE WHEN a.status = 'SKIPPED' THEN 1 ELSE 0 END),
        COUNT(DISTINCT a.interviewId),
        COALESCE(SUM(a.runningTime), 0)
    )
    FROM Answer a
    WHERE a.memberId = :memberId
""")
    UserStatsResponse getUserStats(@Param("memberId") Long memberId);



}
