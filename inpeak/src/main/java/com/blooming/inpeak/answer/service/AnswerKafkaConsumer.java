package com.blooming.inpeak.answer.service;

import com.blooming.inpeak.answer.domain.Answer;
import com.blooming.inpeak.answer.domain.AnswerTask;
import com.blooming.inpeak.answer.dto.command.AnswerCreateCommand;
import com.blooming.inpeak.answer.dto.command.AnswerTaskMessage;
import com.blooming.inpeak.answer.repository.AnswerTaskRepository;
import com.blooming.inpeak.common.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
@Slf4j
@RequiredArgsConstructor
public class AnswerKafkaConsumer {

    private final AnswerTaskRepository answerTaskRepository;
    private final AnswerManagerService answerManagerService;
    private final GPTService gptService;
    private final AnswerPresignedUrlService answerPresignedUrlService;

    /**
     * Kafka 메시지를 수신하여 답변 작업을 처리하는 메서드
     *
     * @param message AnswerTaskMessage 객체
     */
    @KafkaListener(
        topics = "answer-task-topic",
        groupId = "answer-task-group"
    )
    public void listen(AnswerTaskMessage message) {
        Long taskId = message.taskId();

        AnswerTask task = answerTaskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("AnswerTask 없음. taskId=" + taskId));

        AnswerCreateCommand command = AnswerCreateCommand.from(task);

        byte[] audioBytes = answerPresignedUrlService.downloadAudioFromS3(command.audioURL());

        try {
            String feedback = gptService.makeGPTResponse(audioBytes, task.getQuestionContent());
            Answer answer = answerManagerService.generateAnswer(command, feedback);

            task.markSuccess(answer.getId());
            log.info("답변 생성 성공: taskId={}, answerId={}", taskId, answer.getId());

        } catch (Exception e) {
            task.markFailed();
            log.error("답변 생성 실패: taskId={}, error={}", taskId, e.getMessage());

            // 재시도 위해서 다시 오류 반환
            throw e;

        } finally {
            answerTaskRepository.save(task);
        }
    }
}

