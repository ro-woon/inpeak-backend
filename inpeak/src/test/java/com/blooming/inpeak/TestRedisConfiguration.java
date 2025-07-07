package com.blooming.inpeak;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;

@TestConfiguration
public class TestRedisConfiguration {

    private static RedisServer redisServer;
    private static boolean isRunning = false;

    private final int redisPort;

    public TestRedisConfiguration(
        @Value("${spring.data.redis.port}") int redisPort
    ) throws IOException {
        this.redisPort = redisPort;
    }

    @PostConstruct
    public void startRedis() throws IOException {
        if (redisServer == null) {
            redisServer = new RedisServerBuilder()
                .port(redisPort)
                .setting("maxmemory 128M")
                .setting("maxmemory-policy allkeys-lru")
                .build();
        }

        if (!isRunning) {
            redisServer.start();
            isRunning = true;
        }
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        if (redisServer != null && isRunning) {
            redisServer.stop();
            isRunning = false;
        }
    }

    @Bean
    @Primary
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://localhost:" + redisPort);
        return Redisson.create(config);
    }
}
