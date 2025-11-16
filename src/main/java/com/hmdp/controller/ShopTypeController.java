package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.facade.ShopFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Resource
    private ShopFacade shopFacade;

    @GetMapping("list")
    public Result queryTypeList() {
        return shopFacade.listShopTypes();
    }
}
