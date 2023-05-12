package com.sourcefuse.jarc.core.models.filters;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filter {

  Map<String, Object> where;
  Map<String, Boolean> fields;
  List<IncludeRelation> include;
}
