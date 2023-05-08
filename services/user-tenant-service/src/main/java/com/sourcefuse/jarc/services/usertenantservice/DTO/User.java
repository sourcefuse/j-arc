package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
//@NoArgsConstructor
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users", schema = "main")
//@SecondaryTable(name = "users")
public class User extends UserModifiableEntity {

    //doubt:
    //implements IAuthUser
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @NotBlank
    @Column(nullable = false)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String email;

    private String designation;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phone;

    @Column(name = "auth_client_ids")
    private String authClientIds;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "photo_url")
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    private Date dob;

 /* @ManyToOne
  @JoinColumn(name = "default_tenant_id")
  //doubt:
  //this not found at Tenant class
  private Tenant defaultTenant;*/

    @Column(name = "default_tenant_id")
    private UUID defaultTenantId;

    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL)
    private UserCredentials credentials;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    //private List<UserTenant> userTenants = new ArrayList<>();
    private List<UserTenant> userTenants;

//doubt
//  public User(MessageHandler.Partial<User> data) {
//    super();
//  }
}