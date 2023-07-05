package com.sourcefuse.jarc.services.featuretoggleservice.services;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureHandlers;
import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle;
import java.util.List;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureHandlerService {

  @Nullable
  private final List<FeatureHandlers> featureHandler;

  public boolean featureHandle(FeatureToggle featureToggle) {
    if (featureHandler == null) {
      throw new IllegalArgumentException(
        "Provide an implementation for ConvertEnum"
      );
    }
    for (int i = 0; i < featureHandler.size(); i++) {
      FeatureHandlers h = featureHandler.get(i);
      if (h.getClass().getName().equalsIgnoreCase(featureToggle.handler())) {
        return h.handle();
      }
    }

    return false;
  }
}
