package com.sourcefuse.jarc.services.featuretoggleservice.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.featuretoggleservice.model.FeatureList;
import java.util.UUID;

public interface FeatureListRepository
  extends SoftDeletesRepository<FeatureList, UUID> {}
