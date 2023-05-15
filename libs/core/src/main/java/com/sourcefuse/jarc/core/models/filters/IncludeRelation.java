package com.sourcefuse.jarc.core.models.filters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncludeRelation {

  String relation;
  Filter scope = new Filter();
}
