package com.blooming.inpeak.answer.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.blooming.inpeak.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswerTest {

    @DisplayName("스킵된 답변 생성 테스트")
    @Test
    void ofSkipped() {
        //given
        Member member = new Member(1L);
        Long questionId = 100L;
        Long interviewId = 200L;

        //when
        Answer skippedAnswer = Answer.ofSkipped(member, questionId, interviewId);

        //then
        assertNotNull(skippedAnswer);
        assertEquals(questionId, skippedAnswer.getQuestionId());
        assertEquals(member.getId(), skippedAnswer.getMemberId());
        assertEquals(interviewId, skippedAnswer.getInterviewId());
        assertFalse(skippedAnswer.isUnderstood()); // 스킵된 답변은 이해 여부가 false
        assertEquals(AnswerStatus.SKIPPED, skippedAnswer.getStatus()); // 스킵된 상태 설정
    }
}
