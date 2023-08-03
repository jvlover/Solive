package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.request.MatchedRegistPostReq;
import com.ssafy.solive.api.response.MatchedFindMineRes;
import com.ssafy.solive.api.service.MatchedService;
import com.ssafy.solive.common.model.CommonResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
@CrossOrigin("*")
public class MatchedController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    private final MatchedService matchedService;

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

    /*
     *  유저가 자신이 등록했던 문제(매칭 이력들)을 검색하기 위한 API
     *  유저가 학생인지, 강사인지 상태 코드를 보내면, 확인 후 각자 다르게 서비스 처리
     *  매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @GetMapping("/my")
    public CommonResponse<?> findMyMatching(MatchedFindMineGetReq findCondition) {
        /*
         *  findCondition : 검색 조건
         */

        log.info("MatchedController_findMyMatching_start: " + findCondition.toString());

        List<MatchedFindMineRes> findResList = matchedService.findMyMatching(findCondition);

        log.info("MatchedController_findMyMatching_end: " + findResList.toString());
        return CommonResponse.success(findResList);
    }
}
