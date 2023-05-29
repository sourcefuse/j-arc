package com.sourcefuse.jarc.core.test.models;

import com.sourcefuse.jarc.core.entitylisteners.AuditLogEntityListener;
import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", schema = "main")
@EntityListeners(AuditLogEntityListener.class)
public class Role extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  String name;

  String permissions;
}
