package com.pinyougou.sellergoods.service;

//import com.github.pagehelper.PageInfo;

import com.pinyougou.pojo.TbBrand;
import entity.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 *
 * @author Administrator
 */
public interface BrandService {

    /**
     * 查找所有并分页
     * @param pageNum
     * @param pageSize
     * @return PageInfo<TbBrand>
     */


    public PageInfo<TbBrand> findAll(Integer pageNum, Integer pageSize);

    public void add(TbBrand tbBrand);

    /**
     * update
     * @param tbBrand
     * @return
     */
    public void update(TbBrand tbBrand);

    /**
     * 查找一个
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);

    public void delete(Long[] ids);

    public  PageInfo<TbBrand> findAll(Integer pageNum, Integer pageSize, TbBrand tbBrand);

    List<Map<String, Object>> selectOptionList();
}
