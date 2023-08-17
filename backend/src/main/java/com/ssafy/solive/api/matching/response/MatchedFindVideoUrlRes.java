package com.ssafy.solive.api.matching.response;

import lombok.Builder;
import lombok.Data;

/**
 * question Id로 Matching의 video url 찾은 후 Response DTO
 */
@Data
@Builder
public class MatchedFindVideoUrlRes {

    // 매칭 비디오 url
    String videoUrl;
}
