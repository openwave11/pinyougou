package com.pinyougou.page.service;

/**
 * 服务层接口
 * @author Administrator
 *
 */

public interface ItemPageService {

	/**
	 * 返回全部列表
	 *
	 * @return
	 */
	public boolean getItemHtml(Long goodsId);

	public boolean deleItemHtml(Long[] goodsIds);
}