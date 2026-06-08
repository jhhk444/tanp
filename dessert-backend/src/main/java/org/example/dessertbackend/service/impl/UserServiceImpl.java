package org.example.dessertbackend.service.impl;

import org.example.dessertbackend.entity.User;
import org.example.dessertbackend.mapper.UserMapper;
import org.example.dessertbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Map<String, Object> getList() {
        List<User> list = userMapper.selectList();
        int total = userMapper.count();
        Map<String, Object> map = new HashMap<>();
        map.put("records", list);
        map.put("total", total);
        return map;
    }

    @Override
    public int register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (userMapper.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        if (userMapper.findByPhone(user.getPhone()) != null) {
            throw new RuntimeException("手机号已被注册");
        }
        
        Integer maxId = userMapper.getMaxId();
        user.setId(maxId + 1);
        
        return userMapper.insert(user);
    }

    @Override
    public int delete(Integer id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int update(User user) {
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new RuntimeException("用户名已存在");
        }
        
        existingUser = userMapper.findByEmail(user.getEmail());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new RuntimeException("邮箱已被使用");
        }
        
        existingUser = userMapper.findByPhone(user.getPhone());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new RuntimeException("手机号已被使用");
        }
        
        return userMapper.update(user);
    }

    @Override
    public int batchDelete(List<Integer> ids) {
        return userMapper.batchDelete(ids);
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}