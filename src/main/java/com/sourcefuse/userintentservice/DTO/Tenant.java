package com.sourcefuse.userintentservice.DTO;

import jakarta.persistence.*;
import com.sourcefuse.userintentservice.commons.UserModifiableEntity;
import com.sourcefuse.userintentservice.enums.TenantStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenants",schema = "main")
//@SecondaryTable(name = "tenants")
public class Tenant extends UserModifiableEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2") //uuid4
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    private TenantStatus status;

    @Column(name = "KEY")
    private String key;

    private String website;

    private String address;

    private String city;

    private String state;

    private String zip;

    private String country;

    @Column(name = "primary_contact_email")
    private String primaryContactEmail;

    @Column(name = "allowed_domain")
    private String allowedDomain;

    @Column(name = "tenant_type")
    private String tenantType;

    @OneToMany(mappedBy = "tenantId", cascade = CascadeType.ALL)
    private List<TenantConfig> tenantConfigs;

    @OneToMany(mappedBy = "tenantId", cascade = CascadeType.ALL)
    private List<UserTenant> userTenants;

}