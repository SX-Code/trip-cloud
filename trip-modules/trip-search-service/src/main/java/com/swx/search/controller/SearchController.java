package com.swx.search.controller;

import com.alibaba.fastjson2.JSONObject;
import com.swx.article.dto.DestinationDTO;
import com.swx.article.dto.StrategyDTO;
import com.swx.article.dto.TravelDTO;
import com.swx.common.core.utils.R;
import com.swx.search.DestinationEs;
import com.swx.search.StrategyEs;
import com.swx.search.TravelEs;
import com.swx.search.UserInfoEs;
import com.swx.search.feign.ArticleFeignService;
import com.swx.search.feign.UserInfoFeignService;
import com.swx.search.qo.SearchQueryObject;
import com.swx.search.service.ElasticsearchService;
import com.swx.search.vo.SearchResult;
import com.swx.user.dto.UserInfoDTO;
import org.springframework.data.domain.Page;
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
    private final ElasticsearchService elasticsearchService;

    public SearchController(ArticleFeignService articleFeignService, UserInfoFeignService userInfoFeignService, ElasticsearchService elasticsearchService) {
        this.articleFeignService = articleFeignService;
        this.userInfoFeignService = userInfoFeignService;
        this.elasticsearchService = elasticsearchService;
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
        Page<UserInfoDTO> page = elasticsearchService.searchWithHighlight(UserInfoEs.class, UserInfoDTO.class, qo,
                (clazz, id) -> userInfoFeignService.getById(id).checkAndGet(), "city", "info");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("qo", qo);
        jsonObject.put("page", page);
        return R.ok(jsonObject);
    }

    private R<?> searchForAll(SearchQueryObject qo) {
        SearchResult result = new SearchResult();
        Page<UserInfoDTO> userPage = elasticsearchService.searchWithHighlight(UserInfoEs.class, UserInfoDTO.class, qo,
                (clazz, id) -> userInfoFeignService.getById(id).checkAndGet(), "city", "info");
        result.setUsers(userPage.getContent());
        result.setTotal(userPage.getTotalElements());

        Page<TravelDTO> travelPage = elasticsearchService.searchWithHighlight(TravelEs.class, TravelDTO.class, qo,
                (clazz, id) -> articleFeignService.getTravelById(id).checkAndGet(), "title", "summary");
        result.setTravels(travelPage.getContent());
        result.setTotal(result.getTotal() + travelPage.getTotalElements());

        Page<StrategyDTO> strategyPage = elasticsearchService.searchWithHighlight(StrategyEs.class, StrategyDTO.class, qo,
                (clazz, id) -> articleFeignService.getStrategyById(id), "title", "subtitle", "summary");
        result.setStrategies(strategyPage.getContent());
        result.setTotal(result.getTotal() + strategyPage.getTotalElements());

        Page<DestinationDTO> destPage = elasticsearchService.searchWithHighlight(DestinationEs.class, DestinationDTO.class, qo,
                (clazz, id) -> articleFeignService.getDestById(id), "name", "info");
        result.setDests(destPage.getContent());
        result.setTotal(result.getTotal() + destPage.getTotalElements());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("qo", qo);
        return R.ok(jsonObject);
    }

    private R<?> searchForTravel(SearchQueryObject qo) {
        Page<TravelDTO> page = elasticsearchService.searchWithHighlight(TravelEs.class, TravelDTO.class, qo,
                (clazz, id) -> articleFeignService.getTravelById(id).checkAndGet(), "title", "summary");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("qo", qo);
        jsonObject.put("page", page);
        return R.ok(jsonObject);
    }

    private R<?> searchForStrategy(SearchQueryObject qo) {
        Page<StrategyDTO> page = elasticsearchService.searchWithHighlight(StrategyEs.class, StrategyDTO.class, qo,
                (clazz, id) -> articleFeignService.getStrategyById(id), "title", "subtitle", "summary");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("qo", qo);
        jsonObject.put("page", page);
        return R.ok(jsonObject);
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
