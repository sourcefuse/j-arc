package com.sourcefuse.jarc.sandbox.featuretoggleexample.handlers;

import org.springframework.stereotype.Component;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureHandlers;

@Component
public class HeyHandler implements FeatureHandlers{

	@Override
	public boolean handle() {
		System.out.println("inside the hey handler");
		return true;
	}

}
