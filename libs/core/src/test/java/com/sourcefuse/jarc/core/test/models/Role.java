package com.sourcefuse.jarc.core.test.models;

import com.sourcefuse.jarc.core.entitylisteners.AuditLogEntityListener;
import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", schema = "main")
@EntityListeners(AuditLogEntityListener.class)
public class Role extends SoftDeleteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  String name;

  String permissions;

  @OneToMany(
    mappedBy = "role",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL
  )
  @Where(clause = "deleted = false")
  List<User> users;
}
