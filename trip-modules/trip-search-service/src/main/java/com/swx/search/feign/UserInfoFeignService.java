package com.swx.search.feign;

import com.swx.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-service")
public interface UserInfoFeignService {

    /**
     * 如果 Feign 发起远程调用后，接收的类型没有明确具体类型是什么
     * Feign 会将返回的 JSON 结构转换为 LinkedHashMap 对象
     */
    @GetMapping("/users/")
    R<List<Object>> findList(@RequestParam Integer current, @RequestParam Integer limit);
}
