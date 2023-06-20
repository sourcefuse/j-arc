package com.sourcefuse.jarc.services.featuretoggleservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sourcefuse.jarc.services.featuretoggleservice.enums.Features;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface FeatureToggle {

	Features value();
	String handler() default "";
}
