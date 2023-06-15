package com.sourcefuse.jarc.services.featuretoggleservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.sourcefuse.jarc.services.featuretoggleservice.model.FeatureList;

public interface FeatureListRepository extends CrudRepository<FeatureList, UUID> {

}
