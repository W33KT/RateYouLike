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

    public UserDTO4RedisString convertToDTO4Redis() {
        return new UserDTO4RedisString()
                .setId(id.toString())
                .setNickName(nickName)
                .setIcon(icon);
    }

    @Data
    @Accessors(chain = true)
    public static class UserDTO4RedisString {
        private String id;
        private String nickName;
        private String icon;

        public UserDTO convertToDTO() {
            return new UserDTO()
                    .setId(Long.parseLong(id))
                    .setNickName(nickName)
                    .setIcon(icon);
        }
    }
}
