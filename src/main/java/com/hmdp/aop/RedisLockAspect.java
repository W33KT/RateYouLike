package com.hmdp.aop;

import com.hmdp.aop.annotation.RedisLock;
import com.hmdp.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author tankaiwen
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAspect {

    // private final RedisLockService redisLockService;
    private final RedissonClient redissonClient;
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final StandardEvaluationContext context = new StandardEvaluationContext();

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String key = parseKey(redisLock.key(), joinPoint);
        RLock lock = redissonClient.getLock(key);
        try {
            // boolean locked = redisLockService.tryLock(key, redisLock.timeout(), redisLock.unit());
            boolean locked = lock.tryLock(0, redisLock.timeout(), redisLock.unit());
            if (!locked) {
                throw new SystemException("Duplicate operation: " + key + "please try again later!");
            }

            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("redis lock error: {}", e.getMessage());
            throw new SystemException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    private String parseKey(String keySpEL, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        context.setVariables(IntStream.range(0, paramNames.length)
                .boxed()
                .collect(Collectors.toMap(i -> paramNames[i], i -> args[i])));

        Expression expression = parser.parseExpression(keySpEL);
        return expression.getValue(context, String.class);
    }
}
