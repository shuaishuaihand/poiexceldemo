package com.agesun.mybatis.util;

import java.io.Serializable;

/**
 * @author:lh
 * @description:
 * @create:2018/04/25 13:52
 */
public class Json implements Serializable {

    private static final long serialVersionUID = 3395510469149034759L;

    private Integer result;

    private Object data;

    private String message="";

    private boolean isOperation;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOperation() {
        return isOperation;
    }

    public void setIsOperation(boolean operation) {
        isOperation = operation;
    }
}
