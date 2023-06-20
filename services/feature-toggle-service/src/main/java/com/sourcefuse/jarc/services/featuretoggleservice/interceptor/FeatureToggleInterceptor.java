package com.sourcefuse.jarc.services.featuretoggleservice.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.togglz.core.context.FeatureContext;
import org.togglz.core.manager.FeatureManager;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle;
import com.sourcefuse.jarc.services.featuretoggleservice.services.FeatureHandlerService;

@Aspect
@Component
public class FeatureToggleInterceptor {

	private final Logger log = LoggerFactory.getLogger(FeatureToggleInterceptor.class);
	@Autowired
	private FeatureManager featureManager;
	@Autowired
	private FeatureHandlerService featureHandlerService;

	// @Around("@annotation(com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle)")
	@Around("@annotation(featureToggle)")
	// @Before("@annotation(com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle)")

	public Object checkIfAllowed(final ProceedingJoinPoint pj, FeatureToggle featureToggle) throws Throwable {

		FeatureContext.clearCache();

		if (FeatureContext.getFeatureManager().isActive(featureToggle.value())) {
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
