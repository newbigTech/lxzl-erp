package com.lxzl.erp.core.component;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.se.common.domain.Result;
import org.springframework.stereotype.Component;

@Component
public class ResultGenerator {
    private static final String SUCCESS_CODE = ErrorCode.SUCCESS;
    private static final String CUSTOM_ERROR_CODE = ErrorCode.CUSTOM_ERROR;
    public Result generate(String code, Object obj){
        Result result = null;
        if(SUCCESS_CODE.equals(code)){
            result = new Result(SUCCESS_CODE,ErrorCode.getMessage(SUCCESS_CODE),true);
            result.setProperty("data", obj);
        }else{
            result = new Result(code,ErrorCode.getMessage(code),false);
        }

        if(CUSTOM_ERROR_CODE.equals(code)){
            ErrorCode.clear(CUSTOM_ERROR_CODE);
        }
        return result;
    }
    public Result generate(String code){
        return generate(code,"");
    }
}