package com.sourcefuse.jarc.core.models.base;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import java.util.UUID;

@MappedSuperclass
public interface BaseEntity {
  default UUID getId() {
    return null;
  }

  default String getTableName() {
    Table tableAnnotation = this.getClass().getAnnotation(Table.class);
    if (tableAnnotation != null) {
      return tableAnnotation.name();
    }
}
