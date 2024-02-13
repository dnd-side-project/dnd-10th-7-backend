package com.sendback.domain.scrap.controller;

import com.sendback.domain.scrap.dto.response.ClickScrapResponse;
import com.sendback.domain.scrap.service.ScrapService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sendback.global.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects")
public class ScrapController {

    private final ScrapService scrapService;

    @PutMapping("/{projectId}/scrap")
    public ApiResponse<ClickScrapResponse> click(
            @UserId Long userId,
            @PathVariable Long projectId) {
        return success(scrapService.clickScrap(userId, projectId));
    }

}
