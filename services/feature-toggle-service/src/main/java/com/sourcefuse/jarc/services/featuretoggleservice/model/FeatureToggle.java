package com.sourcefuse.jarc.services.featuretoggleservice.model;

import java.util.UUID;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "feature_toggle", schema = "main")
public class FeatureToggle extends UserModifiableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "feature_name")
	private String featureName;

	@Column(name = "feature_enabled")
	private Integer featureEnabled;

	@Column(name = "strategy_id")
	private String strategyId;

	@Column(name = "strategy_params")
	@Nullable
	private String strategyParams;

}
