package com.github.bestheroz.sample.api.member;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/members")
public class MemberController {
    @Resource private MemberRepository memberRepository;

    @GetMapping
    ResponseEntity<ApiResult> getItems() {
        return Result.ok(this.memberRepository.getCodeItems());
    }
}
