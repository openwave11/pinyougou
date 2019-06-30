package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		//添加specification
		TbSpecification tbSpecification = specification.getTbSpecification();
		specificationMapper.insert(tbSpecification);

		//添加获取specId
		Long specId = tbSpecification.getId();

		List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
		for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
			tbSpecificationOption.setSpecId(specId);
			specificationOptionMapper.insert(tbSpecificationOption);
		}

	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		//先修改tbspecification
		TbSpecification tbSpecification = specification.getTbSpecification();
		specificationMapper.updateByPrimaryKey(tbSpecification);

		//删除specificationoption中所有数据

		TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
		criteria.andSpecIdEqualTo(tbSpecification.getId());

		specificationOptionMapper.deleteByExample(tbSpecificationOptionExample);

//		List<TbSpecificationOption> specificationOptionList = specification.getTbSpecificationOptionList();
//		for (TbSpecificationOption tbSpecificationOption : specificationOptionList) {
//			specificationOptionMapper.deleteByPrimaryKey(tbSpecificationOption.getId());
//		}
		//增加specificationoption中数据
		List<TbSpecificationOption> specificationOptionList = specification.getTbSpecificationOptionList();

		for (TbSpecificationOption tbSpecificationOption : specificationOptionList) {
			tbSpecificationOption.setSpecId(tbSpecification.getId());
			specificationOptionMapper.insert(tbSpecificationOption);
		}


	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
		criteria.andSpecIdEqualTo(id);

		//查询tbSpecificationOptionExample
		List<TbSpecificationOption> tbSpecificationOptions = specificationOptionMapper.selectByExample(tbSpecificationOptionExample);

		Specification specification = new Specification();
		specification.setTbSpecification(tbSpecification);
		specification.setTbSpecificationOptionList(tbSpecificationOptions);

		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		for(Long id:ids){
			//删除specification
			specificationMapper.deleteByPrimaryKey(id);
			criteria.andSpecIdEqualTo(id);
			//删除specificationOption
			specificationOptionMapper.deleteByExample(example);
		}
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
