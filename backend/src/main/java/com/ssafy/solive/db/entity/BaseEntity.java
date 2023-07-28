package com.ssafy.solive.db.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
}
