package com.example.final_augues.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ExecutionTimeAspect {

    private long startTime;

    //    @Pointcut("execution(* com.example.final_augues..*.*(..))")
    @Pointcut("@annotation(com.example.final_augues.aspect.Log)")
    public void pointCut() {
    }

    @Before("pointCut()") // 定义切入点表达式，指定需要测量的方法
    public void beforeExecution() {
        System.out.println("start.....................");
        startTime = System.currentTimeMillis();
    }

    @After("pointCut()") // 定义切入点表达式，指定需要测量的方法
    public void afterExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName(); // 获取方法名
        Object[] args = joinPoint.getArgs(); // 获取方法参数
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("方法执行时间：" + duration + "纳秒，methodName：" + methodName + ",args:" + Arrays.toString(args));
    }
}
