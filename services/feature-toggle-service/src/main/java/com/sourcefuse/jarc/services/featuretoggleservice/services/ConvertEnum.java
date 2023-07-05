package com.sourcefuse.jarc.services.featuretoggleservice.services;

import org.togglz.core.Feature;

public interface ConvertEnum {
  Feature getEnum(String enumName) throws IllegalArgumentException;
}
