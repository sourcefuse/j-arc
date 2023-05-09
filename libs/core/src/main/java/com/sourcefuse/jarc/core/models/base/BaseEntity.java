package com.sourcefuse.jarc.core.models.base;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {

  public abstract UUID getId();

  public String getTableName() {
    Table tableAnnotation = this.getClass().getAnnotation(Table.class);
    if (tableAnnotation != null) {
      return tableAnnotation.name();
    }
    return null;
  }
}
