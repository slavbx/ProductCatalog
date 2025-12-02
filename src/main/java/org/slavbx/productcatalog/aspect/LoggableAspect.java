package org.slavbx.productcatalog.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Аспект для автоматического логирования действий
 */
@Aspect
public class LoggableAspect {
    @Pointcut("within(@org.slavbx.productcatalog.annotation.Loggable *) && execution(* * (..))")
    public void annotatedByLoggable() {}

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method = " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        System.out.println("Execution of method = " + proceedingJoinPoint.getSignature() +
                " finished. execution time is " + end + " ms.");
        return result;
    }

}
