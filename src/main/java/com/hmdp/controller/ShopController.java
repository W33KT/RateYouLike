package com.hmdp.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.facade.ShopFacade;
import com.hmdp.dao.IShopDAO;
import com.hmdp.constants.SystemConstants;
import com.hmdp.vo.request.ShopUpdateReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author tankaiwen
 */
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    public IShopDAO shopService;
    @Autowired
    private ShopFacade shopFacade;

    /**
     * query shop info by id
     * @param id shop id
     * @return shop info
     */
    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return shopFacade.queryShopById(id);
    }

    /**
     * add new shop info
     * @param reqVO shop info to insert
     * @return shop id
     */
    @PostMapping
    public Result saveShop(@RequestBody ShopUpdateReqVO reqVO) {
        return shopFacade.saveShop(reqVO);
    }

    /**
     * update shop info
     * @param reqVO shop info to update
     * @return 无
     */
    @PutMapping
    public Result updateShop(@RequestBody ShopUpdateReqVO reqVO) {
        return shopFacade.updateShop(reqVO);
    }

    /**
     * 根据商铺类型分页查询商铺信息
     * @param typeId 商铺类型
     * @param current 页码
     * @return 商铺列表
     */
    @GetMapping("/of/type")
    public Result queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        // 根据类型分页查询
        Page<Shop> page = shopService.query()
                .eq("type_id", typeId)
                .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
        // 返回数据
        return Result.ok(page.getRecords());
    }

    /**
     * 根据商铺名称关键字分页查询商铺信息
     * @param name 商铺名称关键字
     * @param current 页码
     * @return 商铺列表
     */
    @GetMapping("/of/name")
    public Result queryShopByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        // 根据类型分页查询
        Page<Shop> page = shopService.query()
                .like(StrUtil.isNotBlank(name), "name", name)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 返回数据
        return Result.ok(page.getRecords());
    }
}
