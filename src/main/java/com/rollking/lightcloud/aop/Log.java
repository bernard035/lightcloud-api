/*CHECKSTYLE:OFF*/
package com.rollking.lightcloud.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
/*CHECKSTYLE:ON*/
/**
 * @author bernard @Component 该注解表示把类加入spring容器 @Aspect 该注解表示切面
 */
@Component
@Aspect
public class Log {

  private final Logger logger = LoggerFactory.getLogger(Log.class);

  /** 定义一个公共切点，代码复用 拦截 类下面的所有public方法 */
  @Pointcut(value = "execution(public * com.rollking.lightcloud.controller.HdfsController.*(..))")
  private void log() {}

  /** 方法执行前切入 */
  @Before(value = "log()")
  public void before(JoinPoint joinPoint) {
    ServletRequestAttributes sa =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    assert sa != null;
    HttpServletRequest request = sa.getRequest();
    // 获取用户访问的url
    logger.info("url={}", request.getRequestURL());
    // 获取用户访问的方式，get/post
    logger.info("method={}", request.getMethod());
    // 获取的ip
    logger.info("ip={}", request.getRemoteAddr());
    // 获取用户访问的是哪个方法
    logger.info(
        "class_method={}",
        joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    // 获取方法的参数
    logger.info("args={}", joinPoint.getArgs());
  }

  /**
   * 打印返回的内容
   *
   * @param object 返回的内容
   */
  @AfterReturning(value = "log()", returning = "object")
  public void afterReturn(Object object) {
    logger.info("object={}", object);
  }
}
