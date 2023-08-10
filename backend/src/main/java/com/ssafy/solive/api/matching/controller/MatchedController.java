package com.ssafy.solive.api.matching.controller;

import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.request.MatchedPutReq;
import com.ssafy.solive.api.matching.request.MatchedRegistPostReq;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import com.ssafy.solive.api.matching.response.MatchedRegistPostRes;
import com.ssafy.solive.api.matching.service.MatchedService;
import com.ssafy.solive.api.matching.service.NotificationService;
import com.ssafy.solive.api.user.service.UserService;
import com.ssafy.solive.common.model.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 학생이 강사가 지원한 요청들 중 하나를 수락한 후 부터의 과정들 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/matched")
@CrossOrigin("*")
public class MatchedController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    private final MatchedService matchedService;
    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public MatchedController(MatchedService matchedService, NotificationService notificationService,
        UserService userService) {
        this.matchedService = matchedService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * 학생이 강사의 문제 풀이 지원 요청에 수락하여 매칭을 생성하는 API
     *
     * @param registInfo : 학생이 요청 수락을 위해 보낸 request body
     */
    @Transactional
    @PostMapping()
    public CommonResponse<?> regist(@RequestBody MatchedRegistPostReq registInfo,
        HttpServletRequest request) {

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);
        log.info("MatchedController_regist_start: " + registInfo.toString() + ", " + userId);

        registInfo.setStudentId(userId);

        MatchedRegistPostRes matchedRegistPostRes = matchedService.registMatched(registInfo);
        String sessionId = matchedRegistPostRes.getSessionId();

        // 강사에게 알림 전송 코드. title과 content의 내용은 일단 임시
        String title =
            matchedRegistPostRes.getUser().getNickname() + "님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. "
                + "강의 세션 ID는 " + sessionId + " 입니다.";
        notificationService.send(matchedRegistPostRes.getUser(), title, sessionId);

        log.info("MatchedController_regist_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 유저가 자신이 등록했던 문제(매칭 이력들)을 검색하기 위한 API. 유저가 학생인지, 강사인지 상태 코드를 보내면, 확인 후 각자 다르게 서비스 처리
     *
     * @param findCondition : 검색 조건. 매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @GetMapping("/my")
    public CommonResponse<?> findMyMatching(MatchedFindMineGetReq findCondition,
        HttpServletRequest request) {

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);
        log.info(
            "MatchedController_findMyMatching_start: " + findCondition.toString() + ", " + userId);

        findCondition.setUserId(userId);

        List<MatchedFindMineRes> findResList = matchedService.findMyMatching(findCondition);

        if (findResList.size() == 0) {
            log.info("MatchedController_findMyMatching_end: No Result");
        } else {
            log.info("MatchedController_findMyMatching_end: " + findResList);
        }
        return CommonResponse.success(findResList);
    }

    /**
     * 강사가 학생이 열은 세션에 들어가서 강의 시작
     *
     * @param sessionInfo : 세션 Id 정보
     */
    @PutMapping("/start")
    public CommonResponse<?> start(@RequestBody MatchedPutReq sessionInfo) {

        log.info("MatchedController_start_start: " + sessionInfo.toString());

        matchedService.startMatching(sessionInfo);

        log.info("MatchedController_start_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 강의 시간 연장
     *
     * @param sessionInfo : 세션 Id 정보
     */
    @PutMapping("/extend")
    public CommonResponse<?> extend(@RequestBody MatchedPutReq sessionInfo) {

        log.info("MatchedController_extend_start: " + sessionInfo.toString());

        matchedService.extendMatching(sessionInfo);

        log.info("MatchedController_extend_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 강의 세션 종료
     *
     * @param sessionInfo : 세션 Id 정보
     */
    @PutMapping("/end")
    public CommonResponse<?> end(@RequestBody MatchedPutReq sessionInfo) {

        log.info("MatchedController_end_start: " + sessionInfo.toString());

        matchedService.endMatching(sessionInfo);

        log.info("MatchedController_end_end: success");
        return CommonResponse.success(SUCCESS);
    }
}
