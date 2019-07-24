package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Goods implements Serializable {
    private TbGoods Goods;
    private TbGoodsDesc goodsDesc;
    private List<TbItem> itemList;

}
