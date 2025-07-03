package com.blooming.inpeak.common.config.kafka;

import com.blooming.inpeak.answer.dto.command.AnswerTaskMessage;
import com.blooming.inpeak.common.error.exception.DownloadFailureException;
import com.blooming.inpeak.common.error.exception.NotFoundException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {
    @Bean
    public ConsumerFactory<String, AnswerTaskMessage> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "answer-task-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // JsonDeserializer 구성
        JsonDeserializer<AnswerTaskMessage> jsonDeserializer = new JsonDeserializer<>(AnswerTaskMessage.class);
        jsonDeserializer.addTrustedPackages("com.blooming.inpeak.answer.dto.command");

        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AnswerTaskMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AnswerTaskMessage> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(5);
        factory.setCommonErrorHandler(errorHandler());

        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        // 재시도 간격: 1초, 최대 1회 추가 재시도 (즉 총 2회)
        FixedBackOff backOff = new FixedBackOff(1000L, 1L); // 1초, 1회

        DefaultErrorHandler handler = new DefaultErrorHandler(backOff);

        // 재시도 제외 예외 등록
        handler.addNotRetryableExceptions(
            DownloadFailureException.class,
            NotFoundException.class
        );

        return handler;
    }
}
