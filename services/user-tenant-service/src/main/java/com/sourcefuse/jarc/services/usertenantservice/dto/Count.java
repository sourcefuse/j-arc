package com.sourcefuse.jarc.services.usertenantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Count {

  private Long totalCnt;

  public Long getTotalCnt() {
    return totalCnt;
  }

  public void setTotalCnt(Long totalCnt) {
    this.totalCnt = totalCnt;
  }
}
