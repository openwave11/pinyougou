package com.pinyougou.search.service;

import java.util.Map;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface ItemSearchService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public Map search(Map specMap);


}
