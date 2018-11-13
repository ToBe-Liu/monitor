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

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *  参数校验 切面
 *  注意：
 *  1. {@link BeanValidatorAspect} 注解直接写在方法的实现上
 *  2. 使用此检验时，需要检验的参数必须封装在一个对象里 e.g.
 *     int deleteBatch(ParamsObject paramsObject);
 *     public class ParamsObject{
 *         @NotNull
 *         String name;
 *     }
 *@author LiuXingHai
 *@date 2018/7/9
 */
@Aspect
@Component
public class BeanValidatorAspect {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    private Logger logger = LoggerFactory.getLogger(BeanValidatorAspect.class);

    @Pointcut("@annotation(com.wkyc.monitor.aspect.BeanValidate)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = "";
        Object target = null;
        Method currentMethod = null;
        BeanValidate beanValidate = null;
        try {
            Signature sig = joinPoint.getSignature();
            MethodSignature msig = (MethodSignature) sig;
            target = joinPoint.getTarget();
            currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            methodName = currentMethod.getName();
            beanValidate = currentMethod.getAnnotation(BeanValidate.class);
        } catch (Exception e) {
            logger.error("无法获取方法名称:{}", e);
        }
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null){
                throw new ValidationException(methodName+"参数不能为null");
            }
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(arg,beanValidate.groups());
            if (CollectionUtils.isNotEmpty(constraintViolations)){
                List<String> errors= new ArrayList<>(constraintViolations.size());
                constraintViolations.forEach(constraintViolation -> {
                    String paramName = constraintViolation.getPropertyPath().toString();  // 获得校验的参数名称
                    String message = constraintViolation.getMessage();
                    errors.add(paramName + message);
                });
                logger.debug("{}.{}参数检验失败:{}",target.getClass().getCanonicalName(),methodName,JsonUtil.fromCollection(errors));
                throw new ValidationException(JsonUtil.fromCollection(errors));
            }

        }
        return joinPoint.proceed();
    }
}