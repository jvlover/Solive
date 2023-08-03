package com.ssafy.solive.api.controller;

import lombok.extern.slf4j.Slf4j;
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
}
