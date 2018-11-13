package com.wkyc.monitor.aspect;

import com.zjlp.face.util.json.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *  参数校验 切面
 *  注意：
 *  1. {@link ParamsValidatorAspect} 注解直接写在方法的实现上
 *  2. @NotNull 等JSR303检验注解需要写在接口方法上 e.g.
 *     int deleteBatch(@NotNull List<String> globalTxIds,@NotNull String error);
 *@author LiuXingHai
 *@date 2018/7/9
 */
@Aspect
@Component
public class ParamsValidatorAspect {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator validator = factory.getValidator().forExecutables();
    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Logger logger = LoggerFactory.getLogger(ParamsValidatorAspect.class);

    @Pointcut("@annotation(com.wkyc.monitor.aspect.ParamsValidate)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = "";
        Object target = null;
        Method currentMethod = null;

        try {
            Signature sig = joinPoint.getSignature();
            MethodSignature msig = (MethodSignature) sig;
            target = joinPoint.getTarget();
            currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            methodName = currentMethod.getName();
        } catch (Exception e) {
            logger.error("无法获取方法名称:{}", e);
        }


        Object[] args = joinPoint.getArgs();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validateParameters(target,currentMethod,args);

        if (CollectionUtils.isNotEmpty(constraintViolations)){
            String [] parameterNames = parameterNameDiscoverer.getParameterNames(currentMethod); // 获得方法的参数名称
            List<String> errors= new ArrayList<>(constraintViolations.size());
            constraintViolations.forEach(constraintViolation -> {
                PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();  // 获得校验的参数路径信息
                int paramIndex = pathImpl.getLeafNode().getParameterIndex(); // 获得校验的参数位置
                String paramName = parameterNames[paramIndex];  // 获得校验的参数名称
                String message = constraintViolation.getMessage();
                errors.add(paramName + message);
            });
            logger.debug("{}参数检验失败:{}",methodName,JsonUtil.fromCollection(errors));
            throw new ValidationException(JsonUtil.fromCollection(errors));
        }

        return joinPoint.proceed();
    }
}