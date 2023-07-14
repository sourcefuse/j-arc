package com.sourcefuse.jarc.services.featuretoggleservice.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.togglz.core.context.FeatureContext;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle;
import com.sourcefuse.jarc.services.featuretoggleservice.services.ConvertEnum;
import com.sourcefuse.jarc.services.featuretoggleservice.services.FeatureHandlerService;

@Aspect
@Component
public class FeatureToggleInterceptor {

	@Autowired
	private FeatureHandlerService featureHandlerService;
	@Autowired(required = false)
	private ConvertEnum convertEnumImpl;

	@Around("@annotation(featureToggle)")

	public Object checkIfAllowed(final ProceedingJoinPoint pj, FeatureToggle featureToggle) throws Throwable {

		FeatureContext.clearCache();
		if(convertEnumImpl == null) {
			throw new NullPointerException("Provide an implementation for ConvertEnum");
		}
		if (FeatureContext.getFeatureManager().isActive(convertEnumImpl.getEnum(featureToggle.value()))) {
			if (featureToggle.handler().length() > 0) {

				boolean response = featureHandlerService.featureHandle(featureToggle);
				if (response) {
					return pj.proceed();
				} else {
					throw new ResponseStatusException(HttpStatus.FORBIDDEN,
							"You dont have access to this particular feature");
				}
			}
			return pj.proceed();
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have access to this particular feature");
		}

	}

}
