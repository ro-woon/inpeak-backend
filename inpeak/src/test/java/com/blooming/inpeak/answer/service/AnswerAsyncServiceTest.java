package com.blooming.inpeak.answer.service;

import com.blooming.inpeak.answer.domain.AnswerTask;
import com.blooming.inpeak.answer.domain.AnswerTaskStatus;
import com.blooming.inpeak.answer.dto.command.AnswerCreateCommand;
import com.blooming.inpeak.answer.dto.command.AnswerTaskMessage;
import com.blooming.inpeak.answer.dto.response.TaskIDResponse;
import com.blooming.inpeak.answer.repository.AnswerTaskRepository;
import com.blooming.inpeak.common.error.exception.NotFoundException;
import com.blooming.inpeak.question.domain.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerAsyncServiceTest {

    @Mock
    private AnswerManagerService answerManagerService;

    @Mock
    private AnswerTaskRepository answerTaskRepository;

    @Mock
    private KafkaTemplate<String, AnswerTaskMessage> kafkaTemplate;

    @InjectMocks
    private AnswerAsyncService answerAsyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestAsyncAnswerCreation_정상_작동() {
        // given
        AnswerCreateCommand command = new AnswerCreateCommand(
            "audio",
            2L,
            3L,
            1L,
            4L,
            "video"
        );
        Question question = mock(Question.class);
        when(question.getContent()).thenReturn("Spring이란?");
        when(answerManagerService.validateAndGetQuestion(command)).thenReturn(question);

        AnswerTask savedTask = AnswerTask.waiting(command, "Spring이란?");
        setField(savedTask, "id", 123L);
        when(answerTaskRepository.save(any())).thenReturn(savedTask);

        // when
        TaskIDResponse response = answerAsyncService.requestAsyncAnswerCreation(command);

        // then
        assertThat(response.taskId()).isEqualTo(123L);
        verify(kafkaTemplate).send(eq("answer-task-topic"), any(AnswerTaskMessage.class));
    }

    // 테스트 목적상 private 필드 직접 설정 유틸
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
