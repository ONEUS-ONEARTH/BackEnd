package kr.co.oneusonearth.user.controller;

import kr.co.oneusonearth.user.service.GoogleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 구글 관련 컨트롤러
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class googleController {
    private final GoogleUserService googleUserService;


}
