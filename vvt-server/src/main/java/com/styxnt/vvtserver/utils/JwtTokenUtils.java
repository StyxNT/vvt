package com.styxnt.vvtserver.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author StyxNT
 */
@Component
public class JwtTokenUtils {

    /**
     * 荷载中的用户名
     */
    private static final String CLAIM_KEY_USERNAME="sub";
    /**
     *  荷载中的jwt创建时间
     */
    private static final String CLAIM_KEY_CREATED="created";
    /**
     * jwt密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * jwt失效时间
     */
    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        //荷载
        Map<String,Object> claims=new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 根据荷载生成jwt token
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }
    /**
     * 生成token失效时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis()+expiration*1000);
    }

    /**
     * 从token中获取登录的用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims=getClaimsFromToken(token);
            username=claims.getSubject();
        }catch (Exception e){
            username=null;
        }
        return username;
    }

    /**
     * 从token获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims=Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 检验token是否有效，包括token是否过期以及登录名是否等于token载荷中保存的用户名
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails){
        String username=getUserNameFromToken(token);
        return username.equals(userDetails.getUsername())&&!isTokenExpired(token);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return 返回true表示token已经过期
     */
    private boolean isTokenExpired(String token) {
        Date expireDate=getExpireDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpireDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token是否可以刷新，过期的话可以刷新
     * @param token
     * @return
     */
    public boolean canRefreshToken(String token){
        return isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }
}

