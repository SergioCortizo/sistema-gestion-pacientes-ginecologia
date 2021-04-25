package es.udc.fic.ginecologia.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingServiceAspect {
	
	private static final Logger logger = LogManager.getLogger(LoggingServiceAspect.class);

	@Before("execution(* es.udc.fic.ginecologia..service..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Log before in: " + joinPoint.getSignature().getName());
    }
	
	@AfterReturning(pointcut = "execution(* es.udc.fic.ginecologia..service..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("logAfterReturning");
        logger.info("Log after in: " + joinPoint.getSignature().getName());
        logger.info("- And value returned is: " + result.toString());
    }
	
	@AfterThrowing(pointcut = "execution(* es.udc.fic.ginecologia..service..*(..))", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("logAfterThrowing");
        logger.error("Log in: " + joinPoint.getSignature().getName());
        logger.error("- And thrown exception is: " + e);
    }
}
