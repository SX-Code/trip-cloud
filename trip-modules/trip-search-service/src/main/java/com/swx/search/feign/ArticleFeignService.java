package com.swx.search.feign;

import com.swx.common.core.qo.QueryObject;
import com.swx.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("article-service")
public interface ArticleFeignService {

    @GetMapping("/destinations/search")
    public R<List<Object>> destinationSearch(QueryObject qo);

    @PostMapping("/strategies/search")
    public R<List<Object>> strategySearch(@RequestBody QueryObject qo);

    @PostMapping("/travels/search")
    public R<List<Object>> travelSearch(@RequestBody QueryObject qo);
}
