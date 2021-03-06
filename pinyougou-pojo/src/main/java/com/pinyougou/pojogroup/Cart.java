package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbOrderItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Cart implements Serializable {
    //商家 ID
    private String sellerId;
    //商家名称
    private String sellerName;
    //购物车明细
    private List<TbOrderItem> orderItemList;

}
