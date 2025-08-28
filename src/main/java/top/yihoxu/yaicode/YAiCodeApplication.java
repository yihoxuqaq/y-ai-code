package top.yihoxu.yaicode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
/**
 * exposeProxy=true
 * 通过spring AOP提供对当前代理对象的访问，使得可以在业务逻辑中访问
 * 到当前的代理对象。可以在方法执行时通过AopContext.currentProxy()获取当前的代理对象
 */
public class YAiCodeApplication {

    public static void main(String[] args) {
        System.out.println("http://localhost:8123/api/doc.html#/home");
        SpringApplication.run(YAiCodeApplication.class, args);
    }

}
