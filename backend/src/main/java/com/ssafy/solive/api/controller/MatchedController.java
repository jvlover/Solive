package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.MatchedRegistPostReq;
import com.ssafy.solive.api.service.MatchedService;
import com.ssafy.solive.common.model.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *  학생이 강사가 지원한 요청들 중 하나를 수락한 후부터의 과정들 API 컨트롤러
 */

@Slf4j
@RestController
@RequestMapping("/matched")
public class MatchedController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    MatchedService matchedService;

    @Autowired
    public MatchedController(MatchedService matchedService) {
        this.matchedService = matchedService;
    }

    /*
     *  학생이 강사의 문제 풀이 지원 요청에 수락하여 매칭을 생성하는 API
     */
    @PostMapping()
    public CommonResponse<?> regist(@RequestBody MatchedRegistPostReq registInfo) {
        /*
         *  registInfo : 학생이 요청 수락을 위해 보낸 request body
         */
        // TODO: 인증 된 사용자인지 확인하는 과정 필요할지도

        log.info("MatchedController_regist_start: " + registInfo.toString());

        matchedService.registMatched(registInfo);

        log.info("MatchedController_regist_end: success");
        return CommonResponse.success(SUCCESS);
    }
}
