package com.personnel.common.exception;

import com.personnel.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class GlobalExceptionHandler {
    // 异常处理
    @ExceptionHandler(Exception.class)
    // 返回json数据
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(null);
    }
}
