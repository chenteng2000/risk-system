package com.investment.risk.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code; // 状态码：200代表成功，500代表失败
    private String message; // 给前端的提示信息
    private T data; // 真正要返回的数据放这里

    // 成功时的快捷方法
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 失败时的快捷方法
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
}