package com.sourcefuse.jarc.services.featuretoggleservice.model;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
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
@Table(name = "feature_list", schema = "main")
public class FeatureList extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "feature_name")
  private String featureName;

  private String description;

  private String metadata; // extra metadata for specific handling
}
