package top.yihoxu.yaicode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yihoxu
 * @date 2025/8/28  10:50
 * @description 测试接口
 */

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public String healthCheck(){
        return "ok";
    }
}
