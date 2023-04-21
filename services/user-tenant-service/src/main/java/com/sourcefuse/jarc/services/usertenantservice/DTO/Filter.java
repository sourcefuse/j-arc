package com.sourcefuse.jarc.services.usertenantservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Filter {

    List<Tenant> and;
    List<Tenant> or;
}
