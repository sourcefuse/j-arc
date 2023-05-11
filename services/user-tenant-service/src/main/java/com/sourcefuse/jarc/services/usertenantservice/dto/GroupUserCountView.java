package com.sourcefuse.jarc.services.usertenantservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_group_user_count", indexes = @Index(columnList = "id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserCountView extends Group implements Serializable {

  private static final long serialVersionUID = 1905122041950251209L;

  @Column(name = "user_count")
  private Integer userCount = 0;
}
