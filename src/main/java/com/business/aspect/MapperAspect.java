package com.business.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * @ClassName MapperAspect
 * @Description 使用切面来监控mapper每条sql执行速度
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/17 下午3:23
 */
@Aspect
@Slf4j
@Component
public class MapperAspect {

    /**
     * 定义切入点
     */
    @Pointcut("execution(* com.business.mapper.mysql.*Mapper.*())")
    public void pointcutMethod(){
    }

    /**
     * 方法执行之前
     */
    @Before("pointcutMethod()")
    public void beforeMethod(){
    }

    /**
     * 方法执行之后
     */
    @AfterReturning("pointcutMethod()")
    public void afterMethod(){
    }



    /**
     * 增强环绕
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointcutMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object obj = pjp.proceed();
        long end = System.currentTimeMillis();

        log.info("调用Mapper方法：{}，\n参数：{}，\n耗时：{}毫秒",
            pjp.getSignature().toString(), Arrays.toString(pjp.getArgs())
            , (end - begin));
        return obj;
    }


}
