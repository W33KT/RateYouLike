package com.hmdp.DO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.entity.Shop;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class ShopQueryDO {
    private Long id;

    private Integer typeId;

    private String name;

    private Page<Shop> page;
}
