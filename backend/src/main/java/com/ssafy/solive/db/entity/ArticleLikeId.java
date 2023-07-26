package com.ssafy.solive.db.entity;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLikeId implements Serializable {

    // ArticleLike의 식별자 클래스
    // 필드의 이름은 ArticleLike와 똑같이 하고
    // 타입은 PK의 타입과 똑같이
    private Long user;
    private Long article;
}
