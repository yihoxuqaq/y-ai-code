package top.yihoxu.yaicode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yihoxu
 * @date 2025/8/30  01:02
 * @description 用户登录
 */
@Data
public class UserLoginRequest  implements Serializable {


    private static final long serialVersionUID = 8538682623354357689L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
