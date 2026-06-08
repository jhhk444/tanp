package org.example.dessertbackend.service;

import org.example.dessertbackend.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Object> getList();
    int register(User user);
    int delete(Integer id);
    int update(User user);
    int batchDelete(List<Integer> ids);
    User login(String username, String password);
}