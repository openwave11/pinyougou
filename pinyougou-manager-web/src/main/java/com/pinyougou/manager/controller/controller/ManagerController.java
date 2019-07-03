package com.pinyougou.manager.controller.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageInfo;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Controller

@RequestMapping("brand")
public class ManagerController {

    @Reference
    private BrandService brandService;


    /**
     * @return com.github.pagehelper.PageInfo<com.pinyougou.pojo.TbBrand>
     * @Description 查询所有
     * @Date 19:34 2019-06-27
     * @Author Administrator
     * @Param [pageNum, pageSize]
     */
    @RequestMapping("/findAll.do")
    public PageInfo<TbBrand> findAllBrand(@RequestParam(name = "page", defaultValue = "1") Integer pageNum, @RequestParam(name = "rows", defaultValue = "10") Integer pageSize) {

        PageInfo<TbBrand> pageInfo = brandService.findAll(pageNum, pageSize);

        return pageInfo;
    }

    /**
     * @return entity.Result
     * @Description 修改记录
     * @Date 19:33 2019-06-27
     * @Author Administrator
     * @Param [tbBrand]
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody TbBrand tbBrand) {
        try {
            brandService.update(tbBrand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    @RequestMapping("/add.do")
    /**
     * @Description 添加品牌
     * @Date 19:33 2019-06-27
     * @Author Administrator
     * @Param [tbBrand]
     * @return entity.Result
     */
    public Result add(@RequestBody TbBrand tbBrand) {
        try {
            brandService.add(tbBrand);
            Result result = new Result(true, "添加成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("/findOne.do")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    @RequestMapping("/delete.do")
    public Result delete(Long[] ids) {
        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/search.do")
    public PageInfo<TbBrand> findAllBrand(@RequestParam(name = "page", defaultValue = "1") Integer pageNum, @RequestParam(name = "rows", defaultValue = "10") Integer pageSize,@RequestBody TbBrand tbBrand) {

        PageInfo<TbBrand> pageInfo = brandService.findAll(pageNum, pageSize,tbBrand);

        return pageInfo;
    }

}


