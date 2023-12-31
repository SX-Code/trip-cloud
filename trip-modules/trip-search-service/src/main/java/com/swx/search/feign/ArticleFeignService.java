package com.swx.search.feign;

import com.swx.article.dto.DestinationDTO;
import com.swx.article.dto.StrategyDTO;
import com.swx.article.dto.TravelDTO;
import com.swx.common.core.qo.QueryObject;
import com.swx.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("article-service")
public interface ArticleFeignService {

    @PostMapping("/destinations/search")
    public R<List<Object>> destinationSearch(@RequestBody QueryObject qo);
    @GetMapping("/destinations/getByName")
    public R<DestinationDTO> getDestByName(@RequestParam String name);
    @GetMapping("/destinations/detail")
    public DestinationDTO getDestById(@RequestParam String id);

    @PostMapping("/strategies/search")
    public R<List<Object>> strategySearch(@RequestBody QueryObject qo);
    @GetMapping("/strategies/findByDestName")
    public R<List<StrategyDTO>> findStrategyByDestName(@RequestParam String destName);
    @GetMapping("/strategies/getById")
    public StrategyDTO getStrategyById(@RequestParam String id);

    @PostMapping("/travels/search")
    public R<List<Object>> travelSearch(@RequestBody QueryObject qo);
    @GetMapping("/travels/findByDestName")
    public R<List<TravelDTO>> findTravelByDestName(@RequestParam String destName);
    @GetMapping("/travels/getById")
    R<TravelDTO> getTravelById(@RequestParam String id);
}
