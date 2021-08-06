package com.styxnt.vvtserver.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author StyxNT
 * @date 2021/8/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {


    /**
     * 状态码
     */
    private int code;
    private String message;
    private Object obj;

    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static CommonResponse success(String message){
        return new CommonResponse(200,message,null);
    }

    /**
     * 成功返回结果
     * @param message
     * @param obj
     * @return
     */
    public static CommonResponse success(String message,Object obj){
        return new CommonResponse(200,message,obj);
    }

    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static CommonResponse error(String message){
        return new CommonResponse(500,message,null);
    }

    /**
     * 失败返回结果
     * @param message
     * @param obj
     * @return
     */
    public static CommonResponse error(String message,Object obj){
        return new CommonResponse(500,message,obj);
    }

}
