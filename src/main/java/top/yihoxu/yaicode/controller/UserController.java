package top.yihoxu.yaicode.controller;


import top.yihoxu.yaicode.annotation.AuthCheck;
import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yihoxu.yaicode.common.BaseResponse;
import top.yihoxu.yaicode.common.DeleteRequest;
import top.yihoxu.yaicode.common.ResultUtils;
import top.yihoxu.yaicode.constant.UserConstant;
import top.yihoxu.yaicode.exception.BusinessException;
import top.yihoxu.yaicode.exception.ErrorCode;
import top.yihoxu.yaicode.exception.ThrowUtils;
import top.yihoxu.yaicode.model.dto.user.*;
import top.yihoxu.yaicode.model.entity.User;
import top.yihoxu.yaicode.model.vo.user.LoginUserVO;
import top.yihoxu.yaicode.model.vo.user.UserVO;
import top.yihoxu.yaicode.service.UserService;

import java.util.List;

/**
 * 用户表 控制层。
 *
 * @author yihoxu
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册用户
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long userId = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(userId);
    }


    /**
     * 登录用户
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前登录用户
     */

    @GetMapping("/get/login/vo")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {

        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    /**
     * 用户退出
     */
    @GetMapping("logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {

        boolean result = userService.userLogout(request);

        return ResultUtils.success(result);
    }

    /**
     * 添加用户
     *
     * @param userAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        user.setUserPassword(userService.getEncryptPassword("12345678"));
        boolean result = userService.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建用户失败");
        }
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据id获取用户（仅管理员）
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据id获取用户（脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(deleteRequest.getId());
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户删除失败");
        }
        return ResultUtils.success(result);
    }


    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();

        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户更新失败");
        }
        return ResultUtils.success(result);
    }

    /**
     * 分页获取用户封装列表(仅管理员)
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        int pageSize = userQueryRequest.getPageSize();
        int current = userQueryRequest.getCurrent();
        Page<User> userPage = userService.page(Page.of(current, pageSize), userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> resultPage = new Page<>(current, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        resultPage.setRecords(userVOList);
        return ResultUtils.success(resultPage);
    }


}
