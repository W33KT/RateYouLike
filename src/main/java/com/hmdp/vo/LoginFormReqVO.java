package com.hmdp.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginFormReqVO {
    @Schema(description = "phone number", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;
    @Schema(description = "verify code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;
    @Schema(description = "password", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String password;
}
