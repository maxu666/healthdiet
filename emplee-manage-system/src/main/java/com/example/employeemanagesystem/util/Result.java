package com.example.employeemanagesystem.util;

public class Result<T>  {//使用泛型 T表示任意类型
    private Integer code;
    private String message;
    private T data;

    public Result<T> success(String message, T data) {
        this.code = 200;
        this.message = message;
        this.data = data;
        return this;
    }

    public Result<T> failed(String message) {
        this.code = 500;
        this.message = message;
        return this;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
