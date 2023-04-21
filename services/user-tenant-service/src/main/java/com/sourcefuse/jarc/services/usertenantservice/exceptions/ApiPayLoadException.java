package com.sourcefuse.jarc.services.usertenantservice.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Setter
@Getter
public class ApiPayLoadException extends  Exception{


    public String errorCode;
     public String errMsg;

    public ApiPayLoadException(String errorCode,String errMsg) {
      //  this(errMsg);
        this.errorCode = errorCode;
        this.errMsg=errMsg;

    }
    public ApiPayLoadException(String errMsg) {
       super(errMsg);
        this.errMsg=errMsg;

    }

}
