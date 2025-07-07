package com.blooming.inpeak.auth.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("refreshToken")
public class RefreshToken implements Serializable {

    private static final long TWO_WEEKS_IN_MILLISECONDS = 14L * 24 * 60 * 60 * 1000;

    @Id
    private Long memberId;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration = TWO_WEEKS_IN_MILLISECONDS;

    @Builder
    public RefreshToken(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public void update(String newToken) {
        this.refreshToken = newToken;
        this.expiration = TWO_WEEKS_IN_MILLISECONDS;
    }
}
