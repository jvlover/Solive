package com.ssafy.solive.api.request;

import lombok.Data;

/*
 *  강사의 지원 취소 API에 대한 Request
 */
@Data
public class ApplyDeletePutReq {

    // 취소할 신청 Id
    Long applyId;

    // 지원 신청한 강사 id. DB에 등록된 것과 값이 같아야 함
    Long teacherId;
}
