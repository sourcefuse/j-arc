package com.sourcefuse.jarc.core.models.base;

import java.util.UUID;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {

	public abstract UUID getId();

//	public abstract boolean isDeleted();

	public String getTableName() {
		Table tableAnnotation = this.getClass().getAnnotation(Table.class);
		if (tableAnnotation != null) {
			return tableAnnotation.name();
		}
		return null;
	}
}