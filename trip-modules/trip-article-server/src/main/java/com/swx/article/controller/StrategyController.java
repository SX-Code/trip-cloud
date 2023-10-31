package com.swx.article.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.Strategy;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.domain.StrategyContent;
import com.swx.article.domain.StrategyRank;
import com.swx.article.qo.StrategyQuery;
import com.swx.article.service.StrategyRankService;
import com.swx.article.service.StrategyService;
import com.swx.article.utils.OssUtil;
import com.swx.article.vo.StrategyCondition;
import com.swx.common.core.utils.R;
import com.swx.common.security.annotation.RequireLogin;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/strategies")
public class StrategyController {

    private final StrategyService strategyService;
    private final StrategyRankService strategyRankService;

    public StrategyController(StrategyService strategyService, StrategyRankService strategyRankService) {
        this.strategyService = strategyService;
        this.strategyRankService = strategyRankService;
    }

    @GetMapping("/query")
    public R<Page<Strategy>> pageList(StrategyQuery query) {
        return R.ok(strategyService.pageStrategy(query));
    }

    @GetMapping("/detail")
    public R<Strategy> getById(Long id) {
        strategyService.viewnumIncr(id);
        return R.ok(strategyService.getById(id));
    }

    @GetMapping("/conditions")
    public R<Map<String, List<StrategyCondition>>> getConditions() {
        Map<String, List<StrategyCondition>> result = new HashMap<>();
        List<StrategyCondition> chinaCondition = strategyService.findDestCondition(Strategy.ABROAD_NO);
        List<StrategyCondition> abroadCondition = strategyService.findDestCondition(Strategy.ABROAD_YES);
        List<StrategyCondition> themeCondition = strategyService.findThemeCondition();

        result.put("chinaCondition", chinaCondition);
        result.put("abroadCondition", abroadCondition);
        result.put("themeCondition", themeCondition);

        return R.ok(result);
    }

    @GetMapping("/content")
    public R<StrategyContent> getContentById(Long id) {
        return R.ok(strategyService.getContentById(id));
    }

    @GetMapping("/groups")
    public R<List<StrategyCatalog>> groupByCatalog(Long destId) {
        return R.ok(strategyService.findGroupsByDestId(destId));
    }

    @GetMapping("/viewnumTop3")
    public R<List<Strategy>> viewnumTop3(Long destId) {
        return R.ok(strategyService.findViewnumTop3(destId));
    }

    @RequireLogin
    @GetMapping("/thumbnumIncr")
    public R<Boolean> thumbnumIncr(Long sid) {
        return R.ok(strategyService.thumbnumIncr(sid));
    }


    @GetMapping("/ranks")
    public R<JSONObject> ranks() {
        List<StrategyRank> chinaRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_CHINA);
        List<StrategyRank> abroadRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_CHINA);
        List<StrategyRank> hotRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_HOT);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("abroadRank", abroadRank);
        jsonObject.put("chinaRank", chinaRank);
        jsonObject.put("hotRank", hotRank);
        return R.ok(jsonObject);
    }

    @PostMapping("/uploadImg")
    public JSONObject uploadImg(MultipartFile upload) {
        JSONObject result = new JSONObject();
        if (upload == null) {
            result.put("uploaded", 0);
            JSONObject error = new JSONObject();
            error.put("message", "请选择要上传的文件！");
            result.put("error", error);
            return result;
        }
        String filename = upload.getOriginalFilename();
        String mainFilename = "";
        if (StringUtils.hasText(filename)) {
            mainFilename = filename.substring(0, filename.lastIndexOf(".")) + "_" + System.currentTimeMillis();
        } else {
            mainFilename = System.currentTimeMillis() + "";
        }
        // 返回阿里云可访问的地址
        String url = OssUtil.upload("images", mainFilename, upload);

        result.put("uploaded", 1);
        result.put("fileName", upload.getOriginalFilename());
        result.put("url", url);
        return result;
    }

    @PostMapping("/save")
    public R<?> save(Strategy strategy) {
        return R.ok(strategyService.save(strategy));
    }

    @PostMapping("/update")
    public R<?> update(Strategy strategy) {
        strategyService.updateById(strategy);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> delete(@PathVariable Long id) {
        strategyService.removeById(id);
        return R.ok();
    }
}
