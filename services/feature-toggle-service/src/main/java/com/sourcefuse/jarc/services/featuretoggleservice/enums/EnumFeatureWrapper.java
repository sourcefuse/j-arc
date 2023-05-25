package com.sourcefuse.jarc.services.featuretoggleservice.enums;

import org.togglz.core.Feature;

public class EnumFeatureWrapper implements Feature {
	private Enum<?> enumValue;

	public EnumFeatureWrapper(Enum<?> enumValue) {
		this.enumValue = enumValue;
	}

	@Override
	public String name() {
		return enumValue.name();
	}
}
