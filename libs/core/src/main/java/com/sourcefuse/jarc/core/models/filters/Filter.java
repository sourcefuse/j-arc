package com.sourcefuse.jarc.core.models.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filter {

  Map<String, Object> where = new HashMap<>();
  Map<String, Boolean> fields = new HashMap<>();
  List<IncludeRelation> include = new ArrayList<>();
}
