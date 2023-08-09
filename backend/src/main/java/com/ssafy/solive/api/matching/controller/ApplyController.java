package com.ssafy.solive.api.matching.controller;

import com.ssafy.solive.api.matching.request.ApplyDeletePutReq;
import com.ssafy.solive.api.matching.request.ApplyFindGetReq;
import com.ssafy.solive.api.matching.request.ApplyRegistPostReq;
import com.ssafy.solive.api.matching.response.ApplyFindRes;
import com.ssafy.solive.api.matching.service.ApplyService;
import com.ssafy.solive.api.matching.service.NotificationService;
import com.ssafy.solive.api.user.service.UserService;
import com.ssafy.solive.common.exception.matching.MatchingPossessionFailException;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.User;
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
 * 강사가 학생이 등록한 문제에 지원할 때 필요한 API를 모은 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/apply")
@CrossOrigin("*")
public class ApplyController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    private final ApplyService applyService;
    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public ApplyController(ApplyService applyService, NotificationService notificationService,
        UserService userService) {
        this.applyService = applyService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * 유저(강사)가 학생이 등록한 문제에 풀이 지원 신청하기 위한 API
     *
     * @param registInfo : 유저(강사)가 문제 풀이 지원 신청 할 때 request body
     */
    @Transactional
    @PostMapping()
    public CommonResponse<?> regist(@RequestBody ApplyRegistPostReq registInfo,
        HttpServletRequest request) {

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);
        log.info("ApplyController_regist_start: " + registInfo.toString() + ", " + userId);

        registInfo.setTeacherId(userId);

        // user : 알림을 받는 학생
        User user = applyService.registApply(registInfo);

        // 학생에게 알림 전송 코드. title과 content의 내용은 일단 임시입니다
        String title = "풀이 요청 도착";
        String content = user.getNickname() + "님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.";
        notificationService.send(user, title, content);

        log.info("ApplyController_regist_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 강사가 문제에 지원했다가 취소하기 위한 API
     *
     * @param deleteInfo : 지원 취소하기 위해 필요한 정보
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(@RequestBody ApplyDeletePutReq deleteInfo,
        HttpServletRequest request) {

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);
        log.info("ApplyController_delete_start: " + deleteInfo.toString() + ", " + userId);

        deleteInfo.setTeacherId(userId);

        boolean isDeleted = applyService.deleteApply(deleteInfo); // 삭제 실패하면 false
        if (isDeleted) {
            log.info("ApplyController_delete_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 강사가 본인의 지원이 아닌 것을 신청 취소하려고 하는 경우
            throw new MatchingPossessionFailException();
        }
    }

    /**
     * 유저(학생)가 자신이 등록한 문제에 어떤 강사들이 지원 신청했는지 검색하기 위한 API
     *
     * @param findCondition : 검색 조건. 정렬/검색 기준 = 예상 풀이시간순, 가격순, 평점순 정렬, 강사의 선호 과목과 문제의 과목 일치 여부 선택
     * @return findResList : 강사명, 강사 프로필 사진, 강사의 선호 과목, 강사가 달아 놓은 SP, 예측 시간, 강사 평점
     */
    @GetMapping()
    public CommonResponse<?> findByCondition(ApplyFindGetReq findCondition) {

        log.info("ApplyController_findByCondition_start: " + findCondition.toString());

        List<ApplyFindRes> findResList = applyService.findByCondition(findCondition);

        if (findResList.size() == 0) {
            log.info("ApplyController_findByCondition_end: No Result");
        } else {
            log.info("ApplyController_findByCondition_end: " + findResList);
        }
        return CommonResponse.success(findResList);
    }
}
