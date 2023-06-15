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

@Aspect
@Component
public class FeatureToggleInterceptor {

	private final Logger log = LoggerFactory.getLogger(FeatureToggleInterceptor.class);
	@Autowired
	private FeatureManager featureManager;

	// @Around("@annotation(com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle)")
	@Around("@annotation(featureToggle)")
//	@Before("@annotation(com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle)")

	public Object checkIfAllowed(final ProceedingJoinPoint pj, FeatureToggle featureToggle) throws Throwable {
//		ObjectMapper om = new ObjectMapper();

//		log.info("this is request" + om.writeValueAsString(pj.getArgs()));
//		MethodSignature signature = (MethodSignature) pj.getSignature();
//
//		Method method = signature.getMethod();
//
//		FeatureToggle annotation = method.getAnnotation(FeatureToggle.class);
//		String key = annotation.value();
//		log.info("feature ki key---" + key);

		// log.info("this is response" + om.writeValueAsString(pj.proceed()));
		// return pj.proceed();

		FeatureContext.clearCache();

		if (FeatureContext.getFeatureManager().isActive(featureToggle.value())) {
			return pj.proceed();
		} else {

			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have access to this particular feature");
		}

	}

}
