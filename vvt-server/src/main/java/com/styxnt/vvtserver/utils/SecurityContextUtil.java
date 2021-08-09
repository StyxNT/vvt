package com.styxnt.vvtserver.utils;

import com.styxnt.vvtserver.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author StyxNT
 * @date 2021/8/9
 */
@Component
public class SecurityContextUtil {

    public static User getContextUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
