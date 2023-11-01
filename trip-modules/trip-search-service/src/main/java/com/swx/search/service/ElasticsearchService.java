package com.swx.search.service;

import com.swx.common.core.qo.QueryObject;
import com.swx.search.parser.ElasticsearchTypeParser;
import org.springframework.data.domain.Page;

public interface ElasticsearchService {

    /**
     * 新增方法
     * @param entity ES 实体对象
     */
    public void save(Object entity);

    /**
     * 批量新增方法
     * @param iterable ES 实体对象集合
     */
    public void save(Iterable<?> iterable);

    /**
     * 删除方法
     * @param id 主键
     * @param clazz 实体类型
     */
    public void deleteById(String id, Class<?> clazz);

    /**
     * 通用的高亮分页搜索接口
     *
     * @param esclz  ES 模型字段字节码对象 => ES 查询的对象
     * @param dtoclz domain 字节码对象 => 最终希望返回的对象
     * @param qo     查询对象 => 封装分页参数
     * @param parser 解析器对象 => 利用 ES 对象中的 id 属性，从数据库中查到对应完整的模型对象，返回最终结果
     * @param fields 需要进行高亮查询的字段
     * @param <T>
     * @return 高亮分页数据对象
     */
    <T> Page<T> searchWithHighlight(Class<?> esclz, Class<T> dtoclz, QueryObject qo, ElasticsearchTypeParser<T> parser, String... fields);
}
