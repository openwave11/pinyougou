package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

//import com.github.pagehelper.PageInfo;

/**
 * BrandServiceImpl
 *
 * @author 11
 */
@Service(timeout = 5000,retries = 0)
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * findAll+分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<TbBrand> findAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TbBrand> list = brandMapper.selectByExample(null);
        PageInfo<TbBrand> info = new PageInfo<TbBrand>(list);
        return info;
    }

    /**
     * add
     *
     * @param tbBrand
     * @return
     */
    @Override
    public void add(TbBrand tbBrand) {

        brandMapper.insert(tbBrand);


    }

    /**
     * 修改
     * @param tbBrand
     */
    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
     * 修改之前回显。查询一个
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        TbBrand tbBrand = brandMapper.selectByPrimaryKey(id);
        return tbBrand;
    }

    /**
     * @Description
     * @Date 21:21 2019-06-28
     * @Author Administrator
     * @Param [ids]
     * @return void
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    /**
     * @Description 条件查询+分页
     * @Date 21:21 2019-06-28
     * @Author Administrator
     * @Param [pageNum, pageSize, tbBrand]
     * @return entity.PageInfo<com.pinyougou.pojo.TbBrand>
     */
    public PageInfo<TbBrand> findAll(Integer pageNum, Integer pageSize, TbBrand tbBrand) {
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();

        if (tbBrand != null) {
            if (tbBrand.getName() != null && tbBrand.getName().length() > 0) {
                criteria.andNameLike("%" + tbBrand.getName() + "%");
            }
            if (tbBrand.getFirstChar() != null && tbBrand.getFirstChar().length() > 0) {
                /*criteria.andFirstCharLike("%" + tbBrand.getFirstChar() + "%");*/
                criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        List<TbBrand> tbBrandList = brandMapper.selectByExample(example);

        PageInfo<TbBrand> pageInfo = new PageInfo<TbBrand>(tbBrandList);
        return pageInfo;
    }
    @Override
    public List<Map<String, Object>> selectOptionList() {
        return brandMapper.selectOptionList();
    }

}
