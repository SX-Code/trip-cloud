package com.swx.article.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.Strategy;
import com.swx.article.service.StrategyService;
import com.swx.article.utils.OssUtil;
import com.swx.common.core.utils.R;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/strategies")
public class StrategyController {

    private final StrategyService strategyService;

    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    @GetMapping("/query")
    public R<Page<Strategy>> pageList(Page<Strategy> page) {
        return R.ok(strategyService.page(page));
    }

    @GetMapping("/detail")
    public R<Strategy> getById(Long id) {
        return R.ok(strategyService.getById(id));
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
