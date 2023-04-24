package com.sourcefuse.jarc.services.auditservice.models;

import java.util.UUID;

import jakarta.persistence.Table;

public interface BaseModel {

	default UUID getId() {
		return null;
	}

	default UUID getCreatedBy() {
		return null;
	}

	default UUID getModifiedBy() {
		return null;
	}
	
	default String getTableName() {
        Table tableAnnotation = this.getClass().getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        }
        return null;
    }
}