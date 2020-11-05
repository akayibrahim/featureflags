package io.github.akayibrahim.featureflags.aspect;

import io.github.akayibrahim.featureflags.annotation.FeatureFlag;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Method;

/**
 * Created by ibrahim akay on 2020-10-16.
 */
@Aspect
@Configuration
@Order(0)
public class FeatureFlags {
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String CAN_ONLY_BE_TRUE_FALSE = " can only be true/false!";
    private Environment environment;

    @Autowired
    public FeatureFlags(Environment environment) {
        this.environment = environment;
    }

    @Around(value = "@annotation(io.github.akayibrahim.featureflags.annotation.FeatureFlag)")
    public Object beforeMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        FeatureFlag featureFlag = getFeatureFlag(joinPoint);
        String value = getFeatureFlagParameter(featureFlag);
        checkParameterPattern(featureFlag, value);
        boolean enabled = Boolean.valueOf(value);
        if (enabled) {
            return joinPoint.proceed();
        }
        return null;
    }

    private void checkParameterPattern(FeatureFlag featureFlag, String value) throws PropertyException {
        if (!TRUE.equalsIgnoreCase(value) && !FALSE.equalsIgnoreCase(value)) {
            throw new PropertyException(featureFlag.enabled() + CAN_ONLY_BE_TRUE_FALSE);
        }
    }

    private String getFeatureFlagParameter(FeatureFlag featureFlags) {
        return environment.getProperty(featureFlags.enabled());
    }

    private FeatureFlag getFeatureFlag(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(FeatureFlag.class);
    }
}
