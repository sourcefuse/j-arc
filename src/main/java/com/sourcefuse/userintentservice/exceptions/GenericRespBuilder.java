package com.sourcefuse.userintentservice.exceptions;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class GenericRespBuilder {
    public String buildGenerResp(String errcd,String msg){
        GenericError genericError=new GenericError(errcd,"V",msg);
        return (new Gson().toJson(genericError));
    }
}
