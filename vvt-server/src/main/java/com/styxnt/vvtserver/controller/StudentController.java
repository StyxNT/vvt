package com.styxnt.vvtserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生用户访问接口
 * @author StyxNT
 * @date 2021/8/8
 */
@RestController
@RequestMapping("/student")
@PreAuthorize("hasRole('student')")
public class StudentController {
}
