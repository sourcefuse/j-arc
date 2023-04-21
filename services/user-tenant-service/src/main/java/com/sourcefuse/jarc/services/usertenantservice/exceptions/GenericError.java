package com.sourcefuse.jarc.services.usertenantservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericError {
    private String status;
    private String errCode;
    private String message;
    
    

}
