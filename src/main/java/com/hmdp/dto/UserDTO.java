package com.hmdp.dto;

import com.hmdp.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class UserDTO {
    private Long id;
    private String nickName;
    private String icon;

    public static UserDTO convertFromUser(User user) {
        if (Objects.isNull( user)) {
            return null;
        }
        return new UserDTO()
                .setId(user.getId())
                .setNickName(user.getNickName())
                .setIcon(user.getIcon());
    }
}
