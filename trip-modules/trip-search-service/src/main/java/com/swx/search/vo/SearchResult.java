package com.swx.search.vo;

import com.swx.article.dto.StrategyDTO;
import com.swx.article.dto.TravelDTO;
import com.swx.user.dto.UserInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchResult {
    private List<StrategyDTO> strategies = new ArrayList<>();
    private List<TravelDTO> travels = new ArrayList<>();
    private List<UserInfoDTO> users = new ArrayList<>();
    private Long total = 0L;
}
