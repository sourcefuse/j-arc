package com.sourcefuse.jarc.services.featuretoggleservice.repositories;

import java.util.UUID;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.featuretoggleservice.model.FeatureToggle;

public interface FeatureToggleRepository extends SoftDeletesRepository<FeatureToggle, UUID> {

}
