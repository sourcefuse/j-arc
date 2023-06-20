package com.sourcefuse.jarc.services.featuretoggleservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureHandlers;
import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle;

@Service
public class FeatureHandlerService {

	@Autowired(required = false)
	private List<FeatureHandlers> featureHandler;
	
	
	public boolean featureHandle(FeatureToggle featureToggle) {
		FeatureHandlers handler = null;
		for(int i=0; i<featureHandler.size(); i++) {
			FeatureHandlers h = featureHandler.get(i);
			if(h.getClass().getName().equalsIgnoreCase(featureToggle.handler())) {
				return h.handle();
			}
		}
		
		return false;
	}
}
