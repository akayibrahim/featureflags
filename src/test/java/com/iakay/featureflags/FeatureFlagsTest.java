package com.iakay.featureflags;

import com.iakay.featureflags.annotation.FeatureFlag;
import com.iakay.featureflags.aspect.FeatureFlags;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class FeatureFlagsTest {
    @Mock
    Environment environment;
    private ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
    private FeatureFlags featureFlagsAspect;

    @Before
    public void setup() {
        when(environment.getProperty("featureflags.unitteston")).thenReturn("true");
        when(environment.getProperty("featureflags.unittestoff")).thenReturn("false");
        featureFlagsAspect = new FeatureFlags(environment);
    }

    @Test
    public void featureFlagsOn() throws Throwable {
        MethodSignature signature = mock(MethodSignature.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(onMethod());
        featureFlagsAspect.beforeMethodExecution(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(1)).proceed();
        verify(proceedingJoinPoint, never()).proceed(null);
    }

    @Test
    public void featureFlagsOff() throws Throwable {
        MethodSignature signature = mock(MethodSignature.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(offMethod());
        featureFlagsAspect.beforeMethodExecution(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(0)).proceed();
        verify(proceedingJoinPoint, never()).proceed(null);
    }

    public Method onMethod() throws NoSuchMethodException {
        return getClass().getDeclaredMethod("onMethodStructure");
    }

    @FeatureFlag(enabled = "featureflags.unitteston")
    public void onMethodStructure() {
        System.out.println("Unit Test Feature Flags");
    }

    public Method offMethod() throws NoSuchMethodException {
        return getClass().getDeclaredMethod("offMethodStructure");
    }

    @FeatureFlag(enabled = "featureflags.unittestoff")
    public void offMethodStructure() {
        System.out.println("Unit Test Feature Flags");
    }
}
