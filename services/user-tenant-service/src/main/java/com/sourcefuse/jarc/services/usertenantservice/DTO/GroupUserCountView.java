package com.sourcefuse.jarc.services.usertenantservice.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_group_user_count", indexes = @Index(columnList = "id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserCountView extends Group {

    @Column(name = "user_count")
    private Integer userCount = 0;

    //doubt
//    public GroupUserCountView(Long id, String name, Integer userCount) {
//        super(id, name);
//        this.userCount = userCount;
//    }
}
