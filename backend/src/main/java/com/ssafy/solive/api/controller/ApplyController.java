package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.ApplyDeletePutReq;
import com.ssafy.solive.api.request.ApplyRegistPostReq;
import com.ssafy.solive.api.service.ApplyService;
import com.ssafy.solive.common.exception.ApplyPossessionFailException;
import com.ssafy.solive.common.model.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *  강사가 학생이 등록한 문제에 지원할 때 필요한 API를 모은 컨트롤러
 */

@Slf4j
@RestController
@RequestMapping("/apply")
public class ApplyController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    ApplyService applyService;

    @Autowired
    public ApplyController(ApplyService applyService) {
        this.applyService = applyService;
    }

    /*
     *  유저(강사)가 학생이 등록한 문제에 풀이 지원 신청하기 위한 API
     */
    @PostMapping()
    public CommonResponse<?> regist(@RequestBody ApplyRegistPostReq registInfo) {
        /*
         *  registInfo : 유저(강사)가 문제 풀이 지원 신청 할 때 request body
         */
        // TODO: 인증 된 사용자인지 확인하는 과정 필요할지도

        log.info("ApplyController_regist_start: " + registInfo.toString());

        applyService.registApply(registInfo);

        log.info("ApplyController_regist_end: success");
        return CommonResponse.success(SUCCESS);
    }


    /*
     *  강사가 문제에 지원했다가 취소하기 위한 API
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(@RequestBody ApplyDeletePutReq deleteInfo) {
        /*
         *  deleteInfo : 지원 취소하기 위해 필요한 정보
         */
        // TODO: 인증 된 사용자인지 확인하는 과정 필요

        log.info("ApplyController_delete_start: " + deleteInfo.toString());

        boolean isDeleted = applyService.deleteApply(deleteInfo); // 삭제 실패하면 false
        if (isDeleted) {
            log.info("ApplyController_delete_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            // 강사가 본인의 지원이 아닌 것을 신청 취소하려고 하는 경우
            log.info("ApplyController_delete_end: ApplyPossessionFailException");
            throw new ApplyPossessionFailException();
        }
    }
}
