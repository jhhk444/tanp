package org.example.dessertbackend.service.impl;

import org.example.dessertbackend.entity.LoginRecord;
import org.example.dessertbackend.mapper.LoginRecordMapper;
import org.example.dessertbackend.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {
    @Autowired
    LoginRecordMapper loginRecordMapper;

    @Override
    public Map<String, Object> getList() {
        List<LoginRecord> list = loginRecordMapper.selectList();
        int total = loginRecordMapper.count();
        Map<String, Object> map = new HashMap<>();
        map.put("records", list);
        map.put("total", total);
        return map;
    }

    @Override
    public int addRecord(LoginRecord record) {
        return loginRecordMapper.insert(record);
    }
}