package com.sourcefuse.jarc.sandbox.featuretoggleexample.handlers;

import org.springframework.stereotype.Component;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureHandlers;

@Component("HelloHandler")
public class HelloHandler implements FeatureHandlers{

	@Override
	public boolean handle() {		
		System.out.println("inside the hello handler");
		return true;
	}

}
