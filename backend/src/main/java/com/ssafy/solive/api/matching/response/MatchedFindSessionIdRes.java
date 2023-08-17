package com.ssafy.solive.api.matching.response;

import lombok.Builder;
import lombok.Data;

/**
 * question Id로 Matching의 Session Id 찾은 후 Response DTO
 */
@Data
@Builder
public class MatchedFindSessionIdRes {

    // 매칭 세션 Id
    String sessionId;
}
