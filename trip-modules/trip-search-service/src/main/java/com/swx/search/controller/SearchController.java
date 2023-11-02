package com.swx.search.controller;

import com.alibaba.fastjson2.JSONObject;
import com.swx.article.dto.DestinationDTO;
import com.swx.article.dto.StrategyDTO;
import com.swx.article.dto.TravelDTO;
import com.swx.common.core.utils.R;
import com.swx.search.feign.ArticleFeignService;
import com.swx.search.feign.UserInfoFeignService;
import com.swx.search.qo.SearchQueryObject;
import com.swx.search.vo.SearchResult;
import com.swx.user.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/q")
public class SearchController {
    private final ArticleFeignService articleFeignService;
    private final UserInfoFeignService userInfoFeignService;

    public SearchController(ArticleFeignService articleFeignService, UserInfoFeignService userInfoFeignService) {
        this.articleFeignService = articleFeignService;
        this.userInfoFeignService = userInfoFeignService;
    }

    @GetMapping
    public R<?> search(SearchQueryObject qo) {
        qo.setKeyword(URLDecoder.decode(qo.getKeyword(), StandardCharsets.UTF_8));
        // 请求分发器
        switch (qo.getType()) {
            case 0:
                // 目的地
                return this.searchForDest(qo);
            case 1:
                // 攻略
                return this.searchForStrategy(qo);
            case 2:
                // 游记
                return this.searchForTravel(qo);
            case 3:
                // 用户
                return this.searchForUserInfo(qo);
            default:
                // 所有
                return this.searchForAll(qo);
        }
    }

    private R<?> searchForUserInfo(SearchQueryObject qo) {
        return null;
    }

    private R<?> searchForAll(SearchQueryObject qo) {
        return null;
    }

    private R<?> searchForTravel(SearchQueryObject qo) {
        return null;
    }

    private R<?> searchForStrategy(SearchQueryObject qo) {
        return null;
    }

    private R<?> searchForDest(SearchQueryObject qo) {
        SearchResult result = new SearchResult();
        // 远程调用目的地模块，根据名称查询目的地
        R<DestinationDTO> destResult = articleFeignService.getDestByName(qo.getKeyword());
        DestinationDTO dest = destResult.checkAndGet();
        if (dest != null) {
            result.setTotal(1L);
            // 查询攻略
            R<List<StrategyDTO>> strategyResult = articleFeignService.findStrategyByDestName(qo.getKeyword());
            List<StrategyDTO> strategyDTOS = strategyResult.checkAndGet();
            result.setStrategies(strategyDTOS);
            result.setTotal(result.getTotal() + strategyDTOS.size());

            // 查询游记
            R<List<TravelDTO>> travelResult = articleFeignService.findTravelByDestName(qo.getKeyword());
            List<TravelDTO> travelDTOS = travelResult.checkAndGet();
            result.setTravels(travelDTOS);
            result.setTotal(result.getTotal() + travelDTOS.size());

            // 查询用户
            R<List<UserInfoDTO>> userResult = userInfoFeignService.findUserByDestName(qo.getKeyword());
            List<UserInfoDTO> userInfoDTOS = userResult.checkAndGet();
            result.setUsers(userInfoDTOS);
            result.setTotal(result.getTotal() + userInfoDTOS.size());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("qo", qo);
        jsonObject.put("result", result);
        jsonObject.put("dest", dest);
        return R.ok(jsonObject);
    }
}
