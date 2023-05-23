package com.sourcefuse.jarc.core.filters.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncludeRelation {

  String relation;
  Filter scope = new Filter();
}
