package com.hmdp.facade;

import com.hmdp.dto.Result;
import com.hmdp.dto.ShopDTO;
import com.hmdp.dto.ShopTypeDTO;
import com.hmdp.service.ShopService;
import com.hmdp.vo.request.ShopUpdateReqVO;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author tankaiwen
 */
@Service
public class ShopFacade {
    @Resource
    public ShopService shopService;

    public Result queryShopById(Long id) {
        ShopDTO shopDTO = shopService.queryShopById(id);

        return Result.ok(Objects.isNull(shopDTO) ? null : shopDTO.convertToVO());
    }

    public Result listShopTypes() {
        List<ShopTypeDTO> shopTypes = shopService.listShopTypes();

        return Result.ok(ListUtils.emptyIfNull(shopTypes).stream().map(ShopTypeDTO::convertToVO).toList());
    }

    public Result saveShop(ShopUpdateReqVO reqVO) {
        ShopDTO shopDTO = ShopDTO.convertFromShopUpdateReqVO(reqVO);

        Long id = shopService.saveNewShop(shopDTO);

        return Result.ok(id);
    }

    public Result updateShop(ShopUpdateReqVO reqVO) {
        ShopDTO shopDTO = ShopDTO.convertFromShopUpdateReqVO(reqVO);

        shopService.updateShop(shopDTO);

        return Result.ok();
    }
}
