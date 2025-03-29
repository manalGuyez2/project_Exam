package uni.master.backend.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Log avant l'exécution d'une méthode (pour une insertion ou une mise à jour)
    @Before("execution(* uni.master.backend.service.*.add*(..)) || execution(* uni.master.backend.service.*.update*(..))")
    public void logBeforeUpdate(JoinPoint joinPoint) {
        logger.info("Execution de la méthode: " + joinPoint.getSignature().getName() + " avec les arguments : " + joinPoint.getArgs());
    }

    // Log après le succès d'une méthode (pour une suppression)
    @AfterReturning(value = "execution(* uni.master.backend.service.*.delete*(..))", returning = "result")
    public void logAfterDelete(JoinPoint joinPoint, Object result) {
        logger.info("Suppression réussie dans la méthode: " + joinPoint.getSignature().getName() + " avec les arguments : " + joinPoint.getArgs());
    }

    // Log après une exception dans une méthode (gestion des erreurs)
    @AfterThrowing(value = "execution(* uni.master.backend.service.*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Erreur dans la méthode: " + joinPoint.getSignature().getName() + " avec les arguments : " + joinPoint.getArgs(), exception);
    }
}
