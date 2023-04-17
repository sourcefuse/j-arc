package com.sourcefuse.usertenantservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupCheckDto {

    @Id
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private boolean isSignedUp;

}
