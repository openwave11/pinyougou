package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbOrderItem;
import lombok.Data;

import java.util.List;

@Data
public class Cart {

    private String sellerId;//商家 ID
    private String sellerName;//商家名称
    private List<TbOrderItem> orderItemList;//购物车明细

}
