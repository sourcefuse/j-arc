//package com.sourcefuse.jarc.services.featuretoggleservice.service;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import org.springframework.core.env.Environment;
//import org.togglz.core.Feature;
//import org.togglz.core.metadata.FeatureGroup;
//import org.togglz.core.metadata.FeatureMetaData;
//import org.togglz.core.metadata.SimpleFeatureGroup;
//import org.togglz.core.repository.FeatureState;
//
//public class EnvironmentFeatureMetaData implements FeatureMetaData {
//	private Feature feature;
//	private Environment environment;
//
//	public EnvironmentFeatureMetaData(Feature feature, Environment environment) {
//		this.feature = feature;
//		this.environment = environment;
//	}
//
//	@Override
//	public String getLabel() {
//		return environment.getProperty("togglz.features." + feature.name() + ".label", feature.name());
//	}
//
//	@Override
//	public FeatureState getDefaultFeatureState() {
//		boolean defaultEnabledState = environment.getProperty("togglz.features." + feature.name() + ".enabled",
//				Boolean.class, false);
//		return new FeatureState(feature, defaultEnabledState);
//	}
//
//	@Override
//	public Set<FeatureGroup> getGroups() {
//		String group = environment.getProperty("togglz.features." + feature.name() + ".group");
//		final HashSet<FeatureGroup> featureGroups = new HashSet<>();
//		if (group != null) {
//			featureGroups.add(new SimpleFeatureGroup(group));
//		}
//		return featureGroups;
//	}
//
//	@Override
//	public Map<String, String> getAttributes() {
//		return new HashMap<>();
//	}
//}
