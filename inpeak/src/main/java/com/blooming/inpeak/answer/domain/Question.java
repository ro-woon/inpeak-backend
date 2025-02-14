package com.blooming.inpeak.answer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Question {
    @Id
    Long id;

    String title;
}
