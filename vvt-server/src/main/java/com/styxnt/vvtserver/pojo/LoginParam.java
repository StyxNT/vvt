package com.styxnt.vvtserver.pojo;

import lombok.Data;
import org.springframework.boot.web.server.PortInUseException;

/**
 * @author StyxNT
 * @date 2021/8/6
 */
@Data
public class LoginParam {

    private String username;
    private String password;
}
