package com.sourcefuse.jarc.services.auditservice.models;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity extends SoftDeleteEntity {

	@CreatedDate
    Date createdOn;
	
	@LastModifiedDate
    Date modifiedOn;
	
	public abstract UUID getId();
	
	public String getTableName() {
        Table tableAnnotation = this.getClass().getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        }
        return null;
    }
}