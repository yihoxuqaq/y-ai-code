package top.yihoxu.yaicode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yihoxu
 * @date 2025/9/1  10:45
 * @description 创建用户
 */
@Data
public class UserAddRequest implements Serializable {

    private static final long serialVersionUID = -4537280301109381670L;
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user,admin
     */

    private String userRole;

}
