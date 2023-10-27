package com.swx.article.feign;

import com.swx.common.core.utils.R;
import com.swx.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserInfoFeignService {
    @GetMapping("/users/getById")
    R<UserInfoDTO> getById(@RequestParam Long id);
}
