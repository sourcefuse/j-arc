package com.sourcefuse.jarc.services.featuretoggleservice.model;

import java.util.UUID;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "todo", schema = "main")
public class Todo extends UserModifiableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column(name = "task_name")
	String taskName;
	@Column(name = "task_description")
	String taskDescription;
}
