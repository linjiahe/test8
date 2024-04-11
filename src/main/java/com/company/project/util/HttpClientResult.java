package com.company.project.util;

import java.io.Serializable;

/**
 * Description: 封装httpClient响应结果
 *
 * @author JourWon
 * @date Created on 2018年4月19日
 */
public class HttpClientResult implements Serializable {

    public HttpClientResult(int code,String content){
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HttpClientResult(int code){
        this.code = code;
    }

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;

}
