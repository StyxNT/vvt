package com.styxnt.vvtserver.filter;

import com.styxnt.vvtserver.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author StyxNT
 * @date 2021/8/6
 */
//@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(tokenHeader);
        //存在token 必须以我们定义的(在properties.yml中)Bearer开头
        if(authHeader != null&&authHeader.startsWith(tokenHead)){
            //去除请求头中的tokenhead，获取token
            String authToken=authHeader.substring(tokenHead.length());
            String username = jwtTokenUtils.getUserNameFromToken(authToken);
            //用户名存在，但是SpringSecurity全局中没有用户对象，说明没登录,必须重新登录并把userdetails设置到SpringSecurity的全局上下文中
            if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){

//                System.out.println("JWT未登录---------------------------------------------->执行登录操作");

                //登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtTokenUtils.validateToken(authToken,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}

