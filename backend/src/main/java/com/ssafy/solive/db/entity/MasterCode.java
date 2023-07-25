package com.ssafy.solive.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class MasterCode {

    @Id
    private Integer id;

    private String name;
}
