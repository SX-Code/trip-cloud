package com.swx.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.*;
import com.swx.article.mapper.StrategyContentMapper;
import com.swx.article.mapper.StrategyMapper;
import com.swx.article.service.DestinationService;
import com.swx.article.service.StrategyCatalogService;
import com.swx.article.service.StrategyService;
import com.swx.article.service.StrategyThemeService;
import com.swx.article.utils.OssUtil;
import com.swx.common.core.exception.BizException;
import com.swx.common.core.utils.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {

    private final StrategyCatalogService strategyCatalogService;
    private final DestinationService destinationService;
    private final StrategyThemeService strategyThemeService;
    private final StrategyContentMapper strategyContentMapper;

    public StrategyServiceImpl(StrategyCatalogService strategyCatalogService, DestinationService destinationService, StrategyThemeService strategyThemeService, StrategyContentMapper strategyContentMapper) {
        this.strategyCatalogService = strategyCatalogService;
        this.destinationService = destinationService;
        this.strategyThemeService = strategyThemeService;
        this.strategyContentMapper = strategyContentMapper;
    }

    @Override
    public Strategy getById(Serializable id) {
        Strategy strategy = super.getById(id);
        StrategyContent strategyContent = strategyContentMapper.selectById(id);
        strategy.setContent(strategyContent);
        return strategy;
    }

    /**
     * 保存攻略
     *
     * @param strategy 攻略参数对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Strategy strategy) {
        // 同一类，事务调用非事务，事务都生效
        return doSaveOrUpdate(strategy);
    }

    /**
     * 更新攻略
     *
     * @param strategy 攻略参数对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(Strategy strategy) {
        return doSaveOrUpdate(strategy);
    }

    private Boolean doSaveOrUpdate(Strategy strategy) {
        // 有封面数据且是base64时才做上传处理
        if (StringUtils.hasText(strategy.getCoverUrl()) && !strategy.getCoverUrl().startsWith("http")) {
            // 上传封面图片，得到URL后，重新设置到cover属性中
            String fiilename = UUID.randomUUID().toString();
            // 可以解析base64得到格式，这里直接写死
            String url = OssUtil.uploadImgByBase64("images/strategies", fiilename + ".jpg", strategy.getCoverUrl());
            strategy.setCoverUrl(url);

        }
        // 补充分类名称
        StrategyCatalog catalog = strategyCatalogService.getById(strategy.getCatalogId());
        if (catalog == null) {
            throw new BizException(R.CODE_ERROR_PARAM, "分类参数异常");
        }
        strategy.setCatalogName(catalog.getName());
        // 根据分类目的地id/名称，设置到目的地中
        strategy.setDestId(catalog.getDestId());
        strategy.setDestName(catalog.getDestName());
        // 判断目的地是否是国外
        List<Destination> toasts = destinationService.toasts(catalog.getDestId());
        if (toasts.get(0).getId() == 1) {
            strategy.setIsabroad(Strategy.ABROAD_NO);
        } else {
            strategy.setIsabroad(Strategy.ABROAD_YES);
        }
        // 查询主题，填充主题名称
        StrategyTheme theme = strategyThemeService.getById(strategy.getThemeId());
        if (theme == null) {
            throw new BizException(R.CODE_ERROR_PARAM, "主题参数异常");
        }
        strategy.setThemeName(theme.getName());

        if (strategy.getId() == null) {
            // 设置创建时间
            strategy.setCreateTime(new Date());
            // 设置各种数量为0
            strategy.setViewnum(0);
            strategy.setSharenum(0);
            strategy.setThumbsupnum(0);
            strategy.setReplynum(0);
            strategy.setFavornum(0);
            // 重新设置状态
            strategy.setState(Strategy.STATE_NORMAL);
            // 保存攻略对象，得到自增ID
            boolean save = super.save(strategy);
            // 将攻略ID设置到内容对象中，保存内容对象
            StrategyContent content = strategy.getContent();
            content.setId(strategy.getId());
            return save && strategyContentMapper.insert(content) > 0;
        }
        // 更新操作
        boolean ret = super.updateById(strategy);
        StrategyContent content = strategy.getContent();
        content.setId(strategy.getId());
        int row = strategyContentMapper.updateById(content);
        return ret && row > 0;
    }
}
