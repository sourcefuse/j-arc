package com.sourcefuse.jarc.core.models.base;

import java.util.UUID;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;


@MappedSuperclass
public interface BaseEntity {

	public abstract UUID getId();

	public default String getTableName() {
		Table tableAnnotation = this.getClass().getAnnotation(Table.class);
		if (tableAnnotation != null) {
			return tableAnnotation.name();
		}
		return null;
	}
}
