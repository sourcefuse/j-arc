package com.basic.example.facadeserviceexample.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import java.util.UUID;

@MappedSuperclass
public interface BaseEntity {
  default UUID getId() {
    return null;
  }

  @JsonIgnore
  default String getTableName() {
    Table tableAnnotation = this.getClass().getAnnotation(Table.class);
    if (tableAnnotation != null) {
      return tableAnnotation.name();
    }
    return null;
  }
}
