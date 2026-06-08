package org.example.dessertbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.dessertbackend.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectList();
    int count();
    int insert(User user);
    int deleteById(Integer id);
    int update(User user);
    int batchDelete(@Param("ids") List<Integer> ids);
    User findByUsername(@Param("username") String username);
    User findByEmail(@Param("email") String email);
    User findByPhone(@Param("phone") String phone);
    Integer getMaxId();
}