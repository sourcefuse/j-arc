package com.sourcefuse.jarc.services.featuretoggleservice.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.featuretoggleservice.model.FeatureToggle;
import java.util.UUID;

public interface FeatureToggleRepository
  extends SoftDeletesRepository<FeatureToggle, UUID> {}
