package com.sourcefuse.jarc.services.featuretoggleservice.repositories;

import java.util.UUID;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.featuretoggleservice.model.FeatureList;

public interface FeatureListRepository extends SoftDeletesRepository<FeatureList, UUID> {

}
