
package org.example.dessertbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dessertbackend.entity.LoginRecord;
import org.example.dessertbackend.entity.User;
import org.example.dessertbackend.service.LoginRecordService;
import org.example.dessertbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    
    @Autowired
    LoginRecordService loginRecordService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        return userService.getList();
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            userService.register(user);
            result.put("success", true);
            result.put("message", "注册成功");
        } catch (RuntimeException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            result.put("success", true);
            result.put("username", loginUser.getUsername());
            result.put("role", loginUser.getRole());
            
            LoginRecord record = new LoginRecord();
            record.setUsername(loginUser.getUsername());
            record.setIpAddress(getClientIp(request));
            record.setUserAgent(request.getHeader("User-Agent"));
            record.setLoginTime(LocalDateTime.now());
            loginRecordService.addRecord(record);
        } else {
            result.put("success", false);
        }
        return result;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            userService.update(user);
            result.put("success", true);
            result.put("message", "修改成功");
        } catch (RuntimeException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/batchDelete")
    public Map<String, Object> batchDelete(@RequestBody Map<String, List<Integer>> params) {
        userService.batchDelete(params.get("ids"));
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }
}
