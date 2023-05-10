package com.sourcefuse.jarc.core.models.session;

import com.sourcefuse.jarc.core.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser<T extends BaseEntity> {

  private T user;
}
