package com.business.controller;

import com.business.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PressureTestController
 * @Description 压力测试控制器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午2:05
 */

@Api(value = "/pressure",description = "压力测试控制器")
@RestController
    @RequestMapping("/pressure")
public class PressureTestController {

    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ApiOperation(value = "测试")
    public String test(){
        List<Map<String,Object>> maps =userService.queryUserAll();
        for (Map<String, Object> map : maps) {
            System.out.println(map.get("username"));
        }
        return "";
    }
}
