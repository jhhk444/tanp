package org.example.dessertbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.dessertbackend.entity.LoginRecord;

import java.util.List;

@Mapper
public interface LoginRecordMapper {
    List<LoginRecord> selectList();
    int count();
    int insert(LoginRecord record);
}