package com.hmdp.vo.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class ShopTypeQueryRespVO {
    private Long id;

    private String name;

    private String icon;

    private Integer sort;
}
