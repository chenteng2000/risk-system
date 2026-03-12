package com.investment.risk.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 拦截所有的 RuntimeException 报错
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        // 把报错信息封装进咱们自己写的漂亮 Result 里返回给前端
        return Result.error(e.getMessage());
    }
}