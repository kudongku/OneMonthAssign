package com.example.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "MainController", description = "헬스체크를 포함한 메인 컨트롤러입니다.")
@RestController
public class MainController {

    @Operation(summary = "health check", description = "상태확인용 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK !!"),
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ok");
    }

}
