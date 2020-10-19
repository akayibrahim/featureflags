package com.iakay.featureflags.aspect;

import com.iakay.featureflags.annotation.FeatureFlags;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Method;

/**
 * Created by ibrahim akay on 2020-10-16.
 */
@Aspect
@Configuration
public class FeatureFlagsAspect {

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String CAN_ONLY_BE_TRUE_FALSE = " can only be true/false!";
    @Autowired
    private Environment environment;

    @Around(value = "@annotation(com.iakay.featureflags.annotation.FeatureFlags)")
    public Object beforeMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        FeatureFlags featureFlags = getFeatureFlags(joinPoint);
        String value = getFeatureFlagParameter(featureFlags);
        checkParameterPattern(featureFlags, value);
        boolean enabled = Boolean.valueOf(value);
        if (enabled) {
            return joinPoint.proceed();
        }
        return null;
    }

    private void checkParameterPattern(FeatureFlags featureFlags, String value) throws PropertyException {
        if (!TRUE.equalsIgnoreCase(value) && !FALSE.equalsIgnoreCase(value)) {
            throw new PropertyException(featureFlags.enabled() + CAN_ONLY_BE_TRUE_FALSE);
        }
    }

    private String getFeatureFlagParameter(FeatureFlags featureFlags) {
        return environment.getProperty(featureFlags.enabled());
    }

    private FeatureFlags getFeatureFlags(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(FeatureFlags.class);
    }
}
