package com.sourcefuse.jarc.services.featuretoggleservice.services;

import org.togglz.core.Feature;

public interface ConvertEnum  {

	public Feature getEnum(String enumName) throws IllegalArgumentException;
}
