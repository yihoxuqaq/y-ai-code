package top.yihoxu.yaicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import top.yihoxu.yaicode.model.dto.user.UserQueryRequest;
import top.yihoxu.yaicode.model.entity.User;
import top.yihoxu.yaicode.model.vo.user.LoginUserVO;
import top.yihoxu.yaicode.model.vo.user.UserVO;

import java.util.List;

/**
 * 用户表 服务层。
 *
 * @author yihoxu
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);


    /**
     * 用户登录
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 登录用户信息（脱敏）
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏用户信息列表
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);


    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 用户退出
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 拼接查询条件
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);


    /**
     * 密码加密
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);


}
