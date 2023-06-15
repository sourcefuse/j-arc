package com.sourcefuse.jarc.services.featuretoggleservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle;

public interface FeatureToggleRepository extends CrudRepository<FeatureToggle, UUID> {

}
