package org.mrchi;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Aspect
public class LogAspects {
    @Pointcut("execution(public int org.mrchi.Calculator.*(..))")//表示切入org.mrchi.Calculator下面所有返回类型为int类型的方法
    public void pointCut() {
    }

    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println("除法运行，参数列表是:" + Arrays.toString(args));
    }

    @After("pointCut()")
    public void logEnd() {
        System.out.println("除法结束...");
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {
        System.out.println("除法正常返回，运行结果是:{" + result + "}");
    }

    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        System.out.println("运行异常， 异常信息是:{" + exception + "}");
    }

    @Around("pointCut()")
    public Object Around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("@Around: 执行目标方法之前...");
        Object obj = proceedingJoinPoint.proceed();//相当于开始调用目标方法
        System.out.println("@Around: 执行目标方法之后...");
        return obj;
    }
}