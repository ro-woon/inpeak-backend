package com.blooming.inpeak.answer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Entity
public class Interview {

    @Id
    Long id;

    LocalDateTime createdAt;
}
