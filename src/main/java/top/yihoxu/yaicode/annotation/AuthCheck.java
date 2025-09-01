package top.yihoxu.yaicode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yihoxu
 * @date 2025/9/1  10:25
 * @description 权限注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须拥有某个角色
     * @return
     */
    String mustRole() default "";
}
