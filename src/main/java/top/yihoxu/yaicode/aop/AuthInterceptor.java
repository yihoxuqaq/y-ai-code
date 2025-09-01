package top.yihoxu.yaicode.aop;

import top.yihoxu.yaicode.annotation.AuthCheck;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.yihoxu.yaicode.exception.BusinessException;
import top.yihoxu.yaicode.exception.ErrorCode;
import top.yihoxu.yaicode.model.entity.User;
import top.yihoxu.yaicode.model.enums.UserRoleEnum;
import top.yihoxu.yaicode.service.UserService;

/**
 * @author yihoxu
 * @date 2025/9/1  10:29
 * @description aop权限
 */

@Component
@Aspect
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request=((ServletRequestAttributes) requestAttributes).getRequest();
        //当前登录用
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //不需要权限，放行
        if (mustRoleEnum==null){
            return  joinPoint.proceed();
        }
        //以下为：必须有该权限才能通过
        //获取当前用户具有的权限
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        //没有权限，拒绝
        if (userRoleEnum==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //要求必须有管理员权限,但是用户没有管理员权限，拒绝
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum)&&!UserRoleEnum.ADMIN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //通过校验权限，放行
        return joinPoint.proceed();

    }
}
