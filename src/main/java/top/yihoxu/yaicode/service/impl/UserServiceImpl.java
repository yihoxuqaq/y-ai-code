package top.yihoxu.yaicode.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.DigestUtils;
import top.yihoxu.yaicode.constant.UserConstant;
import top.yihoxu.yaicode.exception.BusinessException;
import top.yihoxu.yaicode.exception.ErrorCode;
import top.yihoxu.yaicode.model.dto.user.UserQueryRequest;
import top.yihoxu.yaicode.model.entity.User;
import top.yihoxu.yaicode.mapper.UserMapper;
import top.yihoxu.yaicode.model.vo.user.LoginUserVO;
import top.yihoxu.yaicode.model.vo.user.UserVO;
import top.yihoxu.yaicode.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表 服务层实现。
 *
 * @author yihoxu
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1、参数校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        //2、检查重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        //3、插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(getEncryptPassword(userPassword));
        user.setUserName("无名");
        user.setUserProfile("到此一游");
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "注册失败，数据库错误！");
        }
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1、校验参数
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        //2、查询用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);

        queryWrapper.eq("userPassword", getEncryptPassword(userPassword));
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }
        //3、存入登录状态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user==null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user,userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)){
            return new ArrayList<>();
        }
        List<UserVO> userVOList = userList.stream()
                .map(i -> this.getUserVO(i))
                .collect(Collectors.toList());
        return userVOList;
    }


    @Override
    public User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (user == null || user.getId() < 0) {
            return null;
        }
        User currentUser = this.getById(user.getId());
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        //1、判断是否登录
        Object attribute = session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (attribute == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //2、移除登录状态
        session.removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        return QueryWrapper.create()
                .eq("id",id)
                .eq("userRole",userRole)
                .like("userAccount",userAccount)
                .like("userName",userName)
                .like("userProfile",userProfile)
                .orderBy(sortField,"ascend".equals(sortOrder));
    }

    @Override
    public String getEncryptPassword(String userPassword) {
        final String SALT = "yihoxu";

        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
}
