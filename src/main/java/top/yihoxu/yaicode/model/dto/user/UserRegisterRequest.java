package top.yihoxu.yaicode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yihoxu
 * @date 2025/8/29  14:52
 * @description 用户注册
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -3958893177830766439L;
    /**
     * 用户账号
     */
    private String userAccount;


    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;


}
