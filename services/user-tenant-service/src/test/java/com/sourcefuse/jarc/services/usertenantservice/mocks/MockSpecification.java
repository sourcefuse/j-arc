package com.sourcefuse.jarc.services.usertenantservice.mocks;

import com.sourcefuse.jarc.core.filters.models.Filter;
import com.sourcefuse.jarc.core.filters.services.QueryService;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

public final class MockSpecification {

  private MockSpecification() {}

  public static Specification getSpecification(QueryService queryService) {
    Specification mockSpecificationTwo = null;
    Specification mockSpecification = Mockito.mock(Specification.class);
    Filter filter = null;
    Mockito
      .when(queryService.getSpecifications(filter))
      .thenReturn(mockSpecification);
    return mockSpecificationTwo;
  }
}
